package com.github.kr328.clash

import com.github.kr328.clash.common.util.intent
import com.github.kr328.clash.common.util.ticker
import com.github.kr328.clash.core.model.Provider
import com.github.kr328.clash.design.ProvidersDesign
import com.github.kr328.clash.design.util.showExceptionToast
import com.github.kr328.clash.util.withClash
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
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
        return provider
    }

    private suspend fun updateProviderInfo(provider: Provider): Provider {
        // Core provides subscription info directly; no local update needed
        return provider
    }
}