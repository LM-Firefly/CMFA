package com.github.kr328.clash.design.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.kr328.clash.design.R
import com.github.kr328.clash.design.model.File
import com.github.kr328.clash.design.ui.component.LargeActionLabel

@Composable
fun FilesMenuDialog(
    file: File,
    currentInBase: Boolean,
    configurationEditable: Boolean,
    onImport: () -> Unit,
    onExport: () -> Unit,
    onRename: () -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {},
        text = {
            androidx.compose.foundation.layout.Column {
                // Import action
                if (!file.isDirectory && (!currentInBase || configurationEditable)) {
                    LargeActionLabel(
                        icon = Icons.Default.GetApp,
                        text = stringResource(R.string.import_),
                        backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                        onClick = { onImport(); onDismiss() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Export action
                if (!file.isDirectory && file.size > 0) {
                    LargeActionLabel(
                        icon = Icons.Default.Publish,
                        text = stringResource(R.string.export),
                        backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                        onClick = { onExport(); onDismiss() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Rename action
                if (!currentInBase) {
                    LargeActionLabel(
                        icon = Icons.Default.Edit,
                        text = stringResource(R.string.rename),
                        backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                        onClick = { onRename(); onDismiss() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Delete action
                if (!currentInBase) {
                    LargeActionLabel(
                        icon = Icons.Default.Delete,
                        text = stringResource(R.string.delete),
                        backgroundColor = MaterialTheme.colorScheme.errorContainer,
                        onClick = { onDelete(); onDismiss() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    )
}
