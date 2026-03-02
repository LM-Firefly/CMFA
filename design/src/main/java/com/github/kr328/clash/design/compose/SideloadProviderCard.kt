package com.github.kr328.clash.design.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * 侧载提供者（应用选择）卡片组
 * 
 * 用于显示可选择的应用列表，例如从文件管理器或其他应用导入配置
 * 
 * 替代: adapter_sideload_provider.xml
 */

/**
 * 应用信息卡片
 * 
 * @param icon 应用图标（ImageVector）
 * @param label 应用标签/名称
 * @param packageName 包名（可选）
 * @param isSelected 是否选中
 * @param onClick 点击回调
 */
@Composable
fun SideloadProviderCard(
    icon: ImageVector,
    label: String,
    packageName: String? = null,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            ),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 应用图标
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // 应用信息
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (!packageName.isNullOrEmpty()) {
                    Text(
                        text = packageName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            
            // 选中状态单选按钮
            RadioButton(
                selected = isSelected,
                onClick = null // 由外部Card点击处理
            )
        }
    }
}

/**
 * 简化版应用卡片（不使用Drawable图标）
 * 
 * 适用于不需要显示真实图标的场景
 * 
 * @param label 应用标签/名称
 * @param packageName 包名（可选）
 * @param isSelected 是否选中
 * @param onClick 点击回调
 * @param iconContent 自定义图标内容（可选）
 */
@Composable
fun SimpleAppCard(
    label: String,
    packageName: String? = null,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconContent: (@Composable () -> Unit)? = null
) {
    ListItemCard(
        onClick = onClick,
        modifier = modifier
    ) {
        // 自定义图标内容
        if (iconContent != null) {
            Box(modifier = Modifier.size(40.dp)) {
                iconContent()
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
        
        // 应用信息
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            if (!packageName.isNullOrEmpty()) {
                Text(
                    text = packageName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        
        // 选中状态单选按钮
        RadioButton(
            selected = isSelected,
            onClick = null
        )
    }
}
