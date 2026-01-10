package com.github.kr328.clash.design.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.github.kr328.clash.design.R
import com.github.kr328.clash.design.model.File
import com.github.kr328.clash.design.ui.component.CommonCard
import com.github.kr328.clash.design.util.elapsedIntervalString
import com.github.kr328.clash.design.util.toBytesString

@Composable
fun FileItem(
    file: File,
    currentTime: Long,
    onClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    val context = LocalContext.current
    val subtitle = if (!file.isDirectory) {
        "${file.size.toBytesString()} • ${(currentTime - file.lastModified).elapsedIntervalString(context)}"
    } else null
    
    CommonCard(onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(
                    id = if (file.isDirectory) R.drawable.ic_outline_folder else R.drawable.ic_outline_article
                ),
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = file.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                subtitle?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            IconButton(
                onClick = onMenuClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_more_vert),
                    contentDescription = "More",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
