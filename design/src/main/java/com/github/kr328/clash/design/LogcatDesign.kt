package com.github.kr328.clash.design

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.getSystemService
import com.github.kr328.clash.core.model.LogMessage
import com.github.kr328.clash.design.compose.LogItemUi
import com.github.kr328.clash.design.compose.LogLevel
import com.github.kr328.clash.design.compose.LogcatScreen
import com.github.kr328.clash.design.ui.ToastDuration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LogcatDesign(
    context: Context,
    private val streaming: Boolean,
) : Design<LogcatDesign.Request>(context) {
    enum class Request {
        Close, Delete, Export
    }

    private var logItems by mutableStateOf<List<LogItemUi>>(emptyList())
    private var updateTick by mutableIntStateOf(0)

    override val root: View = ComposeView(context).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
        setContent {
            MaterialTheme {
                LogcatScreen(
                    title = context.getString(R.string.logcat),
                    streaming = streaming,
                    logs = logItems,
                    updateTick = updateTick,
                    onBackClick = {
                        (context as? AppCompatActivity)?.onBackPressedDispatcher?.onBackPressed()
                    },
                    onDeleteClick = { request(Request.Delete) },
                    onExportClick = { request(Request.Export) },
                    onCloseStreamClick = { request(Request.Close) },
                    onMessageLongClick = { item ->
                        copyMessage(item.message)
                    }
                )
            }
        }
    }

    fun request(req: Request) {
        requests.trySend(req)
    }

    suspend fun patchMessages(messages: List<LogMessage>, removed: Int, appended: Int) {
        withContext(Dispatchers.Main) {
            logItems = messages.map {
                LogItemUi(
                    level = it.level.toUiLevel(),
                    time = it.time.time,
                    message = it.message,
                    key = "${it.time.time}:${it.message.hashCode()}"
                )
            }

            if (removed > 0 || appended > 0) {
                updateTick += 1
            }
        }
    }

    private fun copyMessage(message: String) {
        launch(Dispatchers.Main) {
            val data = ClipData.newPlainText("log_message", message)
            context.getSystemService<ClipboardManager>()?.setPrimaryClip(data)
            showToast(R.string.copied, ToastDuration.Short)
        }
    }

    private fun LogMessage.Level.toUiLevel(): LogLevel {
        return when (this) {
            LogMessage.Level.Debug -> LogLevel.DEBUG
            LogMessage.Level.Info -> LogLevel.INFO
            LogMessage.Level.Warning -> LogLevel.WARNING
            LogMessage.Level.Error -> LogLevel.ERROR
            LogMessage.Level.Silent -> LogLevel.SILENT
            LogMessage.Level.Unknown -> LogLevel.INFO
        }
    }
}
