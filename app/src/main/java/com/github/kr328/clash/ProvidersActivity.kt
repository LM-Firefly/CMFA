package com.github.kr328.clash

import android.content.Context
import com.github.kr328.clash.common.util.intent
import com.github.kr328.clash.common.util.ticker
import com.github.kr328.clash.core.model.Provider
import com.github.kr328.clash.core.model.SubscriptionInfo
import com.github.kr328.clash.design.ProvidersDesign
import com.github.kr328.clash.design.util.showExceptionToast
import com.github.kr328.clash.service.store.ServiceStore
import com.github.kr328.clash.service.util.importedDir
import com.github.kr328.clash.util.withClash
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.math.BigDecimal
import java.util.concurrent.TimeUnit
import com.github.kr328.clash.design.R

class ProvidersActivity : BaseActivity<ProvidersDesign>() {
    override suspend fun main() {
        val providers = withClash { queryProviders().sorted() }
        val mergedProviders = providers.map { mergeProviderInfo(it) }
        val design = ProvidersDesign(this, mergedProviders)

        setContentDesign(design)

        val ticker = ticker(TimeUnit.MINUTES.toMillis(1))

        while (isActive) {
            select<Unit> {
                events.onReceive {
                    when (it) {
                        Event.ProfileLoaded -> {
                            val newList = withClash { queryProviders().sorted() }

                            if (newList != providers) {
                                startActivity(ProvidersActivity::class.intent)

                                finish()
                            }
                        }
                        else -> Unit
                    }
                }
                design.requests.onReceive {
                    when (it) {
                        is ProvidersDesign.Request.Update -> {
                            launch {
                                try {
                                    withClash {
                                        updateProvider(it.provider.type, it.provider.name)
                                    }

                                    val freshList = withClash { queryProviders() }
                                    val freshProvider = freshList.find { p ->
                                        p.name == it.provider.name && p.type == it.provider.type
                                    } ?: it.provider

                                    val updatedProvider = updateProviderInfo(freshProvider)

                                    val finalProvider = updatedProvider.copy(updatedAt = System.currentTimeMillis())
                                    design.notifyChanged(it.index, finalProvider)
                                } catch (e: Exception) {
                                    design.showExceptionToast(
                                        getString(
                                            R.string.format_update_provider_failure,
                                            it.provider.name,
                                            e.message
                                        )
                                    )

                                    design.notifyUpdated(it.index)
                                }
                            }
                        }
                    }
                }
                if (activityStarted) {
                    ticker.onReceive {
                        design.updateElapsed()
                    }
                }
            }
        }
    }

    private fun mergeProviderInfo(provider: Provider): Provider {
        val stored = getStoredSubscriptionInfo(provider.name) ?: return provider
        return provider.copy(subscriptionInfo = stored)
    }

    private suspend fun updateProviderInfo(provider: Provider): Provider {
        return try {
            val store = ServiceStore(this)
            val active = store.activeProfile ?: return provider

            val profileDir = importedDir.resolve(active.toString())
            val configFile = profileDir.resolve("config.yaml")

            val url = getProviderUrl(configFile, provider.name) ?: return provider

            val versionName = packageManager.getPackageInfo(packageName, 0).versionName
            val userAgent = "ClashMetaForAndroid/$versionName"

            val info = withContext(Dispatchers.IO) {
                fetchSubscriptionInfo(url, userAgent)
            } ?: return provider

            saveStoredSubscriptionInfo(provider.name, info)

            provider.copy(subscriptionInfo = info)
        } catch (e: Exception) {
            provider
        }
    }

    private fun getStoredSubscriptionInfo(name: String): SubscriptionInfo? {
        val prefs = getSharedPreferences("provider_sub_info", Context.MODE_PRIVATE)
        val json = prefs.getString(name, null) ?: return null
        return try {
            Json.decodeFromString<SubscriptionInfo>(json)
        } catch (e: Exception) {
            null
        }
    }

    private fun saveStoredSubscriptionInfo(name: String, info: SubscriptionInfo) {
        val prefs = getSharedPreferences("provider_sub_info", Context.MODE_PRIVATE)
        prefs.edit().putString(name, Json.encodeToString(info)).apply()
    }

    private fun getProviderUrl(profileConfigFile: File, providerName: String): String? {
        if (!profileConfigFile.exists()) return null

        val content = profileConfigFile.readText()

        val lines = content.lines()
        var insideProvider = false
        var currentIndent = 0

        for (i in lines.indices) {
            val line = lines[i]
            val trimLine = line.trim()

            if (trimLine.startsWith("#")) continue

            val isProviderStart = trimLine.startsWith("$providerName:") || trimLine.startsWith("'$providerName':") || trimLine.startsWith("\"$providerName\":")

            if (isProviderStart) {
                insideProvider = true
                currentIndent = line.indexOfFirst { !it.isWhitespace() }
                continue
            }

            if (insideProvider) {
                val indent = line.indexOfFirst { !it.isWhitespace() }
                if (indent != -1 && indent <= currentIndent && trimLine.isNotEmpty()) {
                    return null
                }

                if (trimLine.startsWith("url:")) {
                    return trimLine.substringAfter("url:").substringBefore('#').trim().trim('"', '\'')
                }
            }
        }

        return null
    }

    private fun fetchSubscriptionInfo(url: String, userAgent: String): SubscriptionInfo? {
        try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .head()
                .header("User-Agent", userAgent)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return null
                val header = response.headers["subscription-userinfo"] ?: return null
                return parseSubscriptionInfo(header)
            }
        } catch (e: Exception) {
            return null
        }
    }

    private fun parseSubscriptionInfo(header: String): SubscriptionInfo {
        var upload = 0L
        var download = 0L
        var total = 0L
        var expire = 0L

        val flags = header.split(";")
        for (flag in flags) {
            val info = flag.split("=")
            if (info.size < 2) continue

            try {
                val key = info[0].trim()
                val value = info[1].trim()

                when {
                    key.contains("upload") && value.isNotEmpty() -> {
                        upload = if (value.equals("infinity", ignoreCase = true)) {
                            0L
                        } else {
                            BigDecimal(value.split('.').first()).longValueExact()
                        }
                    }

                    key.contains("download") && value.isNotEmpty() -> {
                        download = if (value.equals("infinity", ignoreCase = true)) {
                            0L
                        } else {
                            BigDecimal(value.split('.').first()).longValueExact()
                        }
                    }

                    key.contains("total") && value.isNotEmpty() -> {
                        total = if (value.equals("infinity", ignoreCase = true)) {
                            0L
                        } else {
                            BigDecimal(value.split('.').first()).longValueExact()
                        }
                    }

                    key.contains("expire") && value.isNotEmpty() -> {
                        expire = if (value.equals("never", ignoreCase = true)) {
                            0L
                        } else {
                            value.toDouble().toLong()
                        }
                    }
                }
            } catch (e: Exception) {
                // Ignore parse error
            }
        }

        return SubscriptionInfo(upload, download, total, expire)
    }
}