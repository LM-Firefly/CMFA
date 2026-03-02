package com.github.kr328.clash.design.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.kr328.clash.design.R

/**
 * 文件管理页面
 *
 * 替代: design_files.xml
 */
@Composable
fun FilesScreen(
    title: String,
    files: List<FileItemUi>,
    currentInBaseDir: Boolean,
    configurationEditable: Boolean,
    onBackClick: () -> Unit,
    onNewClick: () -> Unit,
    onFileClick: (FileItemUi) -> Unit,
    onFileImportClick: (FileItemUi) -> Unit,
    onFileExportClick: (FileItemUi) -> Unit,
    onFileRenameClick: (FileItemUi) -> Unit,
    onFileDeleteClick: (FileItemUi) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            BackTopAppBar(
                title = title,
                onBackClick = onBackClick,
                actions = {
                    if (!currentInBaseDir) {
                        IconButton(onClick = onNewClick) {
                            Icon(
                                imageVector = IcBaselineAddImageVector,
                                contentDescription = "新建"
                            )
                        }
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
            items(items = files, key = { it.key }) { file ->
                val menuActions = listOf(
                    CardMenuAction(
                        icon = IcBaselineGetAppImageVector,
                        text = stringResource(R.string.import_),
                        visible = !file.isDirectory && (!currentInBaseDir || configurationEditable),
                        onClick = { onFileImportClick(file) }
                    ),
                    CardMenuAction(
                        icon = IcBaselinePublishImageVector,
                        text = stringResource(R.string.export),
                        onClick = { onFileExportClick(file) }
                    ),
                    CardMenuAction(
                        icon = IcBaselineEditImageVector,
                        text = stringResource(R.string.rename),
                        visible = !currentInBaseDir || configurationEditable,
                        onClick = { onFileRenameClick(file) }
                    ),
                    CardMenuAction(
                        icon = IcOutlineDeleteImageVector,
                        text = stringResource(R.string.delete),
                        isDanger = true,
                        onClick = { onFileDeleteClick(file) }
                    )
                )

                FileCard(
                    name = file.name,
                    isDirectory = file.isDirectory,
                    size = file.size,
                    sizeText = file.sizeText,
                    modifiedText = file.modifiedText,
                    onClick = { onFileClick(file) },
                    menuActions = menuActions
                )
            }
        }
    }
}

data class FileItemUi(
    val key: String,
    val name: String,
    val isDirectory: Boolean,
    val size: Long = 0,
    val sizeText: String? = null,
    val modifiedText: String? = null
)
