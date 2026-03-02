package com.github.kr328.clash.design

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.github.kr328.clash.design.compose.SettingsScreen

class SettingsDesign(context: Context) : Design<SettingsDesign.Request>(context) {
    enum class Request {
        StartApp, StartNetwork, StartOverride, StartMetaFeature,
    }

    override val root: View = ComposeView(context).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
        setContent {
            MaterialTheme {
                SettingsScreen(
                    title = context.getString(R.string.settings),
                    appTitle = context.getString(R.string.app),
                    networkTitle = context.getString(R.string.network),
                    overrideTitle = context.getString(R.string.override),
                    metaFeatureTitle = context.getString(R.string.meta_features),
                    onBackClick = {
                        (context as? AppCompatActivity)?.onBackPressedDispatcher?.onBackPressed()
                    },
                    onStartApp = { request(Request.StartApp) },
                    onStartNetwork = { request(Request.StartNetwork) },
                    onStartOverride = { request(Request.StartOverride) },
                    onStartMetaFeature = { request(Request.StartMetaFeature) }
                )
            }
        }
    }

    fun request(request: Request) {
        requests.trySend(request)
    }
}
