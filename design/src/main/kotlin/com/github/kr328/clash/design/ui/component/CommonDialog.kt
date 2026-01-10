package com.github.kr328.clash.design.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/**
 * 通用全屏对话框
 */
@Composable
fun FullScreenDialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(
        dismissOnBackPress = true,
        dismissOnClickOutside = false,
        usePlatformDefaultWidth = false
    ),
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            content()
        }
    }
}

/**
 * 通用标准对话框
 */
@Composable
fun StandardDialog(
    title: String,
    onDismissRequest: () -> Unit,
    confirmText: String = "确定",
    dismissText: String = "取消",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit = onDismissRequest,
    neutralText: String? = null,
    onNeutral: (() -> Unit)? = null,
    content: @Composable (ColumnScope.() -> Unit)
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = title) },
        text = { Column(content = content) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(confirmText)
            }
        },
        dismissButton = {
            Row {
                neutralText?.let { text ->
                    onNeutral?.let { action ->
                        TextButton(onClick = action) {
                            Text(text)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
                TextButton(onClick = onDismiss) {
                    Text(dismissText)
                }
            }
        }
    )
}

/**
 * 菜单对话框基类
 */
@Composable
fun MenuDialog(
    title: String? = null,
    onDismissRequest: () -> Unit,
    items: @Composable (ColumnScope.() -> Unit)
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = title?.let { { Text(text = it) } },
        text = {
            Column {
                items()
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}

/**
 * 菜单项
 */
@Composable
fun MenuItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
