import java.net.URI
import java.nio.file.Files
import java.nio.file.StandardCopyOption

plugins {
    kotlin("android")
    kotlin("kapt")
    id("com.android.application")
}

android {
    namespace = "com.github.kr328.clash"
    compileSdk = 35
    
    defaultConfig {
        applicationId = "com.github.metacubex.mihomo"
        minSdk = 21
        targetSdk = 35
        versionCode = 211016
        versionName = "2.11.16"
    }
}

dependencies {
    compileOnly(project(":hideapi"))

    implementation(project(":core"))
    implementation(project(":service"))
    implementation(project(":design"))
    implementation(project(":common"))

    implementation(libs.kotlin.coroutine)
    implementation(libs.androidx.core)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.coordinator)
    implementation(libs.androidx.recyclerview)
    implementation(libs.google.material)
}

tasks.named("clean", type = Delete::class) {
    delete(file("release"))
    delete(file("src/main/assets"))
}

val geoFilesDownloadDir = "src/main/assets"

tasks.register("downloadGeoFiles") {

    val geoFilesUrls = mapOf(
        "https://github.com/MetaCubeX/meta-rules-dat/raw/refs/heads/release/geoip.metadb" to "geoip.metadb",
        "https://github.com/MetaCubeX/meta-rules-dat/raw/refs/heads/release/geosite.dat" to "geosite.dat",
        // "https://github.com/MetaCubeX/meta-rules-dat/raw/refs/heads/release/country.mmdb" to "country.mmdb",
        "https://github.com/MetaCubeX/meta-rules-dat/raw/refs/heads/release/GeoLite2-ASN.mmdb" to "ASN.mmdb",
    )

    doLast {
        geoFilesUrls.forEach { (downloadUrl, outputFileName) ->
            val url = URI(downloadUrl).toURL()
            val outputPath = file("$geoFilesDownloadDir/$outputFileName")
            outputPath.parentFile.mkdirs()
            url.openStream().use { input ->
                Files.copy(input, outputPath.toPath(), StandardCopyOption.REPLACE_EXISTING)
                println("$outputFileName downloaded to $outputPath")
            }
        }
    }
}

afterEvaluate {
    val downloadGeoFilesTask = tasks["downloadGeoFiles"]

    tasks.forEach {
        if (it.name.startsWith("assemble")) {
            it.dependsOn(downloadGeoFilesTask)
        }
    }
}