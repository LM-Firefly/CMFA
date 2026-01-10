package com.github.kr328.clash.design.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.kr328.clash.design.component.ProxyViewState

@Composable
fun ProxyItem(
    state: ProxyViewState,
    selectable: Boolean,
    onClick: () -> Unit
) {
    val config = state.config
    val isSingleLine = config.proxyLine == 1
    
    val paddingHorizontal = if (isSingleLine) 0.dp else 4.dp
    val paddingVertical = if (isSingleLine) 0.dp else 4.dp
    val cornerRadius = if (isSingleLine) 0.dp else 12.dp
    val elevation = if (isSingleLine) 0.dp else 2.dp

    Card(
        onClick = if (selectable) onClick else ({}),
        enabled = selectable,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = paddingHorizontal, vertical = paddingVertical),
        shape = RoundedCornerShape(cornerRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = CardDefaults.cardColors(
            containerColor = Color(state.background),
        )
    ) {
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = config.contentPadding.dp)
                    .heightIn(min = 68.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = state.title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(state.controls),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(config.textMargin.dp))
                    Text(
                        text = state.subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(state.controls).copy(alpha = 0.8f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = state.delayText,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(state.controls)
                )
            }

            if (state.pinned) {
                Text(
                    text = "\uD83D\uDCCC",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 3.dp, end = 3.dp)
                )
            }
        }
    }
}
