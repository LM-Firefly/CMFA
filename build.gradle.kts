@file:Suppress("UNUSED_VARIABLE")

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import java.net.URI
import java.util.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

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

    apply(plugin = if (isApp) "com.android.application" else "com.android.library")


    if (isApp) {
        // Gradle 9.0+ 兼容写法，设置 archivesName，自动跟随 appVersionName
        extensions.configure<org.gradle.api.plugins.BasePluginExtension> {
            archivesName.set("cmfa-$appVersionName")
        }
    }

    extensions.configure<BaseExtension> {
        buildFeatures.buildConfig = true
        defaultConfig {
            if (isApp) {
                applicationId = "com.github.metacubex.mihomo"
            }

            project.name.let { name ->
                namespace = if (name == "app") "com.github.kr328.clash"
                else "com.github.kr328.clash.$name"
            }

            minSdk = 23
            // 同步升级 targetSdk
            targetSdk = 36

            versionName = "2.11.17"
            versionCode = 211017

            resValue("string", "release_name", "v$versionName")
            resValue("integer", "release_code", "$versionCode")

            ndk {
                abiFilters += listOf("arm64-v8a", "armeabi-v7a", "x86", "x86_64")
            }

            externalNativeBuild {
                cmake {
                    abiFilters("arm64-v8a", "armeabi-v7a", "x86", "x86_64")
                }
            }

            if (!isApp) {
                consumerProguardFiles("consumer-rules.pro")
            }
        }

        ndkVersion = "29.0.14206865"

        // 与 targetSdk 脱钩后显式指定 compileSdk，便于后续分阶段升级
        compileSdkVersion(36)

        if (isApp) {
            packagingOptions {
                resources {
                    excludes.add("DebugProbesKt.bin")
                }
            }
        }

        productFlavors {
            flavorDimensions("feature")

            create("alpha") {
                isDefault = true
                dimension = flavorDimensionList[0]
                versionNameSuffix = ".Alpha"

                buildConfigField("boolean", "PREMIUM", "Boolean.parseBoolean(\"false\")")

                resValue("string", "launch_name", "@string/launch_name_alpha")
                resValue("string", "application_name", "@string/application_name_alpha")

            }

            create("meta") {

                dimension = flavorDimensionList[0]
                versionNameSuffix = ".Meta"

                buildConfigField("boolean", "PREMIUM", "Boolean.parseBoolean(\"false\")")

                resValue("string", "launch_name", "@string/launch_name_meta")
                resValue("string", "application_name", "@string/application_name_meta")
            }
        }

        sourceSets {
            getByName("meta") {
                java.srcDirs("src/foss/java")
            }
            getByName("alpha") {
                java.srcDirs("src/foss/java")
            }
        }

        signingConfigs {
            val keystore = rootProject.file("signing.properties")
            if (keystore.exists()) {
                create("release") {
                    val prop = Properties().apply {
                        keystore.inputStream().use(this::load)
                    }

                    storeFile = rootProject.file("release.keystore")
                    storePassword = prop.getProperty("keystore.password")!!
                    keyAlias = prop.getProperty("key.alias")!!
                    keyPassword = prop.getProperty("key.password")!!
                }
            }
        }

        buildTypes {
            named("release") {
                isMinifyEnabled = isApp
                isShrinkResources = isApp
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

        buildFeatures.apply {
            dataBinding {
                isEnabled = name != "hideapi"
            }
        }

        if (isApp) {
            this as AppExtension

            splits {
                abi {
                    isEnable = true
                    isUniversalApk = true
                    reset()
                    include("arm64-v8a", "armeabi-v7a", "x86", "x86_64")
                }
            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_24
            targetCompatibility = JavaVersion.VERSION_24
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        @Suppress("UnstableApiUsage")
        compilerOptions.jvmTarget.set(JvmTarget.JVM_24)
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
    rejectVersionIf {
        isNonStable(candidate.version) && !isNonStable(currentVersion)
    }
    outputFormatter = "json"
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"
}