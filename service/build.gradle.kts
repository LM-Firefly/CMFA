import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(project(":core"))
    implementation(project(":common"))

    ksp(libs.kaidl.compiler)
    ksp(libs.androidx.room.compiler)

    implementation(libs.kotlin.coroutine)
    implementation(libs.kotlin.serialization.json)
    implementation(libs.androidx.core)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.kaidl.runtime)
    implementation(libs.rikkax.multiprocess)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
}

android {
    sourceSets.all {
        kotlin.directories.add(layout.buildDirectory.dir("generated/ksp/${name}/kotlin").get().asFile.absolutePath)
        java.directories.add(layout.buildDirectory.dir("generated/ksp/${name}/java").get().asFile.absolutePath)
    }
}

abstract class ProcessKspTask : DefaultTask() {
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val kspBaseDir: DirectoryProperty

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun process() {
        val base = kspBaseDir.get().asFile
        if (!base.exists()) {
            logger.info("[processKsp] Skip: ${base.name} not found")
            return
        }
        base.listFiles()?.forEach { variantDir ->
            val file = variantDir.resolve("kotlin/com/github/kr328/clash/service/remote/IProfileManager.kt")
            if (!file.exists()) {
                logger.info("[processKsp] Skip missing ${file.relativeTo(base)}")
                return@forEach
            }
            var text = file.readText()
            val original = text
            run {
                val lines = text.lines().toMutableList()
                var seen = false
                var changed = false
                var idx = 0
                while (idx < lines.size) {
                    val l = lines[idx].trim()
                    if (l.startsWith("@file:Suppress(\"DEPRECATION\"")) {
                        if (seen) {
                            lines.removeAt(idx)
                            changed = true
                            continue
                        } else {
                            seen = true
                        }
                    }
                    idx++
                }
                if (changed) text = lines.joinToString("\n")
            }
            val writeRegex = Regex("writeSerializable\\(([^)]+)\\)")
            text = writeRegex.replace(text) { m ->
                val expr = m.groupValues[1].trim()
                if (expr.contains("toString()")) m.value else "writeString(${expr}.toString())"
            }
            val readRegex = Regex("([`]?data[`]?|reply)\\.readSerializable\\(\\) as UUID")
            text = readRegex.replace(text) { m ->
                val src = m.groupValues[1]
                "UUID.fromString(${src}.readString()!!)"
            }
            var modified = text != original
            if (modified && !text.contains("import java.util.UUID")) {
                text = text.replaceFirst(
                    "package com.github.kr328.clash.service.remote",
                    "package com.github.kr328.clash.service.remote\n\nimport java.util.UUID"
                )
            }
            val suppressRegex = Regex("^@file:Suppress\\(([^)]*)\\)\\s$", RegexOption.MULTILINE)
            val allArgs = suppressRegex.findAll(text)
                .flatMap { match ->
                    match.groupValues[1]
                        .split(',')
                        .map { it.trim().trim('"') }
                        .filter { it.isNotBlank() }
                }
                .toMutableSet()
            allArgs.add("DEPRECATION")
            val ordered = listOf(
                "DEPRECATION",
                "NAME_SHADOWING",
                "UNUSED_VARIABLE",
                "UNNECESSARY_NOT_NULL_ASSERTION",
                "UNUSED_PARAMETER"
            ).filter { allArgs.contains(it) }
            val suppressRegexAll = Regex("^@file:Suppress\\(([^)]*)\\)\\s*$", RegexOption.MULTILINE)
            text = suppressRegexAll.replace(text, "").trimStart('\n','\r')
            val suppressLine = "@file:Suppress(${ordered.joinToString { "\"$it\"" }})"
            val optimizedComment = "// UUID serialization optimized: replaced Serializable with String based transport"
            val hasOptimizedComment = text.contains(optimizedComment)
            val builder = StringBuilder()
            builder.append(suppressLine).append('\n')
            if (!hasOptimizedComment) builder.append(optimizedComment).append('\n')
            var body = if (hasOptimizedComment) {
                var first = true
                text.lines().filterNot { line ->
                    if (line.trim() == optimizedComment) {
                        if (first) { first = false; false } else true
                    } else false
                }.joinToString("\n")
            } else text
            body = body.trimStart('\n','\r')
            text = builder.append(body).toString()
            if (text != original) {
                file.writeText(text)
                logger.lifecycle("[processKsp] Updated ${file.relativeTo(base)} (merged Suppress)")
            } else {
                logger.info("[processKsp] No changes for ${file.relativeTo(base)}")
            }
        }
    }
}

val kspBaseProvider = layout.buildDirectory.dir("generated/ksp")
val processKspTask = tasks.register<ProcessKspTask>("processKspGeneratedProfileManager") {
    group = "build setup"
    description = "Normalize generated IProfileManager.kt to avoid Serializable for UUID and dedupe file suppress"
    kspBaseDir.set(kspBaseProvider)
    outputDir.set(kspBaseProvider)
}

tasks.matching { it.name.startsWith("ksp") && it.name.endsWith("Kotlin") }.configureEach {
    finalizedBy(processKspTask)
}
tasks.matching { it.name.startsWith("compile") && it.name.endsWith("Kotlin") }.configureEach {
    dependsOn(processKspTask)
}
tasks.matching { it.name.startsWith("extract") && it.name.contains("Annotations") }.configureEach {
    dependsOn(processKspTask)
}