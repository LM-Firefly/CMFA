package com.github.kr328.clash.design

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.github.kr328.clash.design.compose.NewProfileScreen
import com.github.kr328.clash.design.compose.ProfileProviderItemUi
import com.github.kr328.clash.design.model.ProfileProvider

class NewProfileDesign(context: Context) : Design<NewProfileDesign.Request>(context) {
    sealed class Request {
        data class Create(val provider: ProfileProvider) : Request()
        data class OpenDetail(val provider: ProfileProvider.External) : Request()
        object LaunchScanner : Request()
    }

    private var providers by mutableStateOf<List<ProfileProvider>>(emptyList())

    override val root: View = ComposeView(context).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
        setContent {
            MaterialTheme {
                val providerItems = providers.map {
                    ProfileProviderItemUi(
                        key = it.key(),
                        icon = it.icon,
                        name = it.name,
                        summary = it.summary,
                        supportLongClick = it is ProfileProvider.External,
                    )
                }

                NewProfileScreen(
                    title = context.getString(R.string.new_profile),
                    providers = providerItems,
                    onBackClick = {
                        (context as? AppCompatActivity)?.onBackPressedDispatcher?.onBackPressed()
                    },
                    onProviderClick = { item ->
                        providers.firstOrNull { it.key() == item.key }?.let { requestCreate(it) }
                    },
                    onProviderLongClick = { item ->
                        providers.firstOrNull { it.key() == item.key }?.let { requestDetail(it) }
                    }
                )
            }
        }
    }

    fun patchProviders(providers: List<ProfileProvider>) {
        this.providers = providers
    }

    private fun requestCreate(provider: ProfileProvider) {
        if (provider is ProfileProvider.QR) {
            requests.trySend(Request.LaunchScanner)
        } else {
            requests.trySend(Request.Create(provider))
        }
    }

    private fun requestDetail(provider: ProfileProvider) {
        if (provider !is ProfileProvider.External) return

        requests.trySend(Request.OpenDetail(provider))
    }

    private fun ProfileProvider.key(): String {
        return when (this) {
            is ProfileProvider.File -> "file"
            is ProfileProvider.Url -> "url"
            is ProfileProvider.QR -> "qr"
            is ProfileProvider.External -> "external:${intent.component?.flattenToShortString() ?: name}"
        }
    }
}
