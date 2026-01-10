package com.github.kr328.clash.design.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.kr328.clash.design.R
import com.github.kr328.clash.service.model.Profile
import com.github.kr328.clash.design.ui.component.LargeActionLabel

@Composable
fun ProfilesMenuDialog(
    profile: Profile,
    onUpdate: () -> Unit,
    onEdit: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isImported = profile.imported
    val isFile = profile.type == Profile.Type.File

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {},
        text = {
            androidx.compose.foundation.layout.Column {
                // Update action
                if (isImported && !isFile) {
                    LargeActionLabel(
                        icon = Icons.Default.Refresh,
                        text = stringResource(R.string.update),
                        backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                        onClick = { onUpdate(); onDismiss() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Edit action
                LargeActionLabel(
                    icon = Icons.Default.Edit,
                    text = stringResource(R.string.edit),
                    backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                    onClick = { onEdit(); onDismiss() },
                    modifier = Modifier.fillMaxWidth()
                )

                // Duplicate action
                if (isImported) {
                    LargeActionLabel(
                        icon = Icons.Default.ContentCopy,
                        text = stringResource(R.string.duplicate),
                        backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                        onClick = { onDuplicate(); onDismiss() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Delete action
                LargeActionLabel(
                    icon = Icons.Default.Delete,
                    text = stringResource(R.string.delete),
                    backgroundColor = MaterialTheme.colorScheme.errorContainer,
                    onClick = { onDelete(); onDismiss() },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}
