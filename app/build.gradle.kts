import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
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
    implementation(libs.quickie.bundled)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.kotlin.serialization.json)
    implementation(platform(libs.okhttp.bom))
    implementation("com.squareup.okhttp3:okhttp")
    implementation(platform(libs.compose.bom))
    implementation("androidx.compose.runtime:runtime")
}

tasks.named<Delete>("clean") {
    delete(file("release"))
}

// 改为将文件下载到构建目录下的 generated 目录，避免修改源码树引发 Gradle 隐式依赖问题
val geoAssetsDirProvider = layout.buildDirectory.dir("generated/geoAssets")
val downloadGeoFiles = tasks.register("downloadGeoFiles") {
    group = "assets"
    description = "Download geo database files into generated assets directory"
    // 注意：GeoSite 在核心 Go 代码中使用文件名 "GeoSite.dat" (首字母大写 S)。
    // 之前这里下载为小写 geosite.dat，导致核心启动时认为缺失再次试图联网下载。
    // 统一改为保存为 "GeoSite.dat"，避免运行时再次下载。
    val geoFilesUrls = mapOf(
        "https://github.com/MetaCubeX/meta-rules-dat/raw/refs/heads/release/geoip.metadb" to "geoip.metadb",
        "https://github.com/MetaCubeX/meta-rules-dat/raw/refs/heads/release/geosite.dat" to "GeoSite.dat",
        // 如需国家库可放开："https://github.com/MetaCubeX/meta-rules-dat/raw/refs/heads/release/country.mmdb" to "Country.mmdb",
        "https://github.com/MetaCubeX/meta-rules-dat/raw/refs/heads/release/GeoLite2-ASN.mmdb" to "ASN.mmdb",
    )
    // 作为输入 (URLs 列表)，协助 Gradle 判断是否需要重新执行
    inputs.property("geoFiles", geoFilesUrls.keys.sorted())
    // 产物目录（使用 Provider 真实路径）
    val outDir = geoAssetsDirProvider.get().asFile
    outputs.dir(outDir)
    doLast {
        val client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build()
        val targetDir = outDir
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
// 使用新的 Variant API 将生成目录加入 assets
androidComponents {
    onVariants { variant ->
        val taskProvider = downloadGeoFiles
        variant.sources.assets?.addGeneratedSourceDirectory(
            taskProvider,
            TaskBasedDirectoryProperty(taskProvider, geoAssetsDirProvider)
        )
    }
}

// TaskBasedDirectoryProperty 包装器
class TaskBasedDirectoryProperty(
    private val task: TaskProvider<*>,
    private val dirProvider: Provider<Directory>
) : (Task) -> DirectoryProperty {
    override fun invoke(t: Task): DirectoryProperty {
        return t.project.objects.directoryProperty().apply {
            set(dirProvider)
        }
    }
}

// 所有合并 assets 任务依赖下载
tasks.matching { it.name.startsWith("merge") && it.name.endsWith("Assets") }.configureEach { dependsOn(downloadGeoFiles) }
// Lint / lintVital 各阶段任务（analyze / reportModel / report / vital）都可能读取 assets
tasks.configureEach {
    if (name.startsWith("lint", ignoreCase = true) ||
        (name.contains("Lint", ignoreCase = true) && (
            name.contains("Analyze") ||
            name.contains("ReportModel") ||
            name.contains("Report") ||
            name.contains("Vital")
        ))
    ) {
        dependsOn(downloadGeoFiles)
    }
}
tasks.named<Delete>("clean") {
    delete(geoAssetsDirProvider)
}