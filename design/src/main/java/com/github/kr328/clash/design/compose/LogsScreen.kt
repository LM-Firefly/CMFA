package com.github.kr328.clash.design.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ClearAll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * 日志页面
 *
 * 替代: design_logs.xml
 */
@Composable
fun LogsScreen(
    title: String,
    logMessages: List<LogItemUi>,
    onBackClick: () -> Unit,
    onClearAllClick: () -> Unit,
    onStartLogcatClick: () -> Unit,
    onLogClick: (LogItemUi) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            BackTopAppBar(
                title = title,
                onBackClick = onBackClick,
                actions = {
                    IconButton(onClick = onClearAllClick) {
                        Icon(
                            imageVector = IcBaselineClearAllImageVector,
                            contentDescription = "清空日志"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
        ) {
            Surface(
                onClick = onStartLogcatClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 0.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Clash Logcat",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Click to start",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Text(
                text = "History",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )

            LazyColumn(
                contentPadding = PaddingValues(bottom = padding.calculateBottomPadding() + 8.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(logMessages) { item ->
                    LogMessageCard(
                        level = item.level,
                        time = item.time,
                        message = item.message,
                        onClick = { onLogClick(item) }
                    )
                }
            }
        }
    }
}

data class LogItemUi(
    val level: LogLevel,
    val time: Long,
    val message: String,
    val key: String = message
)
