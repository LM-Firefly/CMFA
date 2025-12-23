package com.github.kr328.clash.design

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.github.kr328.clash.design.adapter.FileAdapter
import com.github.kr328.clash.design.databinding.DesignFilesBinding
import com.github.kr328.clash.design.databinding.DialogFilesMenuBinding
import com.github.kr328.clash.design.databinding.DialogFilesPopupBinding
import com.github.kr328.clash.design.dialog.AppBottomSheetDialog
import com.github.kr328.clash.design.dialog.requestModelTextInput
import com.github.kr328.clash.design.model.File
import com.github.kr328.clash.design.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

    private val binding = DesignFilesBinding
        .inflate(context.layoutInflater, context.root, false)
    private val adapter: FileAdapter = FileAdapter(context, this::requestOpen, this::requestMore)

    override val root: View
        get() = binding.root

    var configurationEditable: Boolean
        get() = binding.configurationEditable
        set(value) {
            binding.configurationEditable = value
        }

    suspend fun swapFiles(files: List<File>, currentInBaseDir: Boolean) {
        withContext(Dispatchers.Main) {
            adapter.swapDataSet(adapter::files, files)
            binding.currentInBaseDir = currentInBaseDir
        }
    }

    fun updateElapsed() {
        adapter.updateElapsed()
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
        binding.self = this

        binding.activityBarLayout.applyFrom(context)

        binding.mainList.recyclerList.also {
            it.applyLinearAdapter(context, adapter)
            it.bindAppBarElevation(binding.activityBarLayout)
        }
    }

    private fun requestOpen(file: File) {
        if (file.isDirectory) {
            requests.trySend(Request.OpenDirectory(file))
        } else {
            requests.trySend(Request.OpenFile(file))
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

    private fun requestMore(anchor: View, file: File) {
        val popupView = DialogFilesPopupBinding
            .inflate(context.layoutInflater, null, false)

        popupView.master = this
        popupView.file = file
        popupView.currentInBase = this.binding.currentInBaseDir
        popupView.configurationEditable = this.binding.configurationEditable

        val popupWindow = android.widget.PopupWindow(
            popupView.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupView.self = object : Dialog(context) {
            override fun dismiss() {
                popupWindow.dismiss()
            }
        }

        popupWindow.elevation = 8f
        popupWindow.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT))

        // Measure popup and position it similar to ProfilesDesign
        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        popupView.root.measure(
            android.view.View.MeasureSpec.makeMeasureSpec(screenWidth, android.view.View.MeasureSpec.AT_MOST),
            android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED)
        )

        val popupWidth = popupView.root.measuredWidth

        val anchorLocation = IntArray(2)
        anchor.getLocationOnScreen(anchorLocation)
        val anchorRight = anchorLocation[0] + anchor.width
        val anchorBottom = anchorLocation[1] + anchor.height

        val margin = context.getPixels(com.github.kr328.clash.design.R.dimen.dialog_menu_item_padding)

        var x = anchorRight - popupWidth
        x = x.coerceAtLeast(margin)
        x = x.coerceAtMost(screenWidth - popupWidth - margin)

        val y = anchorBottom

        popupWindow.showAtLocation(binding.root, android.view.Gravity.NO_GRAVITY, x, y)
    }
}
