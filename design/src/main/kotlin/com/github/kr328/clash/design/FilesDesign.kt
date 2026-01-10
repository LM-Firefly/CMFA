package com.github.kr328.clash.design

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import com.github.kr328.clash.design.dialog.FilesPopupDialog
import com.github.kr328.clash.design.dialog.requestModelTextInput
import com.github.kr328.clash.design.model.File
import com.github.kr328.clash.design.ui.FileScreen
import com.github.kr328.clash.design.util.*

class FilesDesign(context: Context) : Design<FilesDesign.Request>(context) {
    sealed class Request {
        data class OpenFile(val file: File) : Request()
        data class OpenDirectory(val file: File) : Request()
        data class RenameFile(val file: File) : Request()
        data class DeleteFile(val file: File) : Request()
        data class ImportFile(val file: File?) : Request()
        data class ExportFile(val file: File) : Request()

        object PopStack : Request()
    }

    private val composeView: ComposeView = ComposeView(context).apply {
        setBackgroundColor(android.graphics.Color.TRANSPARENT)
    }

    private val insets = context.insets
    private var files by mutableStateOf<List<File>>(emptyList())
    private var currentInBaseDir by mutableStateOf(true)

    override val root: View
        get() = composeView

    var configurationEditable: Boolean = false

    suspend fun swapFiles(files: List<File>, currentInBaseDir: Boolean) {
        this.files = files
        this.currentInBaseDir = currentInBaseDir
    }

    fun updateElapsed() {
        // Recomposition is automatic with Compose
    }

    suspend fun requestFileName(name: String): String {
        return context.requestModelTextInput(
            initial = name,
            title = context.getText(R.string.file_name),
            hint = context.getText(R.string.file_name),
            error = context.getText(R.string.invalid_file_name),
            validator = ValidatorFileName,
        )
    }

    init {
        composeView.setContent {
            FileScreen(
                files = files,
                currentInBaseDir = currentInBaseDir,
                configurationEditable = configurationEditable,
                insets = insets,
                onSelectFile = { file ->
                    if (file.isDirectory) {
                        requests.trySend(Request.OpenDirectory(file))
                    } else {
                        requests.trySend(Request.OpenFile(file))
                    }
                },
                onShowMenu = { file ->
                    requestMore(file)
                },
                onCreateNew = {
                    requestNew()
                }
            )
        }
    }

    fun requestRename(dialog: Dialog, file: File) {
        requests.trySend(Request.RenameFile(file))
        dialog.dismiss()
    }

    fun requestImport(dialog: Dialog, file: File) {
        requests.trySend(Request.ImportFile(file))
        dialog.dismiss()
    }

    fun requestExport(dialog: Dialog, file: File) {
        requests.trySend(Request.ExportFile(file))
        dialog.dismiss()
    }

    fun requestDelete(dialog: Dialog, file: File) {
        requests.trySend(Request.DeleteFile(file))
        dialog.dismiss()
    }

    fun requestNew() {
        requests.trySend(Request.ImportFile(null))
    }

    private fun requestMore(file: File) {
        val composeView = ComposeView(context)
        
        val popupWindow = android.widget.PopupWindow(
            composeView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        val dialog = object : Dialog(context) {
            override fun dismiss() {
                popupWindow.dismiss()
            }
        }

        composeView.setContent {
            FilesPopupDialog(
                file = file,
                currentInBase = this.currentInBaseDir,
                configurationEditable = this.configurationEditable,
                onImport = {
                    requestImport(dialog, file)
                },
                onExport = {
                    requestExport(dialog, file)
                },
                onRename = {
                    requestRename(dialog, file)
                },
                onDelete = {
                    requestDelete(dialog, file)
                },
                onDismiss = {
                    popupWindow.dismiss()
                }
            )
        }

        popupWindow.elevation = 0f
        popupWindow.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(Color.TRANSPARENT))

        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        composeView.measure(
            android.view.View.MeasureSpec.makeMeasureSpec(screenWidth, android.view.View.MeasureSpec.AT_MOST),
            android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED)
        )

        val popupWidth = composeView.measuredWidth

        val margin = context.getPixels(com.github.kr328.clash.design.R.dimen.dialog_menu_item_padding)
        val offset = (24 * context.resources.displayMetrics.density).toInt()

        var x = screenWidth - popupWidth - margin + offset
        x = x.coerceAtLeast(margin - offset)
        x = x.coerceAtMost(screenWidth - popupWidth - margin + offset)

        val y = 120

        popupWindow.showAtLocation(root, android.view.Gravity.NO_GRAVITY, x, y)
    }
}
