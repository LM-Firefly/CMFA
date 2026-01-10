package com.github.kr328.clash.design.component

import android.content.Context
import android.view.View
import androidx.compose.ui.platform.ComposeView
import com.github.kr328.clash.core.model.ProxySort
import com.github.kr328.clash.core.model.TunnelState
import com.github.kr328.clash.design.ProxyDesign
import com.github.kr328.clash.design.dialog.AppBottomSheetDialog
import com.github.kr328.clash.design.dialog.ProxyMenuDialog
import com.github.kr328.clash.design.store.UiStore
import kotlinx.coroutines.channels.Channel

class ProxyMenu(
    private val context: Context,
    view: View,
    private val mode: TunnelState.Mode?,
    private val uiStore: UiStore,
    private val requests: Channel<ProxyDesign.Request>,
    private val updateConfig: () -> Unit,
) {
    fun show() {
        val composeView = ComposeView(context)
        val dialog = AppBottomSheetDialog(context)

        composeView.setContent {
            ProxyMenuDialog(
                mode = mode,
                proxyLine = uiStore.proxyLine,
                proxySort = uiStore.proxySort,
                filterEnabled = uiStore.proxyExcludeNotSelectable,
                onModeChanged = { newMode ->
                    if (newMode != mode) {
                        requests.trySend(ProxyDesign.Request.PatchMode(newMode))
                        dialog.dismiss()
                    }
                },
                onProxyLineChanged = { lines ->
                    if (uiStore.proxyLine != lines) {
                        uiStore.proxyLine = lines
                        updateConfig()
                        requests.trySend(ProxyDesign.Request.ReloadAll)
                        dialog.dismiss()
                    }
                },
                onProxySortChanged = { sort ->
                    if (uiStore.proxySort != sort) {
                        uiStore.proxySort = sort
                        requests.trySend(ProxyDesign.Request.ReloadAll)
                        dialog.dismiss()
                    }
                },
                onFilterChanged = { isChecked ->
                    uiStore.proxyExcludeNotSelectable = isChecked
                    requests.trySend(ProxyDesign.Request.ReLaunch)
                }
            )
        }

        dialog.setContentView(composeView)
        dialog.show()
    }
}