package com.github.kr328.clash.design

import android.app.Dialog
import android.content.Context
import android.view.View
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.github.kr328.clash.core.model.TunnelState
import com.github.kr328.clash.core.util.trafficTotal
import com.github.kr328.clash.design.dialog.AboutDialog
import com.github.kr328.clash.design.ui.MainScreen
import com.github.kr328.clash.design.util.layoutInflater
import com.github.kr328.clash.design.util.resolveThemedColor
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

    private var profileNameState by mutableStateOf<String?>(null)
    private var clashRunningState by mutableStateOf(false)
    private var forwardedState by mutableStateOf("")
    private var modeState by mutableStateOf("")
    private var hasProvidersState by mutableStateOf(false)

    private val colorClashStarted = Color(context.resolveThemedColor(androidx.appcompat.R.attr.colorPrimary))
    private val colorClashStopped = Color(context.resolveThemedColor(R.attr.colorClashStopped))

    override val root: View = ComposeView(context).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            MainScreen(
                clashRunning = clashRunningState,
                forwarded = forwardedState,
                mode = modeState,
                profileName = profileNameState,
                hasProviders = hasProvidersState,
                colorClashStarted = colorClashStarted,
                colorClashStopped = colorClashStopped,
                onRequest = { request(it) }
            )
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
                else -> context.getString(R.string.rule_mode)
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
            val composeView = ComposeView(context).apply {
                setContent {
                    AboutDialog(versionName = versionName)
                }
            }

            Dialog(context).apply {
                setContentView(composeView)
                window?.apply {
                    setLayout(
                        (context.resources.displayMetrics.widthPixels * 0.9).toInt(),
                        android.view.ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    setBackgroundDrawableResource(android.R.color.transparent)
                }
                show()
            }
        }
    }

    fun request(request: Request) {
        requests.trySend(request)
    }
}
