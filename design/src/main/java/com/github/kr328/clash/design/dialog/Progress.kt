package com.github.kr328.clash.design.dialog

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
    // 使用 Compose 实现进度对话框
    var isIndeterminate by mutableStateOf(true)
    var text by mutableStateOf<String?>(null)
    var progress by mutableStateOf(0)
    var max by mutableStateOf(100)
    var showDialog by mutableStateOf(true)

    val composeView = ComposeView(this).apply {
        setContent {
            if (showDialog) {
                Dialog(
                    onDismissRequest = { },
                    properties = DialogProperties(
                        dismissOnBackPress = false,
                        dismissOnClickOutside = false
                    )
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 6.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                                .widthIn(min = 280.dp)
                                .heightIn(min = 75.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (isIndeterminate) {
                                LinearProgressIndicator(
                                    modifier = Modifier.fillMaxWidth()
                                )
                            } else {
                                val animatedProgress by animateFloatAsState(
                                    targetValue = if (max > 0) progress.toFloat() / max.toFloat() else 0f,
                                    label = "progress"
                                )
                                LinearProgressIndicator(
                                    progress = { animatedProgress },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            text?.let {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(top = 5.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    val configureImpl = object : ModelProgressBarConfigure {
        override var isIndeterminate: Boolean
            get() = isIndeterminate
            set(value) {
                isIndeterminate = value
            }

        override var text: String?
            get() = text
            set(value) {
                text = value
            }

        override var progress: Int
            get() = progress
            set(value) {
                progress = value
            }

        override var max: Int
            get() = max
            set(value) {
                max = value
            }
    }

    val scopeImpl = object : ModelProgressBarScope {
        override suspend fun configure(block: suspend ModelProgressBarConfigure.() -> Unit) {
            withContext(Dispatchers.Main) {
                configureImpl.block()
            }
        }
    }

    // 创建一个不可见的对话框来承载 ComposeView
    val dialog = MaterialAlertDialogBuilder(this)
        .setCancelable(false)
        .setView(composeView)
        .show()
    
    // 隐藏对话框的背景和边框
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

    try {
        scopeImpl.block()
    } finally {
        withContext(Dispatchers.Main) {
            showDialog = false
            dialog.dismiss()
        }
    }
}
