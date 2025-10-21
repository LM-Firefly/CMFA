plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
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
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")
}

// 在配置阶段追加 KSP 生成目录到所有 sourceSet，避免 afterEvaluate 与变体 API 依赖
android {
    sourceSets.all {
        kotlin.srcDir(layout.buildDirectory.dir("generated/ksp/${name}/kotlin").get().asFile)
        java.srcDir(layout.buildDirectory.dir("generated/ksp/${name}/java").get().asFile)
    }
}

// 合并处理：优化 UUID 序列化 + 注入 Suppress (幂等)
val projDir: java.io.File = project.projectDir
val kspBaseProvider = layout.buildDirectory.dir("generated/ksp")
val processKspTask = tasks.register("processKspGeneratedProfileManager") {
    group = "build setup"
    description = "Normalize generated IProfileManager.kt to avoid Serializable for UUID and dedupe file suppress"
    // 声明输入输出，便于增量与配置缓存
    inputs.dir(kspBaseProvider)
    // 目标文件位于多个变体目录下，粗粒度声明整个 ksp 输出目录为输出
    outputs.dir(kspBaseProvider)
    doLast {
        // 遍历所有可能的变体输出，避免使用 libraryVariants（兼容 AGP/Gradle 未来版本）
    val base = kspBaseProvider.get().asFile
        if (!base.exists()) {
            println("[processKsp] Skip: ${base.relativeToOrNull(project.projectDir)} not found")
            return@doLast
        }
        base.listFiles()?.forEach { variantDir ->
            val file = variantDir.resolve("kotlin/com/github/kr328/clash/service/remote/IProfileManager.kt")
            if (!file.exists()) {
                println("[processKsp] Skip missing ${file.relativeToOrNull(projDir)}")
                return@forEach
            }
            var text = file.readText()
            val original = text
            // 去重重复的 @file:Suppress("DEPRECATION") 行，确保只保留一条
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

            // writeSerializable(expr)
            val writeRegex = Regex("writeSerializable\\(([^)]+)\\)")
            text = writeRegex.replace(text) { m ->
                val expr = m.groupValues[1].trim()
                if (expr.contains("toString()")) m.value else "writeString(${expr}.toString())"
            }
            // readSerializable() as UUID
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
            // ==== 合并重复文件级 Suppress 注解 ====
            val suppressRegex = Regex("^@file:Suppress\\(([^)]*)\\)\\s$", RegexOption.MULTILINE)
            val allArgs = suppressRegex.findAll(text)
                .flatMap { match ->
                    match.groupValues[1]
                        .split(',')
                        .map { it.trim().trim('"') }
                        .filter { it.isNotBlank() }
                }
                .toMutableSet()
            // 期望集合（保留出现过的 + 我们需要的 DEPRECATION）
            allArgs.add("DEPRECATION")
            // 规范排序（可重复构建稳定）
            val ordered = listOf(
                "DEPRECATION",
                "NAME_SHADOWING",
                "UNUSED_VARIABLE",
                "UNNECESSARY_NOT_NULL_ASSERTION",
                "UNUSED_PARAMETER"
            ).filter { allArgs.contains(it) }
            // 移除所有旧 Suppress 行
            val suppressRegexAll = Regex("^@file:Suppress\\(([^)]*)\\)\\s*$", RegexOption.MULTILINE)
            text = suppressRegexAll.replace(text, "").trimStart('\n','\r')
            // 插入统一的 Suppress 行
            val suppressLine = "@file:Suppress(${ordered.joinToString { "\"$it\"" }})"
            // 处理优化注释（保证仅一份）
            val optimizedComment = "// UUID serialization optimized: replaced Serializable with String based transport"
            val hasOptimizedComment = text.contains(optimizedComment)
            // 确定插入位置（保持在第一行，紧接后面跟优化注释 + 原始内容）
            val builder = StringBuilder()
            builder.append(suppressLine).append('\n')
            if (!hasOptimizedComment) builder.append(optimizedComment).append('\n')
            // 避免重复的优化注释：删除正文里额外的同样注释
            var body = if (hasOptimizedComment) {
                // 去除除首个之外的重复
                var first = true
                text.lines().filterNot { line ->
                    if (line.trim() == optimizedComment) {
                        if (first) { first = false; false } else true
                    } else false
                }.joinToString("\n")
            } else text
            // 确保不以空行开头
            body = body.trimStart('\n','\r')
            text = builder.append(body).toString()
            if (text != original) {
                file.writeText(text)
                println("[processKsp] Updated ${file.relativeTo(projDir)} (merged Suppress)")
            } else {
                println("[processKsp] No changes for ${file.relativeTo(projDir)}")
            }
        }
    }
}

// 绑定 KSP -> process -> compile 顺序（基于任务名匹配，避免使用变体 API）
tasks.matching { it.name.startsWith("ksp") && it.name.endsWith("Kotlin") }.configureEach {
    finalizedBy(processKspTask)
}
tasks.matching { it.name.startsWith("compile") && it.name.endsWith("Kotlin") }.configureEach {
    dependsOn(processKspTask)
}