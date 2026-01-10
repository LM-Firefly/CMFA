package com.github.kr328.clash.design.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.kr328.clash.design.model.AppInfo
import com.github.kr328.clash.design.ui.component.CommonCard
import com.github.kr328.clash.design.ui.component.IconTextRow

@Composable
fun AppItem(
    app: AppInfo,
    selected: Boolean,
    onClick: () -> Unit
) {
    CommonCard(
        onClick = onClick,
        horizontalPadding = 8.dp
    ) {
        IconTextRow(
            icon = app.icon,
            iconSize = 30.dp,
            title = app.label,
            subtitle = app.packageName,
            trailing = {
                Checkbox(
                    checked = selected,
                    onCheckedChange = null,
                    modifier = Modifier.size(24.dp)
                )
            },
            modifier = Modifier.padding(0.dp)
        )
    }
}
