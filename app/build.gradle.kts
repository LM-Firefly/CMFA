import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

plugins {
    kotlin("android")
    kotlin("kapt")
    id("com.android.application")
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

tasks.getByName("clean", type = Delete::class) {
    delete(file("release"))
}

// 使用源目录下的 assets 目录路径（直接 File，不需要 Provider 参与快照）
val geoAssetsDir = project.layout.projectDirectory.asFile.resolve("src/main/assets")

val downloadGeoFiles = tasks.register("downloadGeoFiles") {
    group = "assets"
    description = "Download geo database files into generated assets directory"

    val geoFilesUrls = mapOf(
        "https://github.com/MetaCubeX/meta-rules-dat/raw/refs/heads/release/geoip.metadb" to "geoip.metadb",
        "https://github.com/MetaCubeX/meta-rules-dat/raw/refs/heads/release/geosite.dat" to "geosite.dat",
        // "https://github.com/MetaCubeX/meta-rules-dat/raw/refs/heads/release/country.mmdb" to "country.mmdb",
        "https://github.com/MetaCubeX/meta-rules-dat/raw/refs/heads/release/GeoLite2-ASN.mmdb" to "ASN.mmdb",
    )

    // 作为输入 (URLs 列表)，协助 Gradle 判断是否需要重新执行
    inputs.property("geoFiles", geoFilesUrls.keys.sorted())
    // 产物目录
    outputs.dir(geoAssetsDir)

    doLast {
        val client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build()
        val targetDir = geoAssetsDir
            targetDir.mkdirs()

        geoFilesUrls.forEach { (downloadUrl, outputFileName) ->
            val outputPath = targetDir.toPath().resolve(outputFileName)
            val outputFile = outputPath.toFile()
            if (outputFile.exists() && outputFile.length() > 0) {
                println("Skip $outputFileName (already exists, size=${outputFile.length()})")
                return@forEach
            }
            val uri = URI.create(downloadUrl)
            val maxAttempts = 3
            var attempt = 1
            var success = false
            var lastStatus = -1
            while (attempt <= maxAttempts && !success) {
                try {
                    val req = HttpRequest.newBuilder(uri)
                        .GET()
                        .header("Accept", "application/octet-stream")
                        .build()
                    val res = client.send(req, HttpResponse.BodyHandlers.ofByteArray())
                    lastStatus = res.statusCode()
                    if (lastStatus == 200 && res.body().isNotEmpty()) {
                        Files.write(outputPath, res.body())
                        println("Downloaded $outputFileName (attempt $attempt, ${res.body().size} bytes)")
                        success = true
                    } else if (lastStatus in 500..599) {
                        println("Server error $lastStatus for $outputFileName, retrying ($attempt/$maxAttempts)...")
                        Thread.sleep(500L * attempt)
                    } else {
                        throw RuntimeException("Unexpected status $lastStatus (attempt $attempt)")
                    }
                } catch (e: Exception) {
                    if (attempt == maxAttempts) {
                        throw RuntimeException("Failed to download $downloadUrl after $attempt attempts: ${e.message}", e)
                    } else {
                        println("Error on attempt $attempt for $outputFileName: ${e.message}. Retrying...")
                        Thread.sleep(500L * attempt)
                    }
                }
                attempt++
            }
        }
    }
}

// 仅在需要合并 assets 的变体任务执行前下载（更精确）
listOf("mergeAlphaDebugAssets", "mergeMetaDebugAssets").forEach { taskName ->
    tasks.matching { it.name == taskName }.configureEach { dependsOn(downloadGeoFiles) }
}

tasks.getByName("clean", type = Delete::class) {
    delete(geoAssetsDir)
}