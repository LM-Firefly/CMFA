package com.github.kr328.clash.design.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview

/**
 * 通用顶部应用栏组件
 * 
 * 用于应用的顶部导航和标题
 * 
 * 替代: common_activity_bar.xml
 */

/**
 * 基础顶部应用栏组件
 * 
 * @param title 标题文本
 * @param navigationIcon 导航图标（返回菜单等）
 * @param onNavigationClick 导航图标点击回调
 * @param actions 右侧操作按钮
 * @param colors 颜色配置
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTopAppBar(
    title: String,
    navigationIcon: ImageVector? = Icons.AutoMirrored.Filled.ArrowBack,
    onNavigationClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors()
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            if (navigationIcon != null && onNavigationClick != null) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = navigationIcon,
                        contentDescription = if (navigationIcon == Icons.AutoMirrored.Filled.ArrowBack) {
                            "返回"
                        } else {
                            "菜单"
                        }
                    )
                }
            }
        },
        actions = actions,
        colors = colors
    )
}

/**
 * 带返回按钮的顶部应用栏组件
 * 
 * 最常用的应用栏样式，用于子页面
 * 
 * @param title 标题文本
 * @param onBackClick 返回按钮点击回调
 * @param actions 右侧操作按钮
 */
@Composable
fun BackTopAppBar(
    title: String,
    onBackClick: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
    BaseTopAppBar(
        title = title,
        navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onNavigationClick = onBackClick,
        actions = actions
    )
}

/**
 * 带菜单按钮的顶部应用栏组件
 * 
 * 用于主页面，显示侧边栏菜单按钮
 * 
 * @param title 标题文本
 * @param onMenuClick 菜单按钮点击回调
 * @param actions 右侧操作按钮
 */
@Composable
fun MenuTopAppBar(
    title: String,
    onMenuClick: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
    BaseTopAppBar(
        title = title,
        navigationIcon = Icons.Default.Menu,
        onNavigationClick = onMenuClick,
        actions = actions
    )
}

/**
 * 无导航图标的顶部应用栏组件
 * 
 * 仅显示标题和操作按钮
 * 
 * @param title 标题文本
 * @param actions 右侧操作按钮
 */
@Composable
fun SimpleTopAppBar(
    title: String,
    actions: @Composable RowScope.() -> Unit = {}
) {
    BaseTopAppBar(
        title = title,
        navigationIcon = null,
        onNavigationClick = null,
        actions = actions
    )
}

/**
 * 带更多操作菜单的顶部应用栏组件
 * 
 * @param title 标题文本
 * @param onBackClick 返回按钮点击回调（null则不显示返回按钮）
 * @param menuItems 菜单项列表
 * @param actions 其他右侧操作按钮
 */
@Composable
fun TopAppBarWithMenu(
    title: String,
    onBackClick: (() -> Unit)? = null,
    menuItems: List<TopAppBarMenuItem> = emptyList(),
    actions: @Composable RowScope.() -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }

    BaseTopAppBar(
        title = title,
        navigationIcon = if (onBackClick != null) Icons.AutoMirrored.Filled.ArrowBack else null,
        onNavigationClick = onBackClick,
        actions = {
            actions()
            
            if (menuItems.isNotEmpty()) {
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "更多"
                        )
                    }
                    
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        menuItems.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item.title) },
                                onClick = {
                                    showMenu = false
                                    item.onClick()
                                },
                                leadingIcon = item.icon?.let {
                                    { Icon(imageVector = it, contentDescription = null) }
                                },
                                enabled = item.enabled
                            )
                        }
                    }
                }
            }
        }
    )
}

/**
 * 顶部应用栏菜单项
 * 
 * @param title 菜单项标题
 * @param icon 菜单项图标（可选）
 * @param enabled 是否启用
 * @param onClick 点击回调
 */
data class TopAppBarMenuItem(
    val title: String,
    val icon: ImageVector? = null,
    val enabled: Boolean = true,
    val onClick: () -> Unit
)
