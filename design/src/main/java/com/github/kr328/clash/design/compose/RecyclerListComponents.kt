package com.github.kr328.clash.design.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * 通用列表容器组件
 * 
 * 用于包装LazyColumn列表，提供统一的padding和样式
 * 
 * 替代: common_recycler_list.xml
 */

/**
 * 基础列表容器
 * 
 * @param modifier Modifier
 * @param state LazyListState
 * @param contentPadding 内容padding
 * @param content 列表内容
 */
@Composable
fun RecyclerListContainer(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: LazyListScope.() -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = state,
        contentPadding = contentPadding,
        content = content
    )
}

/**
 * 带顶部应用栏padding的列表容器
 * 
 * 自动为顶部应用栏预留空间
 * 
 * @param modifier Modifier
 * @param state LazyListState
 * @param topBarHeight 顶部栏高度
 * @param bottomPadding 底部padding
 * @param content 列表内容
 */
@Composable
fun RecyclerListWithTopBar(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    topBarHeight: androidx.compose.ui.unit.Dp = 56.dp,
    bottomPadding: androidx.compose.ui.unit.Dp = 0.dp,
    content: LazyListScope.() -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = state,
        contentPadding = PaddingValues(
            top = topBarHeight,
            bottom = bottomPadding
        ),
        content = content
    )
}

/**
 * 带Scaffold的完整列表容器
 * 
 * 包含顶部应用栏和列表内容
 * 
 * @param title 标题
 * @param onBackClick 返回按钮点击回调
 * @param topBarActions 顶部栏右侧操作项
 * @param content 列表内容
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecyclerListScreen(
    title: String,
    onBackClick: (() -> Unit)? = null,
    topBarActions: @Composable RowScope.() -> Unit = {},
    content: LazyListScope.() -> Unit
) {
    Scaffold(
        topBar = {
            if (onBackClick != null) {
                BackTopAppBar(
                    title = title,
                    onBackClick = onBackClick,
                    actions = topBarActions
                )
            } else {
                SimpleTopAppBar(
                    title = title,
                    actions = topBarActions
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = paddingValues,
            content = content
        )
    }
}
