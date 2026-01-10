package com.github.kr328.clash.design

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.getSystemService
import com.github.kr328.clash.core.model.LogMessage
import com.github.kr328.clash.design.ui.LogcatScreen
import com.github.kr328.clash.design.ui.ToastDuration
import com.github.kr328.clash.design.util.*
import kotlinx.coroutines.launch

class LogcatDesign(
    context: Context,
    private val streaming: Boolean,
) : Design<LogcatDesign.Request>(context) {
    enum class Request {
        Close, Delete, Export
    }

    private val composeView: androidx.compose.ui.platform.ComposeView =
        androidx.compose.ui.platform.ComposeView(context).apply {
            setBackgroundColor(android.graphics.Color.TRANSPARENT)
        }

    private val insets = context.insets
    private var messages by mutableStateOf<List<LogMessage>>(emptyList())

    fun request(req: Request) {
        requests.trySend(req)
    }

    suspend fun patchMessages(messages: List<LogMessage>, removed: Int, appended: Int) {
        this.messages = messages
    }

    override val root: View
        get() = composeView

    init {
        composeView.setContent {
            LogcatScreen(
                messages = messages,
                isStreaming = streaming,
                insets = insets,
                onDelete = { request(Request.Delete) },
                onExport = { request(Request.Export) },
                onClose = { request(Request.Close) },
                onLogMessageCopy = { message ->
                    launch {
                        val data = ClipData.newPlainText("log_message", message.message)
                        context.getSystemService<ClipboardManager>()?.setPrimaryClip(data)
                        showToast(R.string.copied, ToastDuration.Short)
                    }
                }
            )
        }
    }
}