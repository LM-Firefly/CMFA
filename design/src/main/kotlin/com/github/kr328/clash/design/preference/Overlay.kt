package com.github.kr328.clash.design.preference

import android.content.Context
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.RecyclerView
import com.github.kr328.clash.design.dialog.FullScreenDialog
import com.github.kr328.clash.design.dialog.PreferenceListDialog
import com.github.kr328.clash.design.util.applyLinearAdapter
import com.github.kr328.clash.design.util.layoutInflater
import com.github.kr328.clash.design.util.root
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

internal enum class EditableListOverlayResult {
    Cancel, Apply, Reset
}

internal suspend fun requestEditableListOverlay(
    context: Context,
    adapter: RecyclerView.Adapter<*>,
    title: CharSequence,
    addNewItem: suspend () -> Unit
): EditableListOverlayResult {
    return coroutineScope {
        suspendCancellableCoroutine { ctx ->
            val dialog = FullScreenDialog(context)

            val recyclerView = RecyclerView(context).apply {
                applyLinearAdapter(context, adapter)
            }

            val composeView = ComposeView(context).apply {
                setContent {
                    PreferenceListDialog(
                        title = title.toString(),
                        recyclerView = recyclerView,
                        insets = dialog.surface.insets,
                        onNew = {
                            launch {
                                addNewItem()
                            }
                        },
                        onReset = {
                            ctx.resume(EditableListOverlayResult.Reset)
                            dialog.dismiss()
                        },
                        onCancel = {
                            dialog.dismiss()
                        },
                        onOk = {
                            ctx.resume(EditableListOverlayResult.Apply)
                            dialog.dismiss()
                        }
                    )
                }
            }

            dialog.setContentView(composeView)

            dialog.setOnDismissListener {
                if (!ctx.isCompleted) {
                    ctx.resume(EditableListOverlayResult.Cancel)
                }
            }

            ctx.invokeOnCancellation {
                dialog.dismiss()
            }

            dialog.show()
        }
    }
}