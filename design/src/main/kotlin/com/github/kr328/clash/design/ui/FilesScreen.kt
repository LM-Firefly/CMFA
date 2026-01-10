package com.github.kr328.clash.design.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.github.kr328.clash.design.R
import com.github.kr328.clash.design.model.File

@Composable
fun FilesScreen(
    files: List<File>,
    currentTime: Long,
    onBack: () -> Unit,
    onNew: () -> Unit,
    onFileClick: (File) -> Unit,
    onFileMenuClick: (File) -> Unit
) {
    Scaffold(
        topBar = {
            Surface(tonalElevation = 4.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .height(56.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(ImageVector.vectorResource(R.drawable.ic_baseline_arrow_back), null)
                    }
                    Text(
                        text = stringResource(R.string.files),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onNew) {
                        Icon(ImageVector.vectorResource(R.drawable.ic_baseline_add), null)
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize()
        ) {
            items(files) { file ->
                FileItem(
                    file = file,
                    currentTime = currentTime,
                    onClick = { onFileClick(file) },
                    onMenuClick = { onFileMenuClick(file) }
                )
            }
        }
    }
}
