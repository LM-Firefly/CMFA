package com.github.kr328.clash.design.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Compose版本的文本输入对话框
 * 
 * 这是 dialog_text_field.xml 的Kotlin Compose重写版本
 * 
 * @param title 对话框标题
 * @param initialValue 初始文本值
 * @param hint 输入提示文字
 * @param error 错误消息（如果有）
 * @param validator 输入验证器，返回true表示输入有效
 * @param onDismiss 对话框关闭回调
 * @param onConfirm 确认按钮回调，传入用户输入的文本
 * @param onReset 重置按钮回调（可选），如果不为null则显示重置按钮
 */
@Composable
fun TextInputDialog(
    title: String,
    initialValue: String = "",
    hint: String? = null,
    error: String? = null,
    validator: (String) -> Boolean = { true },
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    onReset: (() -> Unit)? = null
) {
    var text by remember { mutableStateOf(initialValue) }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // 验证输入
    LaunchedEffect(text) {
        if (text.isNotEmpty()) {
            isError = !validator(text)
            errorMessage = if (isError) error else null
        } else {
            isError = false
            errorMessage = null
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    label = hint?.let { { Text(it) } },
                    isError = isError,
                    supportingText = errorMessage?.let { 
                        { Text(it, color = MaterialTheme.colorScheme.error) }
                    },
                    singleLine = false
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (!isError) {
                        onConfirm(text)
                    }
                },
                enabled = !isError
            ) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        },
        // 如果提供了重置回调，添加第三个按钮
        // 注意：AlertDialog的Material3实现不直接支持第三个按钮
        // 需要自定义实现或使用其他方式显示重置按钮
    )
}

/**
 * 带重置按钮的文本输入对话框变体
 */
@Composable
fun TextInputDialogWithReset(
    title: String,
    initialValue: String = "",
    hint: String? = null,
    error: String? = null,
    resetLabel: String = "重置",
    validator: (String) -> Boolean = { true },
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    onReset: () -> Unit
) {
    var text by remember { mutableStateOf(initialValue) }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(text) {
        if (text.isNotEmpty()) {
            isError = !validator(text)
            errorMessage = if (isError) error else null
        } else {
            isError = false
            errorMessage = null
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(16.dp),
        title = { Text(title, style = MaterialTheme.typography.titleLarge) },
        text = {
            Column {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    label = hint?.let { { Text(it) } },
                    isError = isError,
                    supportingText = errorMessage?.let { 
                        { Text(it, color = MaterialTheme.colorScheme.error) }
                    },
                    singleLine = false
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (!isError) onConfirm(text) },
                enabled = !isError
            ) {
                Text("确定")
            }
        },
        dismissButton = {
            Column {
                TextButton(onClick = onReset) {
                    Text(resetLabel)
                }
                TextButton(onClick = onDismiss) {
                    Text("取消")
                }
            }
        }
    )
}

// ==================== 扩展函数用于桥接现有代码 ====================

/**
 * Compose风格的挂起函数，用于显示文本输入对话框变体
 * 
 * 使用示例：
 * ```kotlin
 * val result = showTextInputDialog(
 *     title = "输入名称",
 *     initialValue = "默认值",
 *     hint = "请输入名称"

 * )
 * if (result != null) {
 *     // 用户确认并输入了文本
 * }
 * ```
 */
suspend fun androidx.compose.ui.platform.ComposeView.showTextInputDialog(
    title: String,
    initialValue: String = "",
    hint: String? = null,
    error: String? = null,
    validator: (String) -> Boolean = { true }
): String? = kotlinx.coroutines.suspendCancellableCoroutine { continuation ->
    setContent {
        TextInputDialog(
            title = title,
            initialValue = initialValue,
            hint = hint,
            error = error,
            validator = validator,
            onDismiss = {
                if (continuation.isActive) {
                    continuation.resume(null) { _, _, _ -> }
                }
            },
            onConfirm = { text ->
                if (continuation.isActive) {
                    continuation.resume(text) { _, _, _ -> }
                }
            }
        )
    }
}
