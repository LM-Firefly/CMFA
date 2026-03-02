package com.github.kr328.clash.design.dialog

import android.content.Context
import com.github.kr328.clash.design.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun <T> Context.requestModelListInput(
    items: List<T>,
    title: CharSequence,
    itemLabel: (T) -> CharSequence,
): T? {
    if (items.isEmpty()) return null

    return suspendCancellableCoroutine { continuation ->
        var selected: T? = null
        val labels = items.map { itemLabel(it).toString() }.toTypedArray()

        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setItems(labels) { _, which ->
                selected = items[which]
                if (!continuation.isCompleted) {
                    continuation.resume(selected)
                }
            }
            .setNegativeButton(R.string.cancel) { _, _ ->
                if (!continuation.isCompleted) {
                    continuation.resume(null)
                }
            }
            .setOnDismissListener {
                if (!continuation.isCompleted) {
                    continuation.resume(null)
                }
            }
            .create()

        continuation.invokeOnCancellation {
            dialog.dismiss()
        }

        dialog.show()
    }
}
