package com.github.kr328.clash.design

import android.content.Context
import android.view.View
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.github.kr328.clash.core.model.TunnelState
import com.github.kr328.clash.core.util.trafficTotal
import com.github.kr328.clash.design.compose.AboutCard
import com.github.kr328.clash.design.compose.MainScreen
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainDesign(context: Context) : Design<MainDesign.Request>(context) {
    enum class Request {
        ToggleStatus,
        OpenProxy,
        OpenProfiles,
        OpenProviders,
        OpenLogs,
        OpenSettings,
        OpenHelp,
        OpenAbout,
    }

    private var clashRunningState by mutableStateOf(false)
    private var forwardedState by mutableStateOf("0 B")
    private var modeState by mutableStateOf("")
    private var profileNameState by mutableStateOf<String?>(null)
    private var hasProvidersState by mutableStateOf(false)

    override val root: View = ComposeView(context).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
        setContent {
            MaterialTheme {
                MainScreen(
                    clashRunning = clashRunningState,
                    forwarded = forwardedState,
                    mode = modeState,
                    profileName = profileNameState,
                    hasProviders = hasProvidersState,
                    onToggleStatus = { request(Request.ToggleStatus) },
                    onOpenProxy = { request(Request.OpenProxy) },
                    onOpenProfiles = { request(Request.OpenProfiles) },
                    onOpenProviders = { request(Request.OpenProviders) },
                    onOpenLogs = { request(Request.OpenLogs) },
                    onOpenSettings = { request(Request.OpenSettings) },
                    onOpenHelp = { request(Request.OpenHelp) },
                    onOpenAbout = { request(Request.OpenAbout) }
                )
            }
        }
    }

    suspend fun setProfileName(name: String?) {
        withContext(Dispatchers.Main) {
            profileNameState = name
        }
    }

    suspend fun setClashRunning(running: Boolean) {
        withContext(Dispatchers.Main) {
            clashRunningState = running
        }
    }

    suspend fun setForwarded(value: Long) {
        withContext(Dispatchers.Main) {
            forwardedState = value.trafficTotal()
        }
    }

    suspend fun setMode(mode: TunnelState.Mode) {
        withContext(Dispatchers.Main) {
            modeState = when (mode) {
                TunnelState.Mode.Direct -> context.getString(R.string.direct_mode)
                TunnelState.Mode.Global -> context.getString(R.string.global_mode)
                TunnelState.Mode.Rule -> context.getString(R.string.rule_mode)
                TunnelState.Mode.Script -> context.getString(R.string.script_mode)
            }
        }
    }

    suspend fun setHasProviders(has: Boolean) {
        withContext(Dispatchers.Main) {
            hasProvidersState = has
        }
    }

    suspend fun showAbout(versionName: String) {
        withContext(Dispatchers.Main) {
            val content = ComposeView(context).apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
                setContent {
                    MaterialTheme {
                        AboutCard(versionName = versionName)
                    }
                }
            }
            MaterialAlertDialogBuilder(context)
                .setView(content)
                .setBackgroundInsetStart(16)
                .setBackgroundInsetEnd(16)
                .show()
        }
    }

    suspend fun showUpdatedTips() {
        withContext(Dispatchers.Main) {
            MaterialAlertDialogBuilder(context)
                .setTitle(R.string.version_updated)
                .setMessage(R.string.version_updated_tips)
                .setPositiveButton(R.string.ok) { _, _ -> }
                .show()
        }
    }

    fun request(request: Request) {
        requests.trySend(request)
    }

    fun onRequestToggleStatus(view: View) {
        request(Request.ToggleStatus)
    }

    fun onRequestOpenProxy(view: View) {
        request(Request.OpenProxy)
    }

    fun onRequestOpenProfiles(view: View) {
        request(Request.OpenProfiles)
    }

    fun onRequestOpenProviders(view: View) {
        request(Request.OpenProviders)
    }

    fun onRequestOpenLogs(view: View) {
        request(Request.OpenLogs)
    }

    fun onRequestOpenSettings(view: View) {
        request(Request.OpenSettings)
    }

    fun onRequestOpenHelp(view: View) {
        request(Request.OpenHelp)
    }

    fun onRequestOpenAbout(view: View) {
        request(Request.OpenAbout)
    }
}
