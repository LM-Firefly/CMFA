package com.github.kr328.clash.design.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.HelpCenter
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.NotInterested
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.kr328.clash.design.R

/**
 * 主页界面
 *
 * 替代: design_main.xml
 */
@Composable
fun MainScreen(
    clashRunning: Boolean,
    forwarded: String,
    mode: String,
    profileName: String?,
    hasProviders: Boolean,
    onToggleStatus: () -> Unit,
    onOpenProxy: () -> Unit,
    onOpenProfiles: () -> Unit,
    onOpenProviders: () -> Unit,
    onOpenLogs: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenHelp: () -> Unit,
    onOpenAbout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val appNameRes = context.resources.getIdentifier("application_name", "string", context.packageName)
    val appName = if (appNameRes != 0) context.getString(appNameRes) else stringResource(id = R.string.application_name_alpha)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 30.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 90.dp)
                .padding(vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ClashLogo(modifier = Modifier.size(50.dp))
            Spacer(modifier = Modifier.size(18.dp))
            Text(
                text = appName,
                style = MaterialTheme.typography.titleLarge
            )
        }

        MainStatusCard(
            title = if (clashRunning) stringResource(id = R.string.running) else stringResource(id = R.string.stopped),
            subtitle = if (clashRunning) {
                stringResource(id = R.string.format_traffic_forwarded, forwarded)
            } else {
                stringResource(id = R.string.tap_to_start)
            },
            icon = if (clashRunning) IcOutlineCheckCircleImageVector else IcOutlineNotInterestedImageVector,
            containerColor = if (clashRunning) {
                MaterialTheme.colorScheme.primary
            } else {
                Color(0xFF808080)
            },
            contentColor = if (clashRunning) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                Color.White
            },
            onClick = onToggleStatus
        )

        if (clashRunning) {
            MainStatusCard(
                title = stringResource(id = R.string.proxy),
                subtitle = mode,
                icon = Icons.Default.Apps,
                onClick = onOpenProxy
            )
        }

        MainStatusCard(
            title = stringResource(id = R.string.profile),
            subtitle = profileName?.let { stringResource(id = R.string.format_profile_activated, it) }
                ?: stringResource(id = R.string.not_selected),
            icon = Icons.AutoMirrored.Filled.List,
            onClick = onOpenProfiles
        )

        if (clashRunning && hasProviders) {
            MainStatusCard(
                title = stringResource(id = R.string.providers),
                subtitle = null,
                icon = Icons.Default.SwapVert,
                onClick = onOpenProviders
            )
        }

        MainEntryRow(stringResource(id = R.string.logs), Icons.AutoMirrored.Filled.Assignment, onOpenLogs)
        MainEntryRow(stringResource(id = R.string.settings), Icons.Default.Settings, onOpenSettings)
        MainEntryRow(stringResource(id = R.string.help), Icons.AutoMirrored.Filled.HelpCenter, onOpenHelp)
        MainEntryRow(stringResource(id = R.string.about), Icons.Default.Info, onOpenAbout)
    }
}

@Composable
private fun MainStatusCard(
    title: String,
    subtitle: String?,
    icon: ImageVector,
    onClick: () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    contentColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 85.dp),
        onClick = onClick,
        color = containerColor,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(30.dp))
            Spacer(modifier = Modifier.size(20.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleLarge, color = contentColor)
                if (!subtitle.isNullOrBlank()) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyLarge,
                        color = contentColor.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }
}

@Composable
private fun MainEntryRow(text: String, icon: ImageVector, onClick: () -> Unit) {
    MainEntryCard(
        title = text,
        icon = icon,
        onClick = onClick
    )
}

@Composable
private fun MainEntryCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 85.dp),
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = Color.Transparent,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(30.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
