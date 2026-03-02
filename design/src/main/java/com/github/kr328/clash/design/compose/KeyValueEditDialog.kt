package com.github.kr328.clash.design.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

/**
 * Basic key-value edit dialog
 */
@Composable
fun KeyValueEditDialog(
    title: String,
    keyLabel: String = "Key",
    valueLabel: String = "Value",
    initialKey: String = "",
    initialValue: String = "",
    keyError: String? = null,
    valueError: String? = null,
    keyValidator: (String) -> String? = { null },
    valueValidator: (String) -> String? = { null },
    keyReadOnly: Boolean = false,
    onDismiss: () -> Unit,
    onConfirm: (key: String, value: String) -> Unit
) {
    var key by remember { mutableStateOf(initialKey) }
    var value by remember { mutableStateOf(initialValue) }
    var keyErrorState by remember { mutableStateOf(keyError) }
    var valueErrorState by remember { mutableStateOf(valueError) }

    fun validate(): Boolean {
        keyErrorState = keyValidator(key)
        valueErrorState = valueValidator(value)
        return keyErrorState == null && valueErrorState == null
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(16.dp),
        title = { Text(text = title) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = key,
                    onValueChange = {
                        key = it
                        keyErrorState = null
                    },
                    label = { Text(keyLabel) },
                    isError = keyErrorState != null,
                    supportingText = keyErrorState?.let { { Text(it) } },
                    readOnly = keyReadOnly,
                    enabled = !keyReadOnly,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = value,
                    onValueChange = {
                        value = it
                        valueErrorState = null
                    },
                    label = { Text(valueLabel) },
                    isError = valueErrorState != null,
                    supportingText = valueErrorState?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (validate()) {
                        onConfirm(key, value)
                    }
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

/**
 * Simplified key-value edit dialog with auto validation
 */
@Composable
fun SimpleKeyValueEditDialog(
    title: String,
    keyLabel: String = "Key",
    valueLabel: String = "Value",
    initialKey: String = "",
    initialValue: String = "",
    keyRequired: Boolean = true,
    valueRequired: Boolean = false,
    keyReadOnly: Boolean = false,
    onDismiss: () -> Unit,
    onConfirm: (key: String, value: String) -> Unit
) {
    KeyValueEditDialog(
        title = title,
        keyLabel = keyLabel,
        valueLabel = valueLabel,
        initialKey = initialKey,
        initialValue = initialValue,
        keyValidator = { k ->
            if (keyRequired && k.isBlank()) "Key cannot be empty" else null
        },
        valueValidator = { v ->
            if (valueRequired && v.isBlank()) "Value cannot be empty" else null
        },
        keyReadOnly = keyReadOnly,
        onDismiss = onDismiss,
        onConfirm = onConfirm
    )
}

/**
 * HTTP header edit dialog
 */
@Composable
fun HttpHeaderEditDialog(
    title: String = "Edit HTTP Header",
    initialKey: String = "",
    initialValue: String = "",
    isEdit: Boolean = false,
    onDismiss: () -> Unit,
    onConfirm: (key: String, value: String) -> Unit
) {
    KeyValueEditDialog(
        title = title,
        keyLabel = "Header Name",
        valueLabel = "Header Value",
        initialKey = initialKey,
        initialValue = initialValue,
        keyValidator = { k ->
            when {
                k.isBlank() -> "Header name cannot be empty"
                k.contains(' ') -> "Header name cannot contain spaces"
                !k.matches(Regex("^[a-zA-Z0-9-]+$")) -> "Header name can only contain letters, numbers and hyphens"
                else -> null
            }
        },
        keyReadOnly = isEdit,
        onDismiss = onDismiss,
        onConfirm = onConfirm
    )
}

/**
 * Environment variable edit dialog
 */
@Composable
fun EnvironmentVariableEditDialog(
    title: String = "Edit Environment Variable",
    initialKey: String = "",
    initialValue: String = "",
    isEdit: Boolean = false,
    onDismiss: () -> Unit,
    onConfirm: (key: String, value: String) -> Unit
) {
    KeyValueEditDialog(
        title = title,
        keyLabel = "Variable Name",
        valueLabel = "Variable Value",
        initialKey = initialKey,
        initialValue = initialValue,
        keyValidator = { k ->
            when {
                k.isBlank() -> "Variable name cannot be empty"
                !k.matches(Regex("^[A-Z_][A-Z0-9_]*$")) -> "Variable name must start with uppercase letter or underscore"
                else -> null
            }
        },
        keyReadOnly = isEdit,
        onDismiss = onDismiss,
        onConfirm = onConfirm
    )
}
