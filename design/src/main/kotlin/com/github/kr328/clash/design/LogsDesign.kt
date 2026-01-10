package com.github.kr328.clash.design

import android.content.Context
import android.view.View
import androidx.compose.ui.platform.ComposeView
import com.github.kr328.clash.design.adapter.LogFileAdapter
import com.github.kr328.clash.design.model.LogFile
import com.github.kr328.clash.design.ui.LogsScreen
import com.github.kr328.clash.design.util.*
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

    private val composeView = ComposeView(context)
    private val logsList = mutableListOf<LogFile>()

    override val root: View
        get() = composeView

    fun request(req: Request) {
        requests.trySend(req)
    }

    suspend fun patchLogs(logs: List<LogFile>) {
        withContext(Dispatchers.Main) {
            logsList.clear()
            logsList.addAll(logs)
            updateComposeView()
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

    init {
        updateComposeView()
    }

    private fun updateComposeView() {
        composeView.setContent {
            LogsScreen(
                logs = logsList.toList(),
                onStartLogcat = {
                    request(Request.StartLogcat)
                },
                onDeleteAll = {
                    request(Request.DeleteAll)
                },
                onOpenFile = { file ->
                    request(Request.OpenFile(file))
                }
            )
        }
    }
}