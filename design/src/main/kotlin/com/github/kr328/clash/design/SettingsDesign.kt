package com.github.kr328.clash.design

import android.content.Context
import android.view.View
import androidx.compose.ui.platform.ComposeView
import com.github.kr328.clash.design.ui.SettingsScreen
import com.github.kr328.clash.design.util.insets

class SettingsDesign(context: Context) : Design<SettingsDesign.Request>(context) {
    enum class Request {
        StartApp, StartNetwork, StartOverride, StartMetaFeature,
    }

    private val composeView: ComposeView = ComposeView(context).apply {
        setBackgroundColor(android.graphics.Color.TRANSPARENT)
    }

    private val insets = context.insets

    override val root: View
        get() = composeView

    init {
        composeView.setContent {
            SettingsScreen(
                insets = insets,
                onStartApp = { request(Request.StartApp) },
                onStartNetwork = { request(Request.StartNetwork) },
                onStartOverride = { request(Request.StartOverride) },
                onStartMetaFeature = { request(Request.StartMetaFeature) }
            )
        }
    }

    fun request(request: Request) {
        requests.trySend(request)
    }

    fun onRequestStartApp(view: View) {
        request(Request.StartApp)
    }

    fun onRequestStartNetwork(view: View) {
        request(Request.StartNetwork)
    }

    fun onRequestStartOverride(view: View) {
        request(Request.StartOverride)
    }

    fun onRequestStartMetaFeature(view: View) {
        request(Request.StartMetaFeature)
    }
}
