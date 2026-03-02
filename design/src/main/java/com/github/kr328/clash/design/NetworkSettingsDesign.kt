package com.github.kr328.clash.design

import android.content.Context
import android.os.Build
import android.view.View
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.github.kr328.clash.design.compose.NetworkSettingsScreen
import com.github.kr328.clash.design.compose.SimpleAlertDialog
import com.github.kr328.clash.design.store.UiStore
import com.github.kr328.clash.service.model.AccessControlMode
import com.github.kr328.clash.service.store.ServiceStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NetworkSettingsDesign(
    context: Context,
    private val uiStore: UiStore,
    private val serviceStore: ServiceStore,
    private val clashRunning: Boolean
) : Design<NetworkSettingsDesign.Request>(context) {
    enum class Request {
        StartAccessControlList
    }

    private val enableVpnState = mutableStateOf(uiStore.enableVpn)
    private val bypassPrivateNetworkState = mutableStateOf(serviceStore.bypassPrivateNetwork)
    private val dnsHijackingState = mutableStateOf(serviceStore.dnsHijacking)
    private val allowBypassState = mutableStateOf(serviceStore.allowBypass)
    private val allowIpv6State = mutableStateOf(serviceStore.allowIpv6)
    private val systemProxyState = mutableStateOf(serviceStore.systemProxy)
    private val tunStackModeState = mutableStateOf(serviceStore.tunStackMode)
    private val accessControlModeState = mutableStateOf(serviceStore.accessControlMode)
    private val showAccessControlModeDialogState = mutableStateOf(false)
    private val showTunStackModeDialogState = mutableStateOf(false)

    override val root: View = ComposeView(context).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
        setContent {
            MaterialTheme {
                if (showAccessControlModeDialogState.value) {
                    SimpleAlertDialog(
                        title = context.getString(R.string.access_control_mode),
                        items = AccessControlMode.values().toList(),
                        selectedItem = accessControlModeState.value,
                        itemLabel = { mode ->
                            when (mode) {
                                AccessControlMode.AcceptAll -> context.getString(R.string.allow_all_apps)
                                AccessControlMode.AcceptSelected -> context.getString(R.string.allow_selected_apps)
                                AccessControlMode.DenySelected -> context.getString(R.string.deny_selected_apps)
                            }
                        },
                        onItemSelected = { selected ->
                            accessControlModeState.value = selected
                            launch(Dispatchers.Default) { serviceStore.accessControlMode = selected }
                        },
                        onDismiss = { showAccessControlModeDialogState.value = false }
                    )
                }

                if (showTunStackModeDialogState.value) {
                    val allModes = listOf("system", "gvisor", "mixed")
                    SimpleAlertDialog(
                        title = context.getString(R.string.tun_stack_mode),
                        items = allModes,
                        selectedItem = tunStackModeState.value,
                        itemLabel = { mode -> stackModeText(mode, context) },
                        onItemSelected = { selected ->
                            tunStackModeState.value = selected
                            launch(Dispatchers.Default) { serviceStore.tunStackMode = selected }
                        },
                        onDismiss = { showTunStackModeDialogState.value = false }
                    )
                }

                NetworkSettingsScreen(
                    title = context.getString(R.string.network),
                    enableVpn = enableVpnState.value,
                    onEnableVpnChange = { enabled ->
                        if (clashRunning) return@NetworkSettingsScreen
                        enableVpnState.value = enabled
                        launch(Dispatchers.Default) { uiStore.enableVpn = enabled }
                    },
                    bypassPrivateNetwork = bypassPrivateNetworkState.value,
                    onBypassPrivateNetworkChange = { enabled ->
                        if (!enableVpnState.value || clashRunning) return@NetworkSettingsScreen
                        bypassPrivateNetworkState.value = enabled
                        launch(Dispatchers.Default) { serviceStore.bypassPrivateNetwork = enabled }
                    },
                    dnsHijacking = dnsHijackingState.value,
                    onDnsHijackingChange = { enabled ->
                        if (!enableVpnState.value || clashRunning) return@NetworkSettingsScreen
                        dnsHijackingState.value = enabled
                        launch(Dispatchers.Default) { serviceStore.dnsHijacking = enabled }
                    },
                    allowBypass = allowBypassState.value,
                    onAllowBypassChange = { enabled ->
                        if (!enableVpnState.value || clashRunning) return@NetworkSettingsScreen
                        allowBypassState.value = enabled
                        launch(Dispatchers.Default) { serviceStore.allowBypass = enabled }
                    },
                    allowIpv6 = allowIpv6State.value,
                    onAllowIpv6Change = { enabled ->
                        if (!enableVpnState.value || clashRunning) return@NetworkSettingsScreen
                        allowIpv6State.value = enabled
                        launch(Dispatchers.Default) { serviceStore.allowIpv6 = enabled }
                    },
                    systemProxy = systemProxyState.value,
                    onSystemProxyChange = { enabled ->
                        if (!enableVpnState.value || clashRunning) return@NetworkSettingsScreen
                        systemProxyState.value = enabled
                        launch(Dispatchers.Default) { serviceStore.systemProxy = enabled }
                    },
                    systemProxyEnabled = Build.VERSION.SDK_INT >= 29,
                    stackMode = stackModeText(tunStackModeState.value, context),
                    onStackModeClick = {
                        if (!enableVpnState.value || clashRunning) return@NetworkSettingsScreen
                        showTunStackModeDialogState.value = true
                    },
                    accessControlMode = when (accessControlModeState.value) {
                        AccessControlMode.AcceptAll -> context.getString(R.string.allow_all_apps)
                        AccessControlMode.AcceptSelected -> context.getString(R.string.allow_selected_apps)
                        AccessControlMode.DenySelected -> context.getString(R.string.deny_selected_apps)
                    },
                    onAccessControlModeClick = {
                        if (!enableVpnState.value || clashRunning) return@NetworkSettingsScreen
                        showAccessControlModeDialogState.value = true
                    },
                    accessControlListClick = {
                        if (!enableVpnState.value || clashRunning) return@NetworkSettingsScreen
                        launch { requests.send(Request.StartAccessControlList) }
                    },
                    onBackClick = { (context as? android.app.Activity)?.finish() }
                )
            }
        }
    }

    private fun stackModeText(mode: String, context: Context): String {
        return when (mode) {
            "system" -> context.getString(R.string.tun_stack_system)
            "gvisor" -> context.getString(R.string.tun_stack_gvisor)
            "mixed" -> context.getString(R.string.tun_stack_mixed)
            else -> mode
        }
    }
}
