package com.github.kr328.clash.design

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.github.kr328.clash.core.model.Provider
import com.github.kr328.clash.design.compose.ProviderItemUi
import com.github.kr328.clash.design.compose.ProvidersScreen
import com.github.kr328.clash.design.util.elapsedIntervalString
import com.github.kr328.clash.design.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class ProvidersDesign(
    context: Context,
    providers: List<Provider>,
) : Design<ProvidersDesign.Request>(context) {
    sealed class Request {
        data class Update(val index: Int, val provider: Provider) : Request()
    }

    private var providersState by mutableStateOf(providers)
    private var updatingIndexesState by mutableStateOf(setOf<Int>())
    private var elapsedTick by mutableLongStateOf(System.currentTimeMillis())

    override val root: View = ComposeView(context).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
        setContent {
            MaterialTheme {
                ProvidersScreen(
                    title = context.getString(R.string.providers),
                    providers = providersState.mapIndexed { index, provider ->
                        val updating = updatingIndexesState.contains(index)
                        val subscription = provider.subscriptionInfo
                        
                        // 计算流量相关信息
                        val hasSubscriptionInfo = provider.hasSubscriptionInfo()
                        val hasExpireInfo = provider.hasExpireInfo()
                        
                        val remainingTraffic = if (hasSubscriptionInfo && subscription != null) {
                            val remaining = subscription.total - (subscription.upload + subscription.download)
                            remaining.coerceAtLeast(0).toBytesString()
                        } else {
                            null
                        }
                        
                        val usedTraffic = if (hasSubscriptionInfo && subscription != null) {
                            val used = subscription.upload + subscription.download
                            val total = subscription.total
                            "${used.toBytesString()} / ${if (total > 0) total.toBytesString() else "Unlimited"}"
                        } else {
                            null
                        }
                        
                        val usagePercentage = if (hasSubscriptionInfo) provider.getUsagePercentage() else null
                        val usagePercentageText = if (hasSubscriptionInfo) provider.getUsagePercentageText() else null
                        
                        val expireInfo = if (hasExpireInfo && subscription != null) {
                            Date(subscription.expire * 1000).format(context, includeDate = true, includeTime = false)
                        } else {
                            null
                        }
                        
                        ProviderItemUi(
                            name = provider.name,
                            type = provider.type(context),
                            elapsedTime = (elapsedTick - provider.updatedAt).coerceAtLeast(0).elapsedIntervalString(context),
                            isUpdating = updating,
                            showUpdateButton = provider.vehicleType != Provider.VehicleType.Inline,
                            remainingTraffic = remainingTraffic,
                            usedTraffic = usedTraffic,
                            usagePercentage = usagePercentage,
                            usagePercentageText = usagePercentageText,
                            expireInfo = expireInfo,
                            hasSubscriptionInfo = hasSubscriptionInfo,
                            hasExpireInfo = hasExpireInfo
                        )
                    },
                    onBackClick = {
                        (context as? AppCompatActivity)?.onBackPressedDispatcher?.onBackPressed()
                    },
                    onUpdateAllClick = { requestUpdateAll() },
                    onProviderUpdateClick = { item ->
                        val index = providersState.indexOfFirst { it.name == item.name && it.type.name == item.type }
                        if (index >= 0) {
                            val provider = providersState[index]
                            if (provider.vehicleType != Provider.VehicleType.Inline && !updatingIndexesState.contains(index)) {
                                updatingIndexesState = updatingIndexesState + index
                                requests.trySend(Request.Update(index, provider))
                            }
                        }
                    }
                )
            }
        }
    }

    fun updateElapsed() {
        elapsedTick = System.currentTimeMillis()
    }

    suspend fun notifyUpdated(index: Int) {
        withContext(Dispatchers.Main) {
            updatingIndexesState = updatingIndexesState - index
        }
    }

    suspend fun notifyChanged(index: Int, provider: Provider) {
        withContext(Dispatchers.Main) {
            providersState = providersState.toMutableList().apply {
                if (index in indices) this[index] = provider
            }
            updatingIndexesState = updatingIndexesState - index
        }
    }

    fun requestUpdateAll() {
        providersState.forEachIndexed { index, provider ->
            if (provider.vehicleType != Provider.VehicleType.Inline && !updatingIndexesState.contains(index)) {
                updatingIndexesState = updatingIndexesState + index
                requests.trySend(Request.Update(index, provider))
            }
        }
    }
}
