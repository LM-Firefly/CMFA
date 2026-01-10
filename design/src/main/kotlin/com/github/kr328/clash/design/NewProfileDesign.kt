package com.github.kr328.clash.design

import android.content.Context
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.github.kr328.clash.design.model.ProfileProvider
import com.github.kr328.clash.design.ui.NewProfileScreen
import com.github.kr328.clash.design.util.insets

class NewProfileDesign(context: Context) : Design<NewProfileDesign.Request>(context) {
    sealed class Request {
        data class Create(val provider: ProfileProvider) : Request()
        data class OpenDetail(val provider: ProfileProvider.External) : Request()
        data class LaunchScanner(val provider: ProfileProvider.QR) : Request()
    }

    private val composeView: androidx.compose.ui.platform.ComposeView =
        androidx.compose.ui.platform.ComposeView(context).apply {
            setBackgroundColor(android.graphics.Color.TRANSPARENT)
        }

    private val insets = context.insets
    private var providers by mutableStateOf<List<ProfileProvider>>(emptyList())

    override val root: View
        get() = composeView

    suspend fun patchProviders(providers: List<ProfileProvider>) {
        this.providers = providers
    }

    init {
        composeView.setContent {
            NewProfileScreen(
                providers = providers,
                insets = insets,
                onSelect = { provider ->
                    if (provider is ProfileProvider.QR) {
                        requests.trySend(Request.LaunchScanner(provider))
                    } else {
                        requests.trySend(Request.Create(provider))
                    }
                    if (provider is ProfileProvider.External) {
                        requests.trySend(Request.OpenDetail(provider))
                    }
                }
            )
        }
    }
}