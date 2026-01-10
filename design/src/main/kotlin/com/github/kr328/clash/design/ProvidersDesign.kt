package com.github.kr328.clash.design

import android.content.Context
import android.view.View
import androidx.compose.runtime.mutableStateOf
import com.github.kr328.clash.core.model.Provider
import com.github.kr328.clash.design.ui.ProvidersScreen
import com.github.kr328.clash.design.util.insets

class ProvidersDesign(
    context: Context,
    providers: List<Provider>,
) : Design<ProvidersDesign.Request>(context) {
    sealed class Request {
        data class Update(val index: Int, val provider: Provider) : Request()
    }

    private val composeView: androidx.compose.ui.platform.ComposeView =
        androidx.compose.ui.platform.ComposeView(context).apply {
            setBackgroundColor(android.graphics.Color.TRANSPARENT)
        }

    private val insets = context.insets
    private val providersState = mutableStateOf(providers)
    
    override val root: View
        get() = composeView

    fun updateElapsed() {
        // Compose handles recomposition automatically
    }

    suspend fun notifyUpdated(index: Int) {
        // Compose handles recomposition automatically
    }

    suspend fun notifyChanged(index: Int, provider: Provider) {
        val currentProviders = providersState.value.toMutableList()
        if (index in currentProviders.indices) {
            currentProviders[index] = provider
            providersState.value = currentProviders
        }
    }

    init {
        composeView.setContent {
            ProvidersScreen(
                providers = providersState.value,
                insets = insets,
                onUpdateProvider = { provider ->
                    val index = providersState.value.indexOf(provider)
                    if (index >= 0) {
                        requests.trySend(Request.Update(index, provider))
                    }
                }
            )
        }
    }

    fun requestUpdateAll() {
        // Handle provider updates
    }
}