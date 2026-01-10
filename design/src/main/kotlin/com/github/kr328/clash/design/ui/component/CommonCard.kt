package com.github.kr328.clash.design.ui.component

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap

/**
 * 通用卡片组件，用于统一所有列表项的样式
 */
@Composable
fun CommonCard(
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    horizontalPadding: Dp = 17.5.dp,
    verticalPadding: Dp = 4.dp,
    cornerRadius: Dp = 12.dp,
    elevation: Dp = 2.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        shape = RoundedCornerShape(cornerRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            content = content
        )
    }
}

/**
 * 带图标和文本的行组件
 */
@Composable
fun IconTextRow(
    icon: Any?, // Drawable or ImageVector
    iconSize: Dp = 40.dp,
    iconTint: Color? = null,
    title: String,
    subtitle: String? = null,
    titleStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyLarge,
    subtitleStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium,
    subtitleColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    trailing: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        when (icon) {
            is Drawable -> {
                Image(
                    bitmap = icon.toBitmap().asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(iconSize)
                )
            }
            is ImageVector -> {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize),
                    tint = iconTint ?: MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            null -> {
                Spacer(modifier = Modifier.size(iconSize))
            }
        }

        if (icon != null) {
            Spacer(modifier = Modifier.width(16.dp))
        }

        // Text content
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = titleStyle,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            subtitle?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it,
                    style = subtitleStyle,
                    color = subtitleColor
                )
            }
        }

        // Trailing content
        trailing?.invoke()
    }
}

/**
 * 进度条组件（带颜色状态）
 */
@Composable
fun UsageProgressBar(
    percentage: Float,
    modifier: Modifier = Modifier,
    height: Dp = 8.dp
) {
    val progressColor = when {
        percentage >= 90 -> Color(0xFFE53935) // Red
        percentage >= 70 -> Color(0xFFFFC107) // Yellow
        else -> Color(0xFF4CAF50) // Green
    }

    LinearProgressIndicator(
        progress = { percentage / 200f },
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        color = progressColor,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
        strokeCap = StrokeCap.Round
    )
}

/**
 * 信息行组件（左右对齐的文本）
 */
@Composable
fun InfoRow(
    leftText: String,
    rightText: String,
    leftColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    rightColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.labelSmall,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = leftText,
            style = style,
            color = leftColor
        )
        Text(
            text = rightText,
            style = style,
            color = rightColor
        )
    }
}

/**
 * 标题行组件（带可选的尾部按钮）
 */
@Composable
fun TitleRow(
    title: String,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingContent?.invoke()
        
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        trailingContent?.invoke()
    }
}
