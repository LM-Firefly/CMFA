package com.github.kr328.clash.design.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Logcat页面
 *
 * 替代: design_logcat.xml
 */
@Composable
fun LogcatScreen(
    title: String,
    streaming: Boolean,
    logs: List<LogItemUi>,
    updateTick: Int,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onExportClick: () -> Unit,
    onCloseStreamClick: () -> Unit,
    onMessageLongClick: (LogItemUi) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(updateTick, streaming, logs.size) {
        if (streaming && logs.isNotEmpty()) {
            listState.scrollToItem(logs.lastIndex)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            BackTopAppBar(
                title = title,
                onBackClick = onBackClick,
                actions = {
                    if (streaming) {
                        IconButton(onClick = onCloseStreamClick) {
                            Icon(Icons.Default.Close, contentDescription = "关闭")
                        }
                    } else {
                        IconButton(onClick = onDeleteClick) {
                            Icon(IcOutlineDeleteImageVector, contentDescription = "删除")
                        }
                        IconButton(onClick = onExportClick) {
                            Icon(IcBaselineContentCopyImageVector, contentDescription = "导出")
                        }
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            state = listState,
            reverseLayout = streaming,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 4.dp,
                bottom = padding.calculateBottomPadding() + 8.dp
            )
        ) {
            items(items = logs, key = { it.key }) { item ->
                LogMessageCard(
                    level = item.level,
                    time = item.time,
                    message = item.message,
                    onLongClick = { onMessageLongClick(item) }
                )
            }
        }
    }
}
