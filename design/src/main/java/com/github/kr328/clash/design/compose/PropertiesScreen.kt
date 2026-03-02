package com.github.kr328.clash.design.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material.icons.outlined.Update
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * 配置属性页面
 *
 * 替代: design_properties.xml
 */
@Composable
fun PropertiesScreen(
    title: String,
    tips: String,
    nameTitle: String,
    urlTitle: String,
    autoUpdateTitle: String,
    disabledText: String,
    browseFilesTitle: String,
    browseFilesSummary: String,
    name: String,
    source: String,
    intervalText: String,
    processing: Boolean,
    sourceEnabled: Boolean,
    intervalEnabled: Boolean,
    onBackClick: () -> Unit,
    onInputName: () -> Unit,
    onInputUrl: () -> Unit,
    onInputInterval: () -> Unit,
    onBrowseFiles: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            BackTopAppBar(
                title = title,
                onBackClick = onBackClick,
                actions = {
                    if (processing) {
                        CircularProgressIndicator(modifier = Modifier.padding(end = 12.dp))
                    } else {
                        IconButton(onClick = onSave) {
                            Icon(Icons.Default.Save, contentDescription = "保存")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding(), bottom = padding.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = tips,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )

            ActionTextField(
                title = nameTitle,
                text = name,
                onClick = onInputName,
                icon = { Icon(IcOutlineLabelImageVector, contentDescription = null) }
            )

            ActionTextField(
                title = urlTitle,
                text = if (source.isBlank()) disabledText else source,
                onClick = { if (sourceEnabled) onInputUrl() },
                icon = { Icon(IcOutlineInboxImageVector, contentDescription = null) }
            )

            ActionTextField(
                title = autoUpdateTitle,
                text = intervalText,
                onClick = { if (intervalEnabled) onInputInterval() },
                icon = { Icon(IcOutlineUpdateImageVector, contentDescription = null) }
            )

            ClickablePreference(
                title = browseFilesTitle,
                summary = browseFilesSummary,
                onClick = onBrowseFiles,
                icon = { Icon(IcOutlineFolderImageVector, contentDescription = null) },
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}
