@file:Suppress("UNUSED_VARIABLE")

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import java.net.URI
import java.util.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

// 全局版本号变量，便于同步
val appVersionName = "2.11.17"

plugins {
    // 通过 Version Catalog 声明核心插件版本，避免旧式 buildscript classpath 写法
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.ksp) apply false
    id("com.github.ben-manes.versions") version "0.53.0"
}

buildscript {
    repositories {
        mavenCentral()
        google()
        maven("https://raw.githubusercontent.com/MetaCubeX/maven-backup/main/releases")
    }
    dependencies {
        // 自定义 Go 插件尚未提供插件标记，仍通过 classpath 引入
        classpath(libs.build.golang)
    }
}

subprojects {
    repositories {
        mavenCentral()
        google()
        maven("https://raw.githubusercontent.com/MetaCubeX/maven-backup/main/releases")
    }

    val isApp = name == "app"

        // 统一签名配置，仅在 app 模块声明 release 签名
        if (isApp) {
            extensions.findByType(BaseExtension::class.java)?.apply {
                signingConfigs.create("release").apply {
                    val propsFile = rootProject.file("signing.properties")
                    if (propsFile.exists()) {
                        val props = java.util.Properties().apply { load(propsFile.inputStream()) }
                        storeFile = rootProject.file("release.keystore")
                        storePassword = props["keystore.password"] as String? ?: ""
                        keyAlias = props["key.alias"] as String? ?: ""
                        keyPassword = props["key.password"] as String? ?: ""
                    }
                }
            }
        }
    apply(plugin = if (isApp) "com.android.application" else "com.android.library")

    // Gradle 9.0+ 兼容写法，设置 archivesName，自动跟随 appVersionName
    if (isApp) {
        extensions.configure<org.gradle.api.plugins.BasePluginExtension> {
            archivesName.set("cmfa-$appVersionName")
        }
    }

    // 为 Kotlin 编译器声明工具链（将按需下载并使用对应版本的 JDK）
    extensions.findByType(KotlinProjectExtension::class.java)?.jvmToolchain(24)

    // Kotlin 编译目标
    tasks.withType<KotlinCompile>().configureEach {
        @Suppress("UnstableApiUsage")
        compilerOptions.jvmTarget.set(JvmTarget.JVM_24)
    }

    // 使用旧 DSL 进行统一配置，便于与 Kotlin 插件兼容
    extensions.findByType(BaseExtension::class.java)?.let { android ->
        val moduleName = project.name

        android.apply {
            namespace = if (moduleName == "app") "com.github.kr328.clash" else "com.github.kr328.clash.$moduleName"
            buildFeatures.buildConfig = true
            buildFeatures.resValues = true
            // 开启 Data Binding，使布局中的 @{} 表达式生效（旧 DSL）
            dataBinding {
                isEnabled = true
            }

            defaultConfig {
                if (moduleName == "app") {
                    applicationId = "com.github.metacubex.mihomo"
                    versionName = "2.11.18"
                    versionCode = 211018
                    resValue("string", "release_name", "v$versionName")
                    resValue("integer", "release_code", "$versionCode")
                } else {
                    consumerProguardFiles("consumer-rules.pro")
                }

                minSdk = 23
                targetSdk = 36

                ndk {
                    abiFilters += listOf("arm64-v8a", "armeabi-v7a", "x86", "x86_64")
                }

                externalNativeBuild {
                    cmake {
                        abiFilters("arm64-v8a", "armeabi-v7a", "x86", "x86_64")
                    }
                }
            }

            ndkVersion = "29.0.14206865"
            compileSdkVersion(36)

            if (moduleName == "app") {
                packagingOptions {
                    resources {
                        excludes.add("DebugProbesKt.bin")
                    }
                }
            }

            productFlavors {
                flavorDimensions("feature")

                create("alpha") {
                    if (moduleName == "app") versionNameSuffix = ".Alpha"
                    dimension = flavorDimensionList[0]
                    buildConfigField("boolean", "PREMIUM", "Boolean.parseBoolean(\"false\")")
                    resValue("string", "launch_name", "@string/launch_name_alpha")
                    resValue("string", "application_name", "@string/application_name_alpha")
                }

                create("meta") {
                    if (moduleName == "app") versionNameSuffix = ".Meta"
                    dimension = flavorDimensionList[0]
                    buildConfigField("boolean", "PREMIUM", "Boolean.parseBoolean(\"false\")")
                    resValue("string", "launch_name", "@string/launch_name_meta")
                    resValue("string", "application_name", "@string/application_name_meta")
                }
            }

            sourceSets {
                getByName("meta") { java.srcDirs("src/foss/java") }
                getByName("alpha") { java.srcDirs("src/foss/java") }
            }

            buildTypes {
                named("release") {
                    isMinifyEnabled = (moduleName == "app")
                    isShrinkResources = (moduleName == "app")
                    signingConfig = signingConfigs.findByName("release") ?: signingConfigs["debug"]
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
                named("debug") {
                    versionNameSuffix = ".debug"
                }
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_24
                targetCompatibility = JavaVersion.VERSION_24
            }
        }

        // 仅应用模块配置 ABI splits
        if (isApp) {
            (android as AppExtension).splits {
                abi {
                    isEnable = true
                    isUniversalApk = true
                    reset()
                    include("arm64-v8a", "armeabi-v7a", "x86", "x86_64")
                }
            }
        }
    }
}

tasks.register<Delete>("clean") {
    delete(layout.buildDirectory)
}

tasks.register("printJdkInfo") {
    group = "verification"
    description = "Prints the current JVM version and java.home used by Gradle"
    doLast {
        val props = listOf(
            "java.version",
            "java.vendor",
            "java.vm.name",
            "java.vm.version",
            "java.runtime.version",
            "java.home",
        )
        println("===== JDK Runtime Info (Gradle JVM) =====")
        props.forEach { k -> println("$k = ${System.getProperty(k)}") }
        println("java.class.version = ${System.getProperty("java.class.version")}")
    }
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL

    doLast {
        val sha256 = URI.create("${distributionUrl}.sha256").toURL().openStream()
            .use { it.reader().readText().trim() }

        file("gradle/wrapper/gradle-wrapper.properties")
            .appendText("distributionSha256Sum=$sha256")
    }
}

// 仅在根项目配置 versions 插件的任务行为
fun isNonStable(version: String): Boolean {
    val v = version.lowercase(Locale.ROOT)
    val stableKeywords = listOf("release", "final", "ga")
    val hasStableKeyword = stableKeywords.any { v.contains(it) }
    val regex = Regex("^[0-9,.v-]+(-r)?$")
    val isStable = hasStableKeyword || regex.matches(version)
    return !isStable
}

tasks.withType<DependencyUpdatesTask>().configureEach {
    checkForGradleUpdate = true
    gradleReleaseChannel = "current"
    // 仅在当前为稳定版时拒绝不稳定更新（避免 Alpha/Beta/RC 噪音）
//    rejectVersionIf {
//        isNonStable(candidate.version) && !isNonStable(currentVersion)
//    }
    outputFormatter = "json"
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"
}