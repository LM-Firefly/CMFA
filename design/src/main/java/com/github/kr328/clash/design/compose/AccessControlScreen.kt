package com.github.kr328.clash.design.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.kr328.clash.design.R
import com.github.kr328.clash.design.model.AppInfoSort

/**
 * 访问控制页面
 *
 * 替代: design_access_control.xml
 */
@Composable
fun AccessControlScreen(
    title: String,
    apps: List<AccessAppItemUi>,
    menuExpanded: Boolean,
    hideSystemApps: Boolean,
    sort: AppInfoSort,
    reverse: Boolean,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    onMenuClick: () -> Unit,
    onMenuDismiss: () -> Unit,
    onSelectAll: () -> Unit,
    onSelectNone: () -> Unit,
    onSelectInvert: () -> Unit,
    onToggleHideSystemApps: () -> Unit,
    onSortChange: (AppInfoSort) -> Unit,
    onToggleReverse: () -> Unit,
    onImportFromClipboard: () -> Unit,
    onExportToClipboard: () -> Unit,
    onAppToggle: (AccessAppItemUi) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            BackTopAppBar(
                title = title,
                onBackClick = onBackClick,
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(Icons.Default.Search, contentDescription = "搜索")
                    }
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.MoreVert, contentDescription = "更多")
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = onMenuDismiss,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.select_all)) },
                            onClick = {
                                onSelectAll()
                                onMenuDismiss()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.select_none)) },
                            onClick = {
                                onSelectNone()
                                onMenuDismiss()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.select_invert)) },
                            onClick = {
                                onSelectInvert()
                                onMenuDismiss()
                            }
                        )

                        HorizontalDivider()

                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.system_apps)) },
                            trailingIcon = {
                                Checkbox(
                                    checked = !hideSystemApps,
                                    onCheckedChange = null
                                )
                            },
                            onClick = {
                                onToggleHideSystemApps()
                                onMenuDismiss()
                            }
                        )

                        HorizontalDivider()

                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.name)) },
                            trailingIcon = {
                                Checkbox(
                                    checked = sort == AppInfoSort.Label,
                                    onCheckedChange = null
                                )
                            },
                            onClick = {
                                onSortChange(AppInfoSort.Label)
                                onMenuDismiss()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.package_name)) },
                            trailingIcon = {
                                Checkbox(
                                    checked = sort == AppInfoSort.PackageName,
                                    onCheckedChange = null
                                )
                            },
                            onClick = {
                                onSortChange(AppInfoSort.PackageName)
                                onMenuDismiss()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.install_time)) },
                            trailingIcon = {
                                Checkbox(
                                    checked = sort == AppInfoSort.InstallTime,
                                    onCheckedChange = null
                                )
                            },
                            onClick = {
                                onSortChange(AppInfoSort.InstallTime)
                                onMenuDismiss()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.update_time)) },
                            trailingIcon = {
                                Checkbox(
                                    checked = sort == AppInfoSort.UpdateTime,
                                    onCheckedChange = null
                                )
                            },
                            onClick = {
                                onSortChange(AppInfoSort.UpdateTime)
                                onMenuDismiss()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.reverse)) },
                            trailingIcon = {
                                Checkbox(
                                    checked = reverse,
                                    onCheckedChange = null
                                )
                            },
                            onClick = {
                                onToggleReverse()
                                onMenuDismiss()
                            }
                        )

                        HorizontalDivider()

                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.import_from_clipboard)) },
                            onClick = {
                                onImportFromClipboard()
                                onMenuDismiss()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.export_to_clipboard)) },
                            onClick = {
                                onExportToClipboard()
                                onMenuDismiss()
                            }
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 4.dp,
                bottom = padding.calculateBottomPadding() + 8.dp
            )
        ) {
            items(apps) { app ->
                SimpleAppSelectionCard(
                    appLabel = app.label,
                    packageName = app.packageName,
                    isSelected = app.selected,
                    onClick = { onAppToggle(app) }
                )
            }
        }
    }
}

data class AccessAppItemUi(
    val label: String,
    val packageName: String,
    val selected: Boolean
)

/**
 * 访问控制应用搜索对话框
 * 
 * 替代: dialog_search.xml 用于访问控制页面的搜索功能
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccessControlSearchDialog(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    apps: List<AccessAppItemUi>,
    onAppToggle: (AccessAppItemUi) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
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
                    val focusRequester = remember { FocusRequester() }
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 16.dp)
                            .focusRequester(focusRequester),
                        placeholder = { Text(stringResource(R.string.keyword)) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )
                    
                    // 自动聚焦到输入框
                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()
                    }
                }
                
                // 搜索结果列表
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(apps) { app ->
                        SimpleAppSelectionCard(
                            appLabel = app.label,
                            packageName = app.packageName,
                            isSelected = app.selected,
                            onClick = { onAppToggle(app) }
                        )
                    }
                }
            }
        }
    }
}
