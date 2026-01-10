package com.github.kr328.clash.design.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.kr328.clash.design.R

@Composable
fun TextFieldDialog(
    initialValue: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    errorText: String? = null,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = initialValue,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 16.dp
            ),
        isError = isError,
        supportingText = if (isError && errorText != null) {
            { Text(errorText) }
        } else null,
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge
    )
}
