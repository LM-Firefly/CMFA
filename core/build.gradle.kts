import java.util.Properties
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.Optional
import org.gradle.api.provider.Property
import org.gradle.process.ExecOperations
import org.gradle.workers.WorkerExecutor
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters
import org.gradle.api.Action
import javax.inject.Inject

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
}

// Worker 参数接口（用于并行执行 Go 构建）
interface GoSharedLibWorkParameters : WorkParameters {
    val nativeDirPath: Property<String>
    val soFilePath: Property<String>
    val goArch: Property<String>
    val goArm: Property<String>
    val buildTags: Property<String>
    val ccPath: Property<String>
}

// Worker 执行体（实际执行 Go 构建的隔离进程）
abstract class GoSharedLibWorkAction : WorkAction<GoSharedLibWorkParameters> {
    @get:Inject
    abstract val execOps: ExecOperations

    override fun execute() {
        val params = parameters
        val outSo = File(params.soFilePath.get())
        val outDir = outSo.parentFile
        if (!outDir.exists()) outDir.mkdirs()

        execOps.exec {
            workingDir = File(params.nativeDirPath.get())
            executable = "go"
            args(
                "build", "-buildmode=c-shared",
                "-ldflags", "-extldflags=-Wl,-z,max-page-size=16384",
                "-tags", params.buildTags.get(),
                "-trimpath", "-buildvcs=false",
                "-o", outSo.absolutePath,
                "."
            )
            environment("GOOS", "android")
            environment("GOARCH", params.goArch.get())
            if (params.goArm.isPresent && params.goArm.get().isNotBlank()) {
                environment("GOARM", params.goArm.get())
            }
            environment("CGO_ENABLED", "1")
            environment("CC", params.ccPath.get())
        }
    }
}

// 可缓存的 Go 构建任务（使用 Worker API 并行执行）
@CacheableTask
abstract class GoSharedLibTask : DefaultTask() {
    @get:Inject
    abstract val workerExecutor: WorkerExecutor

    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val nativeDir: DirectoryProperty

    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val moduleRoot: DirectoryProperty

    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val upstreamDir: DirectoryProperty

    @get:OutputFile
    abstract val soFile: RegularFileProperty

    @get:OutputFile
    abstract val headerFile: RegularFileProperty

    @get:Input
    abstract val goArch: Property<String>

    @get:Input
    @get:Optional
    abstract val goArm: Property<String>

    @get:Input
    abstract val apiLevel: Property<Int>

    @get:Input
    abstract val buildTags: Property<String>

    @get:Input
    abstract val ccPath: Property<String>

    @TaskAction
    fun build() {
        val workQueue = workerExecutor.noIsolation()
        workQueue.submit(GoSharedLibWorkAction::class.java, object : Action<GoSharedLibWorkParameters> {
            override fun execute(params: GoSharedLibWorkParameters) {
                params.nativeDirPath.set(nativeDir.get().asFile.absolutePath)
                params.soFilePath.set(soFile.get().asFile.absolutePath)
                params.goArch.set(goArch.get())
                params.goArm.set(goArm.getOrElse(""))
                params.buildTags.set(buildTags.get())
                params.ccPath.set(ccPath.get())
            }
        })
    }
}

// 简单的复制任务（可缓存），暴露 DirectoryProperty 以配合 AGP 的 generated sources API
@CacheableTask
abstract class CopyGoLibsTask : DefaultTask() {
    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val inputFile: RegularFileProperty

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @get:Input
    abstract val abi: Property<String>

    @TaskAction
    fun run() {
        val inFile = inputFile.get().asFile
        val root = outputDir.get().asFile
        if (!root.exists()) root.mkdirs()
        val abiDir = File(root, abi.get())
        if (!abiDir.exists()) abiDir.mkdirs()
        inFile.copyTo(File(abiDir, inFile.name), overwrite = true)
    }
}

// Go 源码与输出目录（替代原插件）
// goNativeDir 为导出 C 符号的包装层（生成 libclash.so 与 libclash.h）
// goFossDir 为被 replace 的上游 mihomo 子模块
val goNativeDir = file("src/main/golang/native")
val goModuleRoot = file("src/main/golang")
val goFossDir = file("src/foss/golang")
val goOutputBase = layout.buildDirectory.dir("go-output")

// ABI 映射与任务名后缀
val abiMap = mapOf(
    "arm64-v8a" to ("Arm64V8a" to Triple("android", "arm64", null)),
    "armeabi-v7a" to ("ArmeabiV7a" to Triple("android", "arm", "7")),
    "x86" to ("X86" to Triple("android", "386", null)),
    "x86_64" to ("X8664" to Triple("android", "amd64", null)),
)

fun ndkRootFromLocalProperties(): String? {
    val local = rootProject.file("local.properties")
    if (!local.exists()) return null
    val p = Properties()
    local.inputStream().use { p.load(it) }
    val sdkDir = p.getProperty("sdk.dir") ?: return null
    // 读取当前模块的 ndkVersion（在根脚本里已统一设置）
    val ndkVersion = extensions.findByName("android")
        ?.let { it as? com.android.build.api.dsl.LibraryExtension }
        ?.ndkVersion
    return if (ndkVersion.isNullOrBlank()) null else "$sdkDir/ndk/$ndkVersion"
}

fun hostTag(): String {
    val os = System.getProperty("os.name").lowercase()
    val arch = System.getProperty("os.arch").lowercase()
    return when {
        os.contains("windows") -> "windows-x86_64"
        os.contains("mac") && (arch.contains("aarch64") || arch.contains("arm64")) -> "darwin-arm64"
        os.contains("mac") -> "darwin-x86_64"
        else -> "linux-x86_64"
    }
}

fun clangPrefixForAbi(abi: String): String = when (abi) {
    "arm64-v8a" -> "aarch64-linux-android"
    "armeabi-v7a" -> "armv7a-linux-androideabi"
    "x86" -> "i686-linux-android"
    "x86_64" -> "x86_64-linux-android"
    else -> error("Unknown ABI $abi")
}

android {
    productFlavors {
        all {
            externalNativeBuild {
                cmake {
                    // 传递 GO_SOURCE（可选，仅保持与旧逻辑一致）与 GO_OUTPUT 基础目录，
                    // CMake 会在其中追加 FLAVOR/BuildType
                    arguments("-DGO_SOURCE:STRING=${goNativeDir}")
                    // 使用 Provider 传递路径，避免配置期触盘
                    arguments("-DGO_OUTPUT:STRING=${goOutputBase.get().asFile.absolutePath}")
                    arguments("-DFLAVOR_NAME:STRING=$name")
                }
            }
        }
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
        }
    }
}

dependencies {
    implementation(project(":common"))

    implementation(libs.androidx.core)
    implementation(libs.kotlin.coroutine)
    implementation(libs.kotlin.serialization.json)
}

androidComponents.onVariants { variant ->
    val cmakeName = if (variant.buildType == "debug") "Debug" else "RelWithDebInfo"
    val buildTypeSuffix = if (variant.buildType == "debug") "Debug" else "Release"
    // 取第一个 flavor（项目仅有 feature 维度）
    val flavorName = variant.productFlavors.firstOrNull()?.second ?: ""

    // 可配置项（默认回退）：go 构建标签与 API Level
    val goTags = providers.gradleProperty("goTags").orElse("foss with_gvisor cmfa").get()
    val goApiLevel = providers.gradleProperty("goApiLevel").orElse("23").map { it.toInt() }.get()

    // 为每个 ABI 注册与旧插件同名的 Go 构建任务，并接到 CMake 之前
    abiMap.forEach { (abi, pair) ->
        val (goAbi, triple) = pair
        val (_, goArch, goArm) = triple

        val variantCap = variant.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        val taskName = "externalGolangBuild${variantCap}${goAbi}"

        // 使用 flavor+buildType 格式以匹配 CMake 期望：<build>/go-output/<flavor><BuildType>/<abi>
        val outDirProvider = goOutputBase.map { base ->
            File(base.asFile, "${flavorName}${buildTypeSuffix}/$abi")
        }

        val soFileProvider = outDirProvider.map { it.resolve("libclash.so") }
        val headerFileProvider = outDirProvider.map { it.resolve("libclash.h") }

        // 注册自定义可缓存任务（替代 Exec）
        val t = tasks.register(taskName, GoSharedLibTask::class.java) {
            group = "go-build"
            description = "Build Go shared library for $abi ($variantCap)"

            nativeDir.set(layout.projectDirectory.dir(goNativeDir.path))
            moduleRoot.set(layout.projectDirectory.dir(goModuleRoot.path))
            upstreamDir.set(layout.projectDirectory.dir(goFossDir.path))

            soFile.set(layout.file(soFileProvider))
            headerFile.set(layout.file(headerFileProvider))

            this.goArch.set(goArch)
            if (goArm != null) this.goArm.set(goArm) else this.goArm.set("")
            this.apiLevel.set(goApiLevel)
            this.buildTags.set(goTags)

            // 延迟计算 ccPath（配置期不触盘）
            this.ccPath.set(providers.provider {
                val ndkRoot = providers.environmentVariable("ANDROID_NDK_ROOT").orNull
                    ?: ndkRootFromLocalProperties()
                    ?: error("Cannot locate ANDROID_NDK_ROOT nor local.properties sdk.dir + ndkVersion")
                File(ndkRoot,
                    "toolchains/llvm/prebuilt/${hostTag()}/bin/${clangPrefixForAbi(abi)}${goApiLevel}-clang"
                ).absolutePath
            })
        }

        // 精确绑定 CMake 任务（避免 configureEach 遍历）
        // CMake 任务命名格式：buildCMake[BuildType][abi]（不含 flavor）
        val cmakeTaskName = "buildCMake$cmakeName[$abi]"
        val cmakeConfigTaskName = "configureCMake$cmakeName[$abi]"
        tasks.matching { it.name.startsWith(cmakeTaskName) }.configureEach {
            dependsOn(t)
            logger.info("[core] Wire CMake build task $name -> ${t.name}")
        }
        // 确保 CMake 配置任务也依赖 Go 构建（生成头文件）
        tasks.matching { it.name.startsWith(cmakeConfigTaskName) }.configureEach {
            dependsOn(t)
            logger.info("[core] Wire CMake configure task $name -> ${t.name}")
        }

        // 将生成的 libclash.so 注入到变体的 jniLibs，以便 AAR / APK 打包
        // 复制到 build/generated/jniLibs/<variant>/<abi>/libclash.so
        // 变体根 jniLibs 目录，内部按 ABI 再分层
        val jniRootDirProvider = layout.buildDirectory.dir("generated/jniLibs/${variant.name}")
        val copyTask = tasks.register("copyGoLib${variantCap}${goAbi}", CopyGoLibsTask::class.java) {
            dependsOn(t)
            val soFileProvider = outDirProvider.map { it.resolve("libclash.so") }
            inputFile.set(layout.file(soFileProvider))
            outputDir.set(jniRootDirProvider)
            this.abi.set(abi)
        }

        // 将该目录作为此变体的 jniLibs 源目录 (AGP Variant API)
        variant.sources.jniLibs?.addGeneratedSourceDirectory(copyTask) { it.outputDir }
    }
}