package com.github.kr328.clash.design.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Update
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.annotation.StringRes
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.kr328.clash.design.R

/**
 * 代理菜单对话框，对应 dialog_proxy_menu.xml
 */
@Composable
fun ProxyMenuDialog(
    filterNotSelectable: Boolean,
    onFilterChange: (Boolean) -> Unit,
    selectedMode: ProxyMode,
    onModeChange: (ProxyMode) -> Unit,
    selectedLayout: ProxyLayout,
    onLayoutChange: (ProxyLayout) -> Unit,
    selectedSort: ProxySort,
    onSortChange: (ProxySort) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 16.dp)
            ) {
                // Filter 部分
                Text(
                    text = stringResource(R.string.filter),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                )

                Surface(
                    onClick = { onFilterChange(!filterNotSelectable) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = stringResource(R.string.not_selectable))
                        Switch(checked = filterNotSelectable, onCheckedChange = null)
                    }
                }

                // Mode 部分
                Text(
                    text = stringResource(R.string.mode),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    contentPadding = PaddingValues(end = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(ProxyMode.entries) { mode ->
                        FilterChip(
                            selected = selectedMode == mode,
                            onClick = { onModeChange(mode) },
                            label = {
                                Text(text = stringResource(mode.labelRes))
                            }
                        )
                    }
                }

                // Layout 部分
                Text(
                    text = stringResource(R.string.layout),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    contentPadding = PaddingValues(end = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(ProxyLayout.entries) { layout ->
                        FilterChip(
                            selected = selectedLayout == layout,
                            onClick = { onLayoutChange(layout) },
                            label = {
                                Text(text = stringResource(layout.labelRes))
                            }
                        )
                    }
                }

                // Sort 部分
                Text(
                    text = stringResource(R.string.sort),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    contentPadding = PaddingValues(end = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(ProxySort.entries) { sort ->
                        FilterChip(
                            selected = selectedSort == sort,
                            onClick = { onSortChange(sort) },
                            label = {
                                Text(text = stringResource(sort.labelRes))
                            }
                        )
                    }
                }
            }
        }
    }
}

enum class ProxyMode(@param:StringRes val labelRes: Int) {
    DEFAULT(R.string.dont_modify),
    DIRECT(R.string.direct_mode),
    GLOBAL(R.string.global_mode),
    RULE(R.string.rule_mode)
}

enum class ProxyLayout(@param:StringRes val labelRes: Int, val lines: Int) {
    SINGLE(R.string.single, 1),
    DOUBLE(R.string.doubles, 2),
    MULTIPLE(R.string.multiple, 3)
}

enum class ProxySort(@param:StringRes val labelRes: Int) {
    DEFAULT(R.string.default_),
    NAME(R.string.name),
    DELAY(R.string.delay)
}

/**
 * 配置文件菜单对话框，对应 dialog_profiles_menu.xml
 */
@Composable
fun ProfileMenuDialog(
    isImported: Boolean,
    isFileType: Boolean,
    onUpdate: () -> Unit,
    onEdit: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    val actions = listOf(
        MenuAction(
            icon = IcOutlineUpdateImageVector,
            text = stringResource(R.string.update),
            visible = isImported && !isFileType,
            onClick = onUpdate
        ),
        MenuAction(
            icon = IcBaselineEditImageVector,
            text = stringResource(R.string.edit),
            onClick = onEdit
        ),
        MenuAction(
            icon = IcBaselineContentCopyImageVector,
            text = stringResource(R.string.duplicate),
            visible = isImported,
            onClick = onDuplicate
        ),
        MenuAction(
            icon = IcOutlineDeleteImageVector,
            text = stringResource(R.string.delete),
            isDanger = true,
            onClick = onDelete
        )
    )
    
    val visibleActions = actions.filter { it.visible }
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                visibleActions.forEach { action ->
                    Surface(
                        onClick = {
                            action.onClick()
                            onDismiss()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        color = if (action.isDanger) {
                            MaterialTheme.colorScheme.errorContainer
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            androidx.compose.material3.Icon(
                                imageVector = action.icon,
                                contentDescription = null,
                                tint = if (action.isDanger) {
                                    MaterialTheme.colorScheme.error
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                            Text(
                                text = action.text,
                                color = if (action.isDanger) {
                                    MaterialTheme.colorScheme.error
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 组菜单对话框，对应 dialog_groups_menu.xml
 */
@Composable
fun GroupsMenuDialog(
    actions: List<MenuAction>,
    onDismiss: () -> Unit
) {
    BottomMenuDialog(items = actions, onDismiss = onDismiss)
}

data class MenuAction(
    val icon: ImageVector,
    val text: String,
    val visible: Boolean = true,
    val isDanger: Boolean = false,
    val onClick: () -> Unit
)

/**
 * 底部菜单对话框，对应 dialog_bottom_menu.xml
 */
@Composable
fun BottomMenuDialog(
    items: List<MenuAction>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                items.filter { it.visible }.forEach { action ->
                    Surface(
                        onClick = {
                            action.onClick()
                            onDismiss()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        color = if (action.isDanger) {
                            MaterialTheme.colorScheme.errorContainer
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            androidx.compose.material3.Icon(
                                imageVector = action.icon,
                                contentDescription = null,
                                tint = if (action.isDanger) {
                                    MaterialTheme.colorScheme.error
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                            Text(
                                text = action.text,
                                color = if (action.isDanger) {
                                    MaterialTheme.colorScheme.error
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
