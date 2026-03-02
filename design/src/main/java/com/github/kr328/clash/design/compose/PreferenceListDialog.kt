package com.github.kr328.clash.design.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/**
 * 偏好设置列表对话框
 * 
 * 全屏对话框，用于显示和编辑可选项列表
 * 
 * 替代: dialog_preference_list.xml
 */

/**
 * 偏好设置列表对话框
 * 
 * @param title 对话框标题
 * @param items 列表项数据
 * @param selectedIndex 当前选中的索引
 * @param onItemSelected 项目选中回调
 * @param onAddClick 新增按钮点击回调（null表示不显示新增按钮）
 * @param onResetClick 重置按钮点击回调（null表示不显示重置按钮）
 * @param onDismiss 取消回调
 * @param onConfirm 确认回调
 * @param itemContent 列表项内容渲染（接收item和isSelected参数）
 */
@Composable
fun <T> PreferenceListDialog(
    title: String,
    items: List<T>,
    selectedIndex: Int = -1,
    onItemSelected: (Int) -> Unit,
    onAddClick: (() -> Unit)? = null,
    onResetClick: (() -> Unit)? = null,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    itemContent: @Composable (item: T, isSelected: Boolean) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // 顶部标题栏和新增按钮
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f)
                    )
                    
                    if (onAddClick != null) {
                        IconButton(
                            onClick = onAddClick,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add"
                            )
                        }
                    }
                }
                
                HorizontalDivider()
                
                // 列表内容
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    itemsIndexed(items) { index, item ->
                        Box(
                            modifier = Modifier.clickable {
                                onItemSelected(index)
                            }
                        ) {
                            itemContent(item, index == selectedIndex)
                        }
                    }
                }
                
                HorizontalDivider()
                
                // 底部按钮栏
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left reset button
                    if (onResetClick != null) {
                        TextButton(onClick = onResetClick) {
                            Text("Reset")
                        }
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }
                    
                    // Right cancel and confirm buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Cancel")
                        }
                        
                        TextButton(onClick = onConfirm) {
                            Text("Confirm")
                        }
                    }
                }
            }
        }
    }
}

/**
 * 简单的字符串列表对话框
 * 
 * @param title 对话框标题
 * @param items 字符串列表
 * @param selectedIndex 当前选中的索引
 * @param onAddClick 新增按钮点击回调（null表示不显示）
 * @param onResetClick 重置按钮点击回调（null表示不显示）
 * @param onDismiss 取消回调
 * @param onConfirm 确认回调，返回选中的索引
 */
@Composable
fun SimplePreferenceListDialog(
    title: String,
    items: List<String>,
    selectedIndex: Int = -1,
    onAddClick: (() -> Unit)? = null,
    onResetClick: (() -> Unit)? = null,
    onDismiss: () -> Unit,
    onConfirm: (selectedIndex: Int) -> Unit
) {
    var currentSelection by remember { mutableStateOf(selectedIndex) }
    
    PreferenceListDialog(
        title = title,
        items = items,
        selectedIndex = currentSelection,
        onItemSelected = { currentSelection = it },
        onAddClick = onAddClick,
        onResetClick = onResetClick,
        onDismiss = onDismiss,
        onConfirm = { onConfirm(currentSelection) }
    ) { item, isSelected ->
        ListItem(
            headlineContent = { Text(item) },
            trailingContent = {
                RadioButton(
                    selected = isSelected,
                    onClick = null
                )
            }
        )
    }
}

/**
 * 单选列表对话框（使用自定义数据类型）
 * 
 * @param title 对话框标题
 * @param items 数据列表
 * @param selectedItem 当前选中的项
 * @param itemLabel 获取项目显示文本的函数
 * @param onAddClick 新增按钮点击回调
 * @param onResetClick 重置按钮点击回调
 * @param onDismiss 取消回调
 * @param onConfirm 确认回调，返回选中的项
 */
@Composable
fun <T> SingleSelectionDialog(
    title: String,
    items: List<T>,
    selectedItem: T?,
    itemLabel: (T) -> String,
    onAddClick: (() -> Unit)? = null,
    onResetClick: (() -> Unit)? = null,
    onDismiss: () -> Unit,
    onConfirm: (T?) -> Unit
) {
    var currentSelection by remember { mutableStateOf(selectedItem) }
    val currentIndex = items.indexOf(currentSelection)
    
    PreferenceListDialog(
        title = title,
        items = items,
        selectedIndex = currentIndex,
        onItemSelected = { currentSelection = items[it] },
        onAddClick = onAddClick,
        onResetClick = onResetClick,
        onDismiss = onDismiss,
        onConfirm = { onConfirm(currentSelection) }
    ) { item, isSelected ->
        ListItem(
            headlineContent = { Text(itemLabel(item)) },
            trailingContent = {
                RadioButton(
                    selected = isSelected,
                    onClick = null
                )
            }
        )
    }
}

/**
 * 简单的 AlertDialog 风格单选对话框
 * 
 * 仿照 main 分支原始的 AlertDialog 样式，不使用全屏Dialog和搜索功能
 * 
 * @param title 对话框标题
 * @param items 选项列表
 * @param selectedItem 当前选中的项（可选）
 * @param itemLabel 获取选项显示文本的函数
 * @param onItemSelected 选中回调（选中后自动关闭对话框）
 * @param onDismiss 取消/关闭回调
 */
@Composable
fun <T> SimpleAlertDialog(
    title: String,
    items: List<T>,
    selectedItem: T? = null,
    itemLabel: (T) -> String,
    onItemSelected: (T) -> Unit,
    onDismiss: () -> Unit
) {
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
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(items) { item ->
                    val isSelected = item == selectedItem
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onItemSelected(item)
                                onDismiss()
                            }
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = itemLabel(item),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                        RadioButton(
                            selected = isSelected,
                            onClick = null
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}
