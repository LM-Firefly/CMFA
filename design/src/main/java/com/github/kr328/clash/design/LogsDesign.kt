package com.github.kr328.clash.design

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.github.kr328.clash.design.compose.LogItemUi
import com.github.kr328.clash.design.compose.LogLevel
import com.github.kr328.clash.design.compose.LogsScreen
import com.github.kr328.clash.design.model.LogFile
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class LogsDesign(context: Context) : Design<LogsDesign.Request>(context) {
    sealed class Request {
        object StartLogcat : Request()
        object DeleteAll : Request()

        data class OpenFile(val file: LogFile) : Request()
    }

    private var logsState by mutableStateOf<List<LogFile>>(emptyList())

    override val root: View = ComposeView(context).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
        setContent {
            MaterialTheme {
                LogsScreen(
                    title = context.getString(R.string.logs),
                    logMessages = logsState.map {
                        LogItemUi(
                            level = LogLevel.INFO,
                            time = it.date.time,
                            message = it.fileName,
                            key = it.fileName
                        )
                    },
                    onBackClick = {
                        (context as? AppCompatActivity)?.onBackPressedDispatcher?.onBackPressed()
                    },
                    onClearAllClick = { requests.trySend(Request.DeleteAll) },
                    onStartLogcatClick = { requests.trySend(Request.StartLogcat) },
                    onLogClick = { item ->
                        logsState.firstOrNull { it.fileName == item.key }?.let {
                            requests.trySend(Request.OpenFile(it))
                        }
                    }
                )
            }
        }
    }

    fun request(req: Request) {
        requests.trySend(req)
    }

    suspend fun patchLogs(logs: List<LogFile>) {
        withContext(Dispatchers.Main) {
            logsState = logs
        }
    }

    suspend fun requestDeleteAll(): Boolean {
        return withContext(Dispatchers.Main) {
            suspendCancellableCoroutine { ctx ->
                MaterialAlertDialogBuilder(context)
                    .setTitle(R.string.delete_all_logs)
                    .setMessage(R.string.delete_all_logs_warn)
                    .setPositiveButton(R.string.ok) { _, _ -> ctx.resume(true) }
                    .setNegativeButton(R.string.cancel) { _, _ -> }
                    .show()
                    .setOnDismissListener { if (!ctx.isCompleted) ctx.resume(false) }
            }
        }
    }

}
