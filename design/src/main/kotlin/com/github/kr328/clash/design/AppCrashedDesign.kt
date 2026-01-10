package com.github.kr328.clash.design

import android.content.Context
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import com.github.kr328.clash.design.ui.AppCrashedScreen
import com.github.kr328.clash.design.util.insets

class AppCrashedDesign(context: Context) : Design<Unit>(context) {
    private val composeView: ComposeView = ComposeView(context).apply {
        setBackgroundColor(android.graphics.Color.TRANSPARENT)
    }

    private val insets = context.insets
    private var logs by mutableStateOf("")

    override val root: View
        get() = composeView

    fun setAppLogs(logs: String) {
        this.logs = logs
    }

    init {
        composeView.setContent {
            AppCrashedScreen(
                logs = logs,
                insets = insets
            )
        }
    }
}
