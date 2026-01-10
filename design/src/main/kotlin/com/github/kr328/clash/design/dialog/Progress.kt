package com.github.kr328.clash.design.dialog

import android.content.Context
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ModelProgressBarConfigure {
    var isIndeterminate: Boolean
    var text: String?
    var progress: Int
    var max: Int
}

interface ModelProgressBarScope {
    suspend fun configure(block: suspend ModelProgressBarConfigure.() -> Unit)
}

suspend fun Context.withModelProgressBar(block: suspend ModelProgressBarScope.() -> Unit) {
    val progressState = mutableIntStateOf(0)
    val maxState = mutableIntStateOf(100)
    val isIndeterminateState = mutableStateOf(true)
    val textState = mutableStateOf<String?>(null)

    val composeView = ComposeView(this).apply {
        setBackgroundColor(android.graphics.Color.TRANSPARENT)
        setContent {
            ProgressDialog(
                progress = progressState.intValue,
                maxProgress = maxState.intValue,
                isIndeterminate = isIndeterminateState.value,
                text = textState.value
            )
        }
    }

    val dialog = MaterialAlertDialogBuilder(this)
        .setCancelable(false)
        .setView(composeView)
        .show()

    val configureImpl = object : ModelProgressBarConfigure {
        override var isIndeterminate: Boolean
            get() = isIndeterminateState.value
            set(value) {
                isIndeterminateState.value = value
            }
        override var text: String?
            get() = textState.value
            set(value) {
                textState.value = value
            }
        override var progress: Int
            get() = progressState.intValue
            set(value) {
                progressState.intValue = value
            }
        override var max: Int
            get() = maxState.intValue
            set(value) {
                maxState.intValue = value
            }
    }

    val scopeImpl = object : ModelProgressBarScope {
        override suspend fun configure(block: suspend ModelProgressBarConfigure.() -> Unit) {
            withContext(Dispatchers.Main) {
                configureImpl.block()
            }
        }
    }

    try {
        scopeImpl.block()
    } finally {
        dialog.dismiss()
    }
}