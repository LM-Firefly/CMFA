package com.github.kr328.clash.design.compose

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.kr328.clash.design.R

/**
 * 通用列表项卡片基础组件
 * 
 * 为所有列表项Card提供统一的样式和行为
 * 
 * @param onClick 点击回调
 * @param modifier 修饰符
 * @param horizontalPadding 水平内边距（默认8.dp，ProfileCard使用16.dp）
 * @param verticalPadding 垂直内边距（默认4.dp）
 * @param useElevatedStyle 是否使用elevated样式（默认false，ProfileCard使用true）
 * @param rowPadding 内部Row的padding（默认16.dp）
 * @param rowHorizontalArrangement Row的水平排列方式（默认null）
 * @param minHeight 最小高度（默认null）
 * @param useSurface 使用Surface而不是Card（用于透明背景场景）
 * @param surfaceShape Surface的形状（当useSurface=true时使用）
 * @param enabled 是否启用（默认true）
 * @param content Row内容
 */
@Composable
fun ListItemCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 8.dp,
    verticalPadding: Dp = 4.dp,
    useElevatedStyle: Boolean = false,
    rowPadding: PaddingValues = PaddingValues(16.dp),
    rowHorizontalArrangement: Arrangement.Horizontal? = null,
    minHeight: Dp? = null,
    useSurface: Boolean = false,
    surfaceShape: androidx.compose.ui.graphics.Shape? = null,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    if (useSurface) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .then(if (horizontalPadding.value > 0f || verticalPadding.value > 0f) {
                    Modifier.padding(horizontal = horizontalPadding, vertical = verticalPadding)
                } else Modifier),
            onClick = onClick,
            enabled = enabled,
            shape = surfaceShape ?: androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
            color = Color.Transparent,
            tonalElevation = 0.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(rowPadding)
                    .let { mod -> minHeight?.let { mod.heightIn(min = it) } ?: mod },
                horizontalArrangement = rowHorizontalArrangement ?: Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    } else {
        Card(
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
            shape = MaterialTheme.shapes.medium,
            elevation = if (useElevatedStyle) {
                CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
            } else {
                CardDefaults.cardElevation(defaultElevation = 2.dp)
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(rowPadding)
                    .let { mod -> minHeight?.let { mod.heightIn(min = it) } ?: mod },
                horizontalArrangement = rowHorizontalArrangement ?: Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}

data class CardMenuAction(
    val text: String,
    val icon: ImageVector,
    val isDanger: Boolean = false,
    val visible: Boolean = true,
    val onClick: () -> Unit
)

@Composable
fun CardActionDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    actions: List<CardMenuAction>,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
    ) {
        actions.filter { it.visible }.forEach { action ->
            DropdownMenuItem(
                text = {
                    Text(
                        text = action.text,
                        color = if (action.isDanger) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = action.icon,
                        contentDescription = null,
                        tint = if (action.isDanger) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    )
                },
                onClick = {
                    action.onClick()
                    onDismissRequest()
                }
            )
        }
    }
}

/**
 * Compose version of Profile Card
 * 
 * Replaces adapter_profile.xml
 * 
 * @param name Profile name
 * @param type Profile type (e.g., URL, File)
 * @param isActive Whether it's the currently active profile
 * @param isPending Whether there are unsaved changes
 * @param elapsedTime Update elapsed time text (e.g., "2 hours ago")
 * @param onClick Card click callback
 * @param onMenuClick Menu button click callback
 * @param isImported Whether the profile is imported (URL/External)
 * @param isFileType Whether the profile is a file type
 * @param onUpdate Update callback (only for imported non-file profiles)
 * @param onEdit Edit callback
 * @param onDuplicate Duplicate callback (only for imported profiles)
 * @param onDelete Delete callback
 */
@Composable
fun ProfileCard(
    name: String,
    type: String,
    isActive: Boolean,
    elapsedTime: String?,
    onClick: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPending: Boolean = false,
    onUpdateClick: (() -> Unit)? = null,
    isImported: Boolean = false,
    isFileType: Boolean = false,
    onUpdate: (() -> Unit)? = null,
    onEdit: (() -> Unit)? = null,
    onDuplicate: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null
) {
    var menuExpanded by remember { mutableStateOf(false) }
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 选中状态的RadioButton
            RadioButton(
                selected = isActive,
                onClick = null,
                modifier = Modifier.size(32.dp)
            )
            
            // 配置文件信息
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // 配置文件名称
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                // 类型和更新时间
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isPending) "$type (Pending)" else type,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )
                    
                    if (elapsedTime != null) {
                        Text(
                            text = elapsedTime,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            // 更新按钮或旋转图标
            if (onUpdateClick != null) {
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isPending) {
                        RotatingIcon(
                            imageVector = IcBaselineSyncImageVector,
                            contentDescription = stringResource(R.string.update),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        IconButton(
                            onClick = onUpdateClick,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = IcBaselineSyncImageVector,
                                contentDescription = stringResource(R.string.update),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            } else {
                // 菜单按钮和下拉菜单
                Box {
                    val menuActions = listOfNotNull(
                        if (isImported && !isFileType && onUpdate != null) {
                            CardMenuAction(
                                text = stringResource(R.string.update),
                                icon = IcOutlineUpdateImageVector,
                                onClick = onUpdate
                            )
                        } else null,
                        if (onEdit != null) {
                            CardMenuAction(
                                text = stringResource(R.string.edit),
                                icon = IcBaselineEditImageVector,
                                onClick = onEdit
                            )
                        } else null,
                        if (isImported && onDuplicate != null) {
                            CardMenuAction(
                                text = stringResource(R.string.duplicate),
                                icon = IcBaselineContentCopyImageVector,
                                onClick = onDuplicate
                            )
                        } else null,
                        if (onDelete != null) {
                            CardMenuAction(
                                text = stringResource(R.string.delete),
                                icon = IcOutlineDeleteImageVector,
                                isDanger = true,
                                onClick = onDelete
                            )
                        } else null
                    )
                    val hasMenuActions = menuActions.isNotEmpty()

                    IconButton(onClick = { 
                        if (hasMenuActions) {
                            menuExpanded = true
                        } else {
                            onMenuClick()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = stringResource(R.string.more),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    // 如果提供了菜单操作回调，显示 DropdownMenu
                    if (hasMenuActions) {
                        CardActionDropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false },
                            actions = menuActions
                        )
                    }
                }
            }
        }
    }
}

/**
 * Compose version of Provider Card
 * 
 * Replaces adapter_provider.xml
 * 
 * @param name Provider name
 * @param type Provider type description
 * @param elapsedTime Update elapsed time text
 * @param isUpdating Whether currently updating
 * @param showUpdateButton Whether to show update button (inline types don't show)
 * @param onUpdateClick Update button click callback
 */
@Composable
fun ProviderCard(
    name: String,
    type: String,
    elapsedTime: String?,
    isUpdating: Boolean,
    showUpdateButton: Boolean,
    onUpdateClick: () -> Unit,
    modifier: Modifier = Modifier,
    remainingTraffic: String? = null,
    usedTraffic: String? = null,
    usagePercentage: Int? = null,
    usagePercentageText: String? = null,
    expireInfo: String? = null,
    hasSubscriptionInfo: Boolean = false,
    hasExpireInfo: Boolean = false
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 提供者信息
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // 提供者名称
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    // 类型和更新时间
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = type,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                        
                        if (elapsedTime != null) {
                            Text(
                                text = elapsedTime,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                
                // 更新按钮或旋转图标
                if (showUpdateButton) {
                    Box(
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isUpdating) {
                            RotatingIcon(
                                imageVector = IcBaselineSyncImageVector,
                                contentDescription = stringResource(R.string.update),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            IconButton(
                                onClick = onUpdateClick,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    imageVector = IcBaselineSyncImageVector,
                                    contentDescription = stringResource(R.string.update),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }
            
            // 流量进度条和信息(仅在有订阅信息时显示)
            if (hasSubscriptionInfo) {
                // 流量进度条
                if (usagePercentage != null) {
                    LinearProgressIndicator(
                        progress = { usagePercentage / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        strokeCap = StrokeCap.Round,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                // 流量使用信息行
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 剩余流量
                    if (remainingTraffic != null) {
                        Text(
                            text = remainingTraffic,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    // 已使用流量
                    if (usedTraffic != null) {
                        Text(
                            text = usedTraffic,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            // 过期时间和百分比行
            if (hasExpireInfo || (hasSubscriptionInfo && usagePercentageText != null)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 过期时间
                    if (hasExpireInfo && expireInfo != null) {
                        Text(
                            text = expireInfo,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    // 百分比
                    if (hasSubscriptionInfo && usagePercentageText != null) {
                        Text(
                            text = usagePercentageText,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

/**
 * File List Item Card (Basic Version)
 * Can be used to display files, apps, etc.
 * 
 * @param title Main title
 * @param subtitle Subtitle (optional)
 * @param icon Left icon content (optional)
 * @param action Right action button content (optional)
 * @param onClick Click callback
 */
@Composable
fun ListItemCard(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    icon: (@Composable () -> Unit)? = null,
    action: (@Composable () -> Unit)? = null
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 1.dp
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 左侧图标
            icon?.invoke()
            
            // 文本内容
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // 右侧操作
            action?.invoke()
        }
    }
}

/**
 * Profile Provider Card
 * 
 * Used to display profile import options (file, URL, QR code, etc.)
 * 
 * Replaces: adapter_profile_provider.xml
 * 
 * @param icon Icon
 * @param name Name
 * @param summary Description
 * @param onClick Click callback
 */
@Composable
fun ProfileProviderCard(
    icon: ImageVector,
    name: String,
    summary: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItemCard(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Text(
                text = summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

/**
 * File/Folder Card
 * 
 * Used for file browser to display files and folders
 * 
 * Replaces: adapter_file.xml
 * 
 * @param name File name
 * @param isDirectory Whether it's a folder
 * @param size File size (bytes), null for folders
 * @param sizeText Displayed size text
 * @param modifiedText Modified time text
 * @param onClick Click callback
 * @param onMenuClick Menu button click callback
 */
@Composable
fun FileCard(
    name: String,
    isDirectory: Boolean,
    size: Long? = null,
    sizeText: String? = null,
    modifiedText: String? = null,
    onClick: () -> Unit,
    menuActions: List<CardMenuAction> = emptyList(),
    modifier: Modifier = Modifier
) {
    var menuExpanded by remember { mutableStateOf(false) }

    ListItemCard(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isDirectory) {
                IcOutlineFolderImageVector
            } else {
                IcBaselineAssignmentImageVector
            },
            contentDescription = if (isDirectory) "Folder" else "File",
            modifier = Modifier.size(40.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            if (!isDirectory && (sizeText != null || modifiedText != null)) {
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    sizeText?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    modifiedText?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
        
        if (menuActions.isNotEmpty()) {
            Box {
                IconButton(
                    onClick = { menuExpanded = true },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More"
                    )
                }

                CardActionDropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    actions = menuActions
                )
            }
        }
    }
}

/**
 * 旋转图标组件 - 用于加载/更新状态
 * 
 * @param imageVector 图标向量
 * @param contentDescription 内容描述
 * @param modifier 修饰符
 * @param tint 图标颜色
 */
@Composable
fun RotatingIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "rotating_icon")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier.rotate(rotation),
        tint = tint
    )
}

