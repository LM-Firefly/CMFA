package com.github.kr328.clash.design

import android.content.Context
import android.view.View
import androidx.compose.ui.platform.ComposeView
import com.github.kr328.clash.design.preference.category
import com.github.kr328.clash.design.preference.clickable
import com.github.kr328.clash.design.preference.preferenceScreen
import com.github.kr328.clash.design.preference.tips

class ApkBrokenDesign(context: Context) : Design<ApkBrokenDesign.Request>(context) {
    data class Request(val url: String)

    private val composeView: ComposeView = ComposeView(context).apply {
        setBackgroundColor(android.graphics.Color.TRANSPARENT)
    }

    override val root: View
        get() = composeView

    init {

        val screen = preferenceScreen(context) {
            tips(R.string.application_broken_tips)

            category(R.string.reinstall)

            clickable(
                title = R.string.github_releases,
                summary = R.string.meta_github_url
            ) {
                clicked {
                    requests.trySend(Request(context.getString(R.string.meta_github_url)))
                }
            }
        }

        composeView.addView(screen.root)
    }
}