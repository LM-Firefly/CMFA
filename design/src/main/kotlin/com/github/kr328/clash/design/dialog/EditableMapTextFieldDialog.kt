package com.github.kr328.clash.design.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.kr328.clash.design.R

@Composable
fun EditableMapTextFieldDialog(
    keyValue: String,
    onKeyChange: (String) -> Unit,
    value: String,
    onValueChange: (String) -> Unit,
    keyError: String? = null,
    valueError: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 16.dp
            ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = keyValue,
            onValueChange = onKeyChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.key)) },
            isError = keyError != null,
            supportingText = if (keyError != null) {
                { Text(keyError) }
            } else null,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.value)) },
            isError = valueError != null,
            supportingText = if (valueError != null) {
                { Text(valueError) }
            } else null,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge
        )
    }
}
