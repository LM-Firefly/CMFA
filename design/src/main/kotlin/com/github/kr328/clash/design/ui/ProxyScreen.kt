package com.github.kr328.clash.design.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.materialPath
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.kr328.clash.design.R

// Flash icon
val FlashOnIcon: ImageVector = ImageVector.Builder(
    name = "FlashOn",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    materialPath {
        moveTo(7f, 2f)
        lineTo(17f, 13f)
        horizontalLineTo(9.98f)
        lineTo(17f, 22f)
        horizontalLineTo(7f)
        lineTo(13.01f, 13f)
        horizontalLineTo(7f)
        close()
    }
}.build()

@Composable
fun ProxyScreen(
    isEmpty: Boolean,
    urlTesting: Boolean,
    insets: Insets,
    onUrlTest: () -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        if (isEmpty) {
            // Empty state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.proxy_empty_tips),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Top Bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.proxies),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (!isEmpty) {
                    Row(
                        modifier = Modifier.wrapContentSize(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = onUrlTest,
                            enabled = !urlTesting,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = FlashOnIcon,
                                contentDescription = stringResource(id = R.string.delay_test),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        IconButton(
                            onClick = onMenu,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = stringResource(id = R.string.more),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // FloatingActionButton
        if (!isEmpty && !urlTesting) {
            FloatingActionButton(
                onClick = onUrlTest,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        bottom = 16.dp + insets.bottom.dp,
                        end = 16.dp + insets.end.dp
                    ),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = FlashOnIcon,
                    contentDescription = stringResource(id = R.string.delay_test),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
