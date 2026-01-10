@file:Suppress("UNUSED_VARIABLE")

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import java.net.URI
import java.util.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

// 全局版本号变量，便于同步
val appVersionName = "2.11.20"
val appVersionCode = 211020
plugins {
    // 通过 Version Catalog 声明核心插件版本, 避免旧式 buildscript classpath 写法
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.ksp) apply false
}

buildscript {
    repositories {
        mavenCentral()
        google()
        maven("https://raw.githubusercontent.com/MetaCubeX/maven-backup/main/releases")
    }
    dependencies {
        // 迁移至自定义 Go 构建任务后，不再需要旧 golang-android 插件
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

    // 根据项目类型配置 Android 扩展
    if (isApp) {
        extensions.configure<ApplicationExtension> {
            namespace = "com.github.kr328.clash"
            defaultConfig {
                applicationId = "com.github.metacubex.mihomo"
                minSdk = 23
                targetSdk = 36
                versionName = appVersionName
                versionCode = appVersionCode
                resValue("string", "release_name", "v$versionName")
                resValue("integer", "release_code", versionCode.toString())
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
        compileSdk = 36
            packaging {
                resources {
                    excludes.add("DebugProbesKt.bin")
                }
            }
            flavorDimensions += "feature"
            productFlavors {
                create("alpha") {
                    dimension = "feature"
                    versionNameSuffix = ".Alpha"
                    // flag removed
                    resValue("string", "launch_name", "@string/launch_name_alpha")
                    resValue("string", "application_name", "@string/application_name_alpha")
                }
                create("meta") {
                    dimension = "feature"
                    versionNameSuffix = ".Meta"
                    // flag removed
                    resValue("string", "launch_name", "@string/launch_name_meta")
                    resValue("string", "application_name", "@string/application_name_meta")
                }
            }
            sourceSets {
                getByName("meta") {
                    java.srcDir("src/foss/java")
                }
                getByName("alpha") {
                    java.srcDir("src/foss/java")
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
                    isMinifyEnabled = true
                    isShrinkResources = true
                    signingConfig = signingConfigs.findByName("release") ?: signingConfigs["debug"]
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
                named("debug") {
                    versionNameSuffix = ".debug"
                    signingConfig = signingConfigs.findByName("release") ?: signingConfigs["debug"]
                }
            }
            buildFeatures {
                buildConfig = true
                dataBinding = project.name != "hideapi"
            }
            splits {
                abi {
                    isEnable = true
                    isUniversalApk = true
                    reset()
                    include("arm64-v8a", "armeabi-v7a", "x86", "x86_64")
                }
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_24
                targetCompatibility = JavaVersion.VERSION_24
            }
        }
    } else {
        extensions.configure<LibraryExtension> {
            project.name.let { name ->
                namespace = "com.github.kr328.clash.$name"
            }
            defaultConfig {
                minSdk = 23

                resValue("string", "release_name", "v$appVersionName")
                resValue("integer", "release_code", appVersionCode.toString())

                ndk {
                    abiFilters += listOf("arm64-v8a", "armeabi-v7a", "x86", "x86_64")
                }

                externalNativeBuild {
                    cmake {
                        abiFilters("arm64-v8a", "armeabi-v7a", "x86", "x86_64")
                    }
                }
                
                consumerProguardFiles("consumer-rules.pro")
            }
        ndkVersion = "29.0.14206865"
        compileSdk = 36
            flavorDimensions += "feature"
            productFlavors {
                create("alpha") {
                    dimension = "feature"
                    // flag removed
                    if (project.name == "design") {
                        resValue("string", "launch_name", "@string/launch_name_alpha")
                        resValue("string", "application_name", "@string/application_name_alpha")
                    }
                }
                create("meta") {
                    dimension = "feature"
                    // flag removed
                    if (project.name == "design") {
                        resValue("string", "launch_name", "@string/launch_name_meta")
                        resValue("string", "application_name", "@string/application_name_meta")
                    }
                }
            }

            sourceSets {
                named("meta") {
                    java.srcDir("src/foss/java")
                }
                named("alpha") {
                    java.srcDir("src/foss/java")
                }
            }

            buildTypes {
                named("release") {
                    isMinifyEnabled = false
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
            }

            buildFeatures {
                buildConfig = true
                dataBinding = project.name != "hideapi"
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_24
                targetCompatibility = JavaVersion.VERSION_24
            }
        }
    }
    tasks.withType<KotlinCompile>().configureEach {
        @Suppress("UnstableApiUsage")
        compilerOptions.jvmTarget.set(JvmTarget.JVM_24)
    }
    tasks.withType<JavaCompile>().configureEach {
        options.compilerArgs.add("-Xlint:deprecation")
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
