package com.github.kr328.clash

import android.app.Application
import android.content.Context
import com.github.kr328.clash.common.Global
import com.github.kr328.clash.common.compat.currentProcessName
import com.github.kr328.clash.common.log.Log
import com.github.kr328.clash.remote.Remote
import com.github.kr328.clash.service.util.sendServiceRecreated
import com.github.kr328.clash.util.clashDir
import java.io.File
import java.io.FileOutputStream
import org.tukaani.xz.XZInputStream

@Suppress("unused")
class MainApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        Global.init(this)
    }

    override fun onCreate() {
        super.onCreate()

        val processName = currentProcessName
        extractGeoFiles()

        Log.d("Process $processName started")

        if (processName == packageName) {
            Remote.launch()
        } else {
            sendServiceRecreated()
        }
    }

    private fun extractGeoFiles() {
        clashDir.mkdirs()

        val updateDate = packageManager.getPackageInfo(packageName, 0).lastUpdateTime
        // xz 压缩的 geo 文件列表
        val xzGeoFiles = listOf(
            "geoip.metadb" to "geoip.metadb",
            "GeoSite.dat" to "GeoSite.dat",
            "ASN.mmdb" to "ASN.mmdb",
        )
        for ((assetName, outputName) in xzGeoFiles) {
            val target = File(clashDir, outputName)
            // 兼容旧版本小写文件名
            if (outputName == "GeoSite.dat") {
                val legacy = File(clashDir, "geosite.dat")
                if (!target.exists() && legacy.exists()) legacy.renameTo(target)
            }
            if (target.exists() && target.lastModified() < updateDate) {
                target.delete()
            }
            if (!target.exists()) {
                XZInputStream(assets.open("$assetName.xz").buffered()).use { xz ->
                    FileOutputStream(target).use { out ->
                        xz.copyTo(out)
                    }
                }
            }
        }
        // BundleMRS.7z 未做 xz 压缩（本身已是 7z 格式）
        val bundleMRSFile = File(clashDir, "BundleMRS.7z")
        if (bundleMRSFile.exists() && bundleMRSFile.lastModified() < updateDate) {
            bundleMRSFile.delete()
        }
        if (!bundleMRSFile.exists()) {
            FileOutputStream(bundleMRSFile).use {
                assets.open("BundleMRS.7z").copyTo(it)
            }
        }
    }

    fun finalize() {
        Global.destroy()
    }
}
