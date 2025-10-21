package com.github.kr328.clash.design

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.github.kr328.clash.design.adapter.LogFileAdapter
import com.github.kr328.clash.design.databinding.DesignLogsBinding
import com.github.kr328.clash.design.model.LogFile
import com.github.kr328.clash.design.util.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
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

    private val binding = DesignLogsBinding
        .inflate(context.layoutInflater, context.root, false)
    private val adapter = LogFileAdapter(context) {
        @Suppress("DEPRECATION")
        requests.trySend(Request.OpenFile(it))
    }

    override val root: View
        get() = binding.root

    suspend fun patchLogs(logs: List<LogFile>) {
        adapter.patchDataSet(adapter::logs, logs, false, LogFile::fileName)
    }

    suspend fun requestDeleteAll(): Boolean {
        return withContext(Dispatchers.Main) {
            suspendCancellableCoroutine { ctx ->
                val dialog = MaterialAlertDialogBuilder(context)
                    .setTitle(R.string.delete_all_logs)
                    .setMessage(R.string.delete_all_logs_warn)
                    .setPositiveButton(R.string.ok) { _, _ -> ctx.resume(true) }
                    .setNegativeButton(R.string.cancel) { _, _ -> }
                    .show()
                val radius = context.getPixels(R.dimen.large_action_card_radius).toFloat()
                applyDialogCornerRadius(dialog, radius)
                dialog.setOnDismissListener { if (!ctx.isCompleted) ctx.resume(false) }
            }
        }
    }

    init {
        binding.self = this

        binding.activityBarLayout.applyFrom(context)

        binding.recyclerList.applyLinearAdapter(context, adapter)
        val radius = context.getPixels(R.dimen.large_action_card_radius).toFloat()
        binding.deleteView.applyRoundedSelectableBackground(radius)
    }

    private fun applyDialogCornerRadius(dialog: AlertDialog, radius: Float) {
        val window = dialog.window ?: return
        val decor = window.decorView
        val background = decor.background
        if (background is MaterialShapeDrawable) {
            background.shapeAppearanceModel = background.shapeAppearanceModel.toBuilder()
                .setAllCornerSizes(radius)
                .build()
            return
        }
        if (background is GradientDrawable) {
            background.cornerRadius = radius
            return
        }
        val fallbackColor = context.resolveThemedColor(com.google.android.material.R.attr.colorSurface)
        val shape = MaterialShapeDrawable(
            ShapeAppearanceModel.builder()
                .setAllCornerSizes(radius)
                .build()
        ).apply {
            fillColor = ColorStateList.valueOf(fallbackColor)
        }
        window.setBackgroundDrawable(shape)
    }
}