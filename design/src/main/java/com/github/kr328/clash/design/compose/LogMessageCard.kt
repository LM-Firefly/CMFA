package com.github.kr328.clash.design.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

/**
 * 日志消息列表项组件
 * 
 * 用于显示日志消息，包含日志级别、时间和内容
 * 
 * 替代: adapter_log_message.xml
 */

/**
 * 日志级别枚举
 */
enum class LogLevel {
    DEBUG,
    INFO,
    WARNING,
    ERROR,
    SILENT
}

/**
 * 日志消息卡片
 * 
 * @param level 日志级别
 * @param time 日志时间戳（毫秒）
 * @param message 日志内容
 * @param onClick 点击回调（可选）
 */
@Composable
fun LogMessageCard(
    level: LogLevel,
    time: Long,
    message: String,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val timeFormatter = remember {
        SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    }
    
    val levelColor = when (level) {
        LogLevel.DEBUG -> MaterialTheme.colorScheme.primary
        LogLevel.INFO -> MaterialTheme.colorScheme.primary
        LogLevel.WARNING -> MaterialTheme.colorScheme.tertiary
        LogLevel.ERROR -> MaterialTheme.colorScheme.error
        LogLevel.SILENT -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .then(
                if (onClick != null || onLongClick != null) {
                    Modifier.combinedClickable(
                        onClick = { onClick?.invoke() },
                        onLongClick = { onLongClick?.invoke() }
                    )
                } else {
                    Modifier
                }
            ),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 顶部：级别和时间
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = level.name,
                    style = MaterialTheme.typography.labelSmall,
                    color = levelColor
                )
                
                Text(
                    text = timeFormatter.format(Date(time)),
                    style = MaterialTheme.typography.labelSmall,
                    color = levelColor
                )
            }
            
            // 消息内容
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

/**
 * 简化的日志消息项（用于列表显示）
 * 
 * @param level 日志级别
 * @param time 日志时间戳（毫秒）
 * @param message 日志内容
 * @param onClick 点击回调
 */
@Composable
fun CompactLogMessageItem(
    level: LogLevel,
    time: Long,
    message: String,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val timeFormatter = remember {
        SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    }
    
    val levelColor = when (level) {
        LogLevel.DEBUG -> MaterialTheme.colorScheme.primary
        LogLevel.INFO -> MaterialTheme.colorScheme.primary
        LogLevel.WARNING -> MaterialTheme.colorScheme.tertiary
        LogLevel.ERROR -> MaterialTheme.colorScheme.error
        LogLevel.SILENT -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null || onLongClick != null) {
                    Modifier.combinedClickable(
                        onClick = { onClick?.invoke() },
                        onLongClick = { onLongClick?.invoke() }
                    )
                } else {
                    Modifier
                }
            ),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 级别标签
            Text(
                text = level.name,
                style = MaterialTheme.typography.labelSmall,
                color = levelColor,
                modifier = Modifier.width(50.dp)
            )
            
            // 时间
            Text(
                text = timeFormatter.format(Date(time)),
                style = MaterialTheme.typography.labelSmall,
                color = levelColor,
                modifier = Modifier.width(70.dp)
            )
            
            // 消息
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.weight(1f),
                maxLines = 1
            )
        }
    }
}
