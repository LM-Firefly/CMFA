package com.github.kr328.clash.design.compose

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap

/**
 * 应用选择卡片
 *
 * 替代: adapter_app.xml
 */
@Composable
fun AppSelectionCard(
    appLabel: String,
    packageName: String,
    appIcon: Drawable?,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItemCard(
        onClick = onClick,
        modifier = modifier
    ) {
        appIcon?.let { drawable ->
            val bitmap = remember(drawable) {
                try {
                    drawable.toBitmap(
                        width = drawable.intrinsicWidth.coerceAtLeast(1),
                        height = drawable.intrinsicHeight.coerceAtLeast(1)
                    ).asImageBitmap()
                } catch (_: Exception) {
                    null
                }
            }
            bitmap?.let {
                Image(
                    bitmap = it,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = appLabel,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = packageName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Checkbox(
            checked = isSelected,
            onCheckedChange = null
        )
    }
}

@Composable
fun SimpleAppSelectionCard(
    appLabel: String,
    packageName: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppSelectionCard(
        appLabel = appLabel,
        packageName = packageName,
        appIcon = null,
        isSelected = isSelected,
        onClick = onClick,
        modifier = modifier
    )
}
