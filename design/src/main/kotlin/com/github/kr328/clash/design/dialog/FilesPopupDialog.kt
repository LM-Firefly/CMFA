package com.github.kr328.clash.design.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.kr328.clash.design.R
import com.github.kr328.clash.design.model.File
import com.github.kr328.clash.design.ui.component.LargeActionLabel

@Composable
fun FilesPopupDialog(
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
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {},
        text = {
            Column {
                // Import action - only show for files not in base or if configuration is editable
                if (!file.isDirectory && (!currentInBase || configurationEditable)) {
                    LargeActionLabel(
                        icon = Icons.Default.GetApp,
                        text = stringResource(R.string.import_),
                        backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                        onClick = { onImport(); onDismiss() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Export action - only show for files with size > 0
                if (!file.isDirectory && file.size > 0) {
                    LargeActionLabel(
                        icon = Icons.Default.Publish,
                        text = stringResource(R.string.export),
                        backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                        onClick = { onExport(); onDismiss() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Rename action - only show for files not in base
                if (!currentInBase) {
                    LargeActionLabel(
                        icon = Icons.Default.Edit,
                        text = stringResource(R.string.rename),
                        backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                        onClick = { onRename(); onDismiss() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Delete action - only show for files not in base
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
        },
        modifier = modifier
    )
}
