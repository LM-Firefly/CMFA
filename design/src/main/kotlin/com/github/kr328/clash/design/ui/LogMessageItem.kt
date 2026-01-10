package com.github.kr328.clash.design.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.github.kr328.clash.core.model.LogMessage
import com.github.kr328.clash.design.ui.component.CommonCard
import com.github.kr328.clash.design.ui.component.InfoRow
import com.github.kr328.clash.design.util.format

@Composable
fun LogMessageItem(
    message: LogMessage,
    onLongClick: () -> Unit
) {
    CommonCard(
        onClick = {},
        horizontalPadding = 8.dp,
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(onLongPress = { onLongClick() })
        }
    ) {
        InfoRow(
            leftText = message.level.name,
            rightText = message.time.format(LocalContext.current, false, true),
            leftColor = MaterialTheme.colorScheme.primary,
            rightColor = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = message.message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
