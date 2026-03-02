package com.github.kr328.clash.design

import android.content.Context
import android.view.View
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.github.kr328.clash.design.compose.AppSettingsScreen
import com.github.kr328.clash.design.compose.SimpleAlertDialog
import com.github.kr328.clash.design.model.Behavior
import com.github.kr328.clash.design.model.DarkMode
import com.github.kr328.clash.design.store.UiStore
import com.github.kr328.clash.service.store.ServiceStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppSettingsDesign(
    context: Context,
    private val uiStore: UiStore,
    private val srvStore: ServiceStore,
    private val behavior: Behavior,
    private val running: Boolean,
    private val onHideIconChange: (hide: Boolean) -> Unit
) : Design<AppSettingsDesign.Request>(context) {
    enum class Request {
        ReCreateAllActivities
    }

    private val autoRestartState = mutableStateOf(behavior.autoRestart)
    private val darkModeState = mutableStateOf(uiStore.darkMode)
    private val hideAppIconState = mutableStateOf(uiStore.hideAppIcon)
    private val hideFromRecentsState = mutableStateOf(uiStore.hideFromRecents)
    private val dynamicNotificationState = mutableStateOf(srvStore.dynamicNotification)
    private val showDarkModeDialogState = mutableStateOf(false)

    override val root: View = ComposeView(context).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
        setContent {
            MaterialTheme {
                if (showDarkModeDialogState.value) {
                    SimpleAlertDialog(
                        title = "Dark Mode",
                        items = DarkMode.values().toList(),
                        selectedItem = darkModeState.value,
                        itemLabel = { darkMode ->
                            when (darkMode) {
                                DarkMode.Auto -> context.getString(R.string.follow_system_android_10)
                                DarkMode.ForceLight -> context.getString(R.string.always_light)
                                DarkMode.ForceDark -> context.getString(R.string.always_dark)
                            }
                        },
                        onItemSelected = { selected ->
                            darkModeState.value = selected
                            launch(Dispatchers.Default) { uiStore.darkMode = selected }
                        },
                        onDismiss = { showDarkModeDialogState.value = false }
                    )
                }

                AppSettingsScreen(
                    title = context.getString(R.string.app),
                    autoRestart = autoRestartState.value,
                    onAutoRestartChange = { enabled ->
                        autoRestartState.value = enabled
                        launch {
                            behavior.autoRestart = enabled
                            requests.send(Request.ReCreateAllActivities)
                        }
                    },
                    darkMode = when (darkModeState.value) {
                        DarkMode.Auto -> context.getString(R.string.follow_system_android_10)
                        DarkMode.ForceLight -> context.getString(R.string.always_light)
                        DarkMode.ForceDark -> context.getString(R.string.always_dark)
                    },
                    onDarkModeClick = { showDarkModeDialogState.value = true },
                    hideAppIcon = hideAppIconState.value,
                    onHideAppIconChange = { hide ->
                        hideAppIconState.value = hide
                        launch(Dispatchers.Default) {
                            uiStore.hideAppIcon = hide
                            onHideIconChange(hide)
                        }
                    },
                    hideFromRecents = hideFromRecentsState.value,
                    onHideFromRecentsChange = { hide ->
                        hideFromRecentsState.value = hide
                        launch(Dispatchers.Default) {
                            uiStore.hideFromRecents = hide
                            requests.send(Request.ReCreateAllActivities)
                        }
                    },
                    dynamicNotification = dynamicNotificationState.value,
                    onDynamicNotificationChange = { enabled ->
                        if (running) return@AppSettingsScreen
                        dynamicNotificationState.value = enabled
                        launch(Dispatchers.Default) { srvStore.dynamicNotification = enabled }
                    },
                    onBackClick = { (context as? android.app.Activity)?.finish() }
                )
            }
        }
    }
}
