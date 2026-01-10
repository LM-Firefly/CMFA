package com.github.kr328.clash.design.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import com.github.kr328.clash.design.model.ProfileProvider
import com.github.kr328.clash.design.ui.component.CommonCard
import com.github.kr328.clash.design.ui.component.IconTextRow

@Composable
fun ProfileProviderItem(
    provider: ProfileProvider,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    CommonCard(
        onClick = {},
        horizontalPadding = 8.dp,
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(
                onTap = { onClick() },
                onLongPress = { onLongClick() }
            )
        }
    ) {
        IconTextRow(
            icon = provider.icon,
            iconSize = 30.dp,
            title = provider.name,
            subtitle = provider.summary,
            modifier = Modifier.padding(0.dp)
        )
    }
}
