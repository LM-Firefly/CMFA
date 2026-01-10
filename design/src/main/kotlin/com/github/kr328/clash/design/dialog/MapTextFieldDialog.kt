package com.github.kr328.clash.design.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.kr328.clash.design.R

@Composable
fun MapTextFieldDialog(
    keyValue: String,
    valueValue: String,
    onKeyChange: (String) -> Unit,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = keyValue,
            onValueChange = onKeyChange,
            label = { Text(stringResource(id = R.string.key)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = valueValue,
            onValueChange = onValueChange,
            label = { Text(stringResource(id = R.string.value)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    }
}
