package com.github.kr328.clash.design.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.core.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.graphics.graphicsLayer
import kotlin.math.max

/**
 * Compose版本的代理卡片组
 * 
 * 对应 adapter_proxy.xml 的Kotlin重写
 * 
 * 调整为3行布局：
 * 第1行：节点名（支持自动滚动）
 * 第2行：策略类型 + 延迟值
 * 第3行：终末节点（策略组专用）
 */
@Composable
fun ProxyCard(
    title: String,
    subtitle: String,
    delay: String?,
    endNode: String? = null,
    isSelected: Boolean,
    isPinned: Boolean = false,
    proxyColumns: Int = 1,
    backgroundColor: androidx.compose.ui.graphics.Color,
    contentColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val columns = proxyColumns.coerceIn(1, 3)
    val contentPadding = if (columns == 2) 16.dp else 12.dp
    val titleStyle = if (columns == 3) MaterialTheme.typography.bodySmall else MaterialTheme.typography.bodyMedium
    val subtitleStyle = MaterialTheme.typography.labelSmall
    val endNodeStyle = if (columns == 3) MaterialTheme.typography.labelSmall else MaterialTheme.typography.labelSmall
    val rowHeight = 20.dp
    
    // 标题滚动动画状态
    var titleWidth by remember { mutableStateOf(0f) }
    var containerWidth by remember { mutableStateOf(0f) }
    val shouldScroll = titleWidth > containerWidth - 40f
    
    val infiniteTransition = rememberInfiniteTransition(label = "title_scroll")
    val scrollOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (shouldScroll) -(titleWidth + 20f) else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (shouldScroll) (titleWidth / 50).toInt().coerceIn(5000, 15000) else 1,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "title_offset"
    )

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 84.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    containerWidth = coordinates.size.width.toFloat()
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = contentPadding, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // 第1行：节点名（支持自动滚动）
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(rowHeight)
                        .clip(RoundedCornerShape(2.dp)),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = title,
                        style = titleStyle,
                        color = contentColor,
                        maxLines = 1,
                        softWrap = false,
                        modifier = Modifier
                            .onGloballyPositioned { coordinates ->
                                titleWidth = coordinates.size.width.toFloat()
                            }
                            .graphicsLayer {
                                translationX = scrollOffset
                            },
                        overflow = TextOverflow.Clip
                    )
                }
                
                // 第2行：策略类型 + 延迟值
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(rowHeight),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = subtitle,
                        style = subtitleStyle,
                        color = contentColor.copy(alpha = 0.8f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        softWrap = false,
                        modifier = Modifier.weight(1f)
                    )
                    
                    if (delay != null) {
                        Text(
                            text = delay,
                            style = subtitleStyle,
                            color = contentColor.copy(alpha = 0.7f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            softWrap = false,
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .widthIn(min = 56.dp)
                        )
                    }
                }
                
                // 第3行：终末节点（策略组专用）
                if (endNode != null && endNode.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(rowHeight),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = endNode,
                            style = endNodeStyle,
                            color = contentColor.copy(alpha = 0.7f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            softWrap = false
                        )
                    }
                }
            }
            
            // 显示固定节点标记
            if (isPinned) {
                Text(
                    text = "📌",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 4.dp, end = 4.dp)
                )
            }
        }
    }
}

/**
 * Action Label Card - 对应component_action_label.xml
 * 用于Settings主页菜单项
 */
@Composable
fun ActionLabelCard(
    title: String,
    subtitle: String? = null,
    icon: (@Composable () -> Unit)? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItemCard(
        onClick = onClick,
        modifier = modifier,
        horizontalPadding = 16.dp,
        verticalPadding = 8.dp,
        useSurface = true,
        surfaceShape = RoundedCornerShape(16.dp),
        rowPadding = PaddingValues(vertical = 16.dp, horizontal = 20.dp),
        rowHorizontalArrangement = Arrangement.spacedBy(20.dp),
        minHeight = 56.dp
    ) {
        // 图标
        icon?.invoke()
        
        // 标题和副标题
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Compose版本的偏好设置项（可点击）
 * 
 * 对应 preference_clickable.xml
 */
@Composable
fun ClickablePreference(
    title: String,
    summary: String? = null,
    icon: (@Composable () -> Unit)? = null,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    ListItemCard(
        onClick = onClick,
        modifier = modifier,
        horizontalPadding = 0.dp,
        verticalPadding = 0.dp,
        useSurface = true,
        surfaceShape = MaterialTheme.shapes.medium,
        rowHorizontalArrangement = Arrangement.spacedBy(16.dp),
        enabled = enabled
    ) {
        icon?.invoke()
        
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = if (enabled) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                }
            )
            
            if (summary != null) {
                Text(
                    text = summary,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (enabled) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                    }
                )
            }
        }
    }
}

/**
 * Compose版本的开关偏好设置项
 * 
 * 对应 preference_switch.xml
 */
@Composable
fun SwitchPreference(
    title: String,
    summary: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    ListItemCard(
        onClick = { if (enabled) onCheckedChange(!checked) },
        modifier = modifier,
        horizontalPadding = 0.dp,
        verticalPadding = 0.dp,
        useSurface = true,
        surfaceShape = MaterialTheme.shapes.medium,
        rowHorizontalArrangement = Arrangement.spacedBy(16.dp),
        enabled = enabled
    ) {
        icon?.invoke()
        
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = if (enabled) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                }
            )
            
            if (summary != null) {
                Text(
                    text = summary,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (enabled) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                    }
                )
            }
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
    }
}

/**
 * Compose版本的偏好设置分类标题
 * 
 * 对应 preference_category.xml
 */
@Composable
fun PreferenceCategory(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

/**
 * Compose版本的提示信息偏好设置项
 * 
 * 对应 preference_tips.xml
 */
@Composable
fun PreferenceTips(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(12.dp)
        )
    }
}

/**
 * Compose版本的操作标签
 * 
 * 对应 component_action_label.xml
 */
@Composable
fun ActionLabel(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

/**
 * Compose版本的大型操作标签
 * 
 * 对应 component_large_action_label.xml
 */
@Composable
fun LargeActionLabel(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

/**
 * 菜单样式的大型操作项
 *
 * 对应 component_large_action_label_menu.xml
 */
@Composable
fun LargeActionMenuLabel(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

/**
 * 危险操作样式的大型菜单项
 *
 * 对应 component_large_action_label_menu_delete.xml
 */
@Composable
fun LargeActionMenuDeleteLabel(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = MaterialTheme.colorScheme.error
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}
