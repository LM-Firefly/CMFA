package com.github.kr328.clash.design.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.kr328.clash.design.R

/**
 * Compose版本的搜索对话框
 * 
 * 对应 dialog_search.xml 的Kotlin重写
 * 
 * @param searchQuery 当前搜索关键词
 * @param onSearchQueryChange 搜索关键词变化回调
 * @param onDismiss 关闭对话框回调
 * @param searchResults 搜索结果列表内容
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SearchDialog(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onDismiss: () -> Unit,
    searchResults: LazyListScope.(List<T>) -> Unit,
    items: List<T>,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // 顶部搜索栏
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 关闭按钮
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.close),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    // 搜索输入框
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 16.dp),
                        placeholder = { Text(stringResource(R.string.keyword)) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )
                }
                
                // 搜索结果列表
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    searchResults(items)
                }
            }
        }
    }
}

/**
 * 简化版搜索对话框，使用字符串列表作为搜索结果列表的内容
 * 
 * @param title 对话框标题（可选）
 * @param initialQuery 初始搜索关键词
 * @param items 所有可搜索的项列表
 * @param onItemSelected 选中项目回调
 * @param onDismiss 关闭对话框回调
 * @param itemContent 每个项目的显示内容
 * @param filter 过滤函数，决定哪些项目匹配搜索关键词
 */
@Composable
fun <T> SimpleSearchDialog(
    title: String? = null,
    initialQuery: String = "",
    items: List<T>,
    onItemSelected: (T) -> Unit,
    onDismiss: () -> Unit,
    itemContent: @Composable (T) -> Unit,
    filter: (T, String) -> Boolean = { _, _ -> true }
) {
    var searchQuery by remember { mutableStateOf(initialQuery) }
    
    val filteredItems = remember(items, searchQuery) {
        if (searchQuery.isEmpty()) {
            items
        } else {
            items.filter { filter(it, searchQuery) }
        }
    }

    SearchDialog(
        searchQuery = searchQuery,
        onSearchQueryChange = { searchQuery = it },
        onDismiss = onDismiss,
        items = filteredItems,
        searchResults = { filtered ->
            if (title != null) {
                item {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
            
            items(filtered) { item ->
                Surface(
                    onClick = { onItemSelected(item) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                        itemContent(item)
                    }
                }
            }
            
            if (filtered.isEmpty()) {
                item {
                    Text(
                        text = "No search results",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }
    )
}

/**
 * 字符串搜索对话框变体
 */
@Composable
fun StringSearchDialog(
    title: String? = null,
    items: List<String>,
    onItemSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    SimpleSearchDialog(
        title = title,
        items = items,
        onItemSelected = onItemSelected,
        onDismiss = onDismiss,
        itemContent = { item ->
            Text(
                text = item,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        filter = { item, query ->
            item.contains(query, ignoreCase = true)
        }
    )
}
