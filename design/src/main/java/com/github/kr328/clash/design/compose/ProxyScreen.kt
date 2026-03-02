package com.github.kr328.clash.design.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.kr328.clash.design.R

/**
 * 代理页面
 *
 * 替代: design_proxy.xml
 */
@Composable
fun ProxyScreen(
    title: String,
    tabs: List<String>,
    selectedTabIndex: Int,
    proxies: List<ProxyItemUi>,
    proxyColumns: Int,
    showFloatingTestButton: Boolean,
    isTestingDelay: Boolean,
    menuExpanded: Boolean,
    filterNotSelectable: Boolean,
    selectedMode: ProxyMode,
    selectedLayout: ProxyLayout,
    selectedSort: ProxySort,
    onBackClick: () -> Unit,
    onDelayTestClick: () -> Unit,
    onMenuClick: () -> Unit,
    onMenuDismiss: () -> Unit,
    onFilterChange: (Boolean) -> Unit,
    onModeChange: (ProxyMode) -> Unit,
    onLayoutChange: (ProxyLayout) -> Unit,
    onSortChange: (ProxySort) -> Unit,
    onTabSelected: (Int) -> Unit,
    onProxyClick: (ProxyItemUi) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Column {
                BackTopAppBar(
                    title = title,
                    onBackClick = onBackClick,
                    actions = {
                        IconButton(onClick = onDelayTestClick, enabled = !isTestingDelay) {
                            if (isTestingDelay) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = IcBaselineFlashOnImageVector,
                                    contentDescription = "延迟测试"
                                )
                            }
                        }
                        IconButton(onClick = onMenuClick) {
                            Icon(
                                imageVector = IcBaselineMoreVertImageVector,
                                contentDescription = "更多"
                            )
                        }
                        
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = onMenuDismiss,
                            shape = MaterialTheme.shapes.medium
                        ) {
                            // Filter 部分
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.not_selectable)) },
                                trailingIcon = {
                                    Checkbox(
                                        checked = filterNotSelectable,
                                        onCheckedChange = null
                                    )
                                },
                                onClick = {
                                    onFilterChange(!filterNotSelectable)
                                    onMenuDismiss()
                                }
                            )
                            
                            HorizontalDivider()
                            
                            // Mode 部分
                            ProxyMode.entries.forEach { mode ->
                                DropdownMenuItem(
                                    text = { Text(stringResource(mode.labelRes)) },
                                    trailingIcon = {
                                        Checkbox(
                                            checked = selectedMode == mode,
                                            onCheckedChange = null
                                        )
                                    },
                                    onClick = {
                                        onModeChange(mode)
                                        onMenuDismiss()
                                    }
                                )
                            }
                            
                            HorizontalDivider()
                            
                            // Layout 部分
                            ProxyLayout.entries.forEach { layout ->
                                DropdownMenuItem(
                                    text = { Text(stringResource(layout.labelRes)) },
                                    trailingIcon = {
                                        Checkbox(
                                            checked = selectedLayout == layout,
                                            onCheckedChange = null
                                        )
                                    },
                                    onClick = {
                                        onLayoutChange(layout)
                                        onMenuDismiss()
                                    }
                                )
                            }
                            
                            HorizontalDivider()
                            
                            // Sort 部分
                            ProxySort.entries.forEach { sort ->
                                DropdownMenuItem(
                                    text = { Text(stringResource(sort.labelRes)) },
                                    trailingIcon = {
                                        Checkbox(
                                            checked = selectedSort == sort,
                                            onCheckedChange = null
                                        )
                                    },
                                    onClick = {
                                        onSortChange(sort)
                                        onMenuDismiss()
                                    }
                                )
                            }
                        }
                    }
                )

                PrimaryScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    edgePadding = 8.dp
                ) {
                    tabs.forEachIndexed { index, tab ->
                        Tab(
                            selected = index == selectedTabIndex,
                            onClick = { onTabSelected(index) },
                            text = { Text(tab) }
                        )
                    }
                }
                HorizontalDivider()
            }
        },
        floatingActionButton = {
            if (showFloatingTestButton) {
                FloatingActionButton(onClick = onDelayTestClick) {
                    Icon(
                        imageVector = IcBaselineFlashOnImageVector,
                        contentDescription = "延迟测试"
                    )
                }
            }
        }
    ) { padding ->
        if (proxies.isEmpty()) {
            Text(
                text = "当前没有可用代理",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp)
            )
        } else {
            val columns = proxyColumns.coerceIn(1, 3)
            if (columns == 1) {
                LazyColumn(
                    contentPadding = PaddingValues(
                        top = padding.calculateTopPadding() + 4.dp,
                        bottom = padding.calculateBottomPadding() + 8.dp,
                        start = 8.dp,
                        end = 8.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(proxies) { proxy ->
                        ProxyCard(
                            title = proxy.name,
                            subtitle = proxy.type,
                            delay = proxy.delay,
                            endNode = proxy.endNode,
                            isSelected = proxy.isSelected,
                            isPinned = proxy.isPinned,
                            proxyColumns = columns,
                            backgroundColor = if (proxy.isSelected) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.surface
                            },
                            contentColor = if (proxy.isSelected) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                            onClick = { onProxyClick(proxy) }
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(columns),
                    contentPadding = PaddingValues(
                        top = padding.calculateTopPadding() + 4.dp,
                        bottom = padding.calculateBottomPadding() + 8.dp,
                        start = 4.dp,
                        end = 4.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(proxies) { proxy ->
                        ProxyCard(
                            title = proxy.name,
                            subtitle = proxy.type,
                            delay = proxy.delay,
                            endNode = proxy.endNode,
                            isSelected = proxy.isSelected,
                            isPinned = proxy.isPinned,
                            proxyColumns = columns,
                            backgroundColor = if (proxy.isSelected) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.surface
                            },
                            contentColor = if (proxy.isSelected) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                            onClick = { onProxyClick(proxy) }
                        )
                    }
                }
            }
        }
    }
}

data class ProxyItemUi(
    val name: String,
    val type: String,
    val delay: String?,
    val isSelected: Boolean,
    val isPinned: Boolean,
    val endNode: String? = null
)

