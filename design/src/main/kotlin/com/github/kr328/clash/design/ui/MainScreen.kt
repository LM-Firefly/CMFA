package com.github.kr328.clash.design.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.kr328.clash.design.MainDesign
import com.github.kr328.clash.design.R
import com.github.kr328.clash.design.ui.component.ActionLabel
import com.github.kr328.clash.design.ui.component.LargeActionLabel

@Composable
fun MainScreen(
    clashRunning: Boolean,
    forwarded: String,
    mode: String,
    profileName: String?,
    hasProviders: Boolean,
    colorClashStarted: Color,
    colorClashStopped: Color,
    onRequest: (MainDesign.Request) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 30.dp)
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Horizontal))
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Vertical))
    ) {
        // Logo & Title (Top Banner)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 90.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_clash),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Text(
                text = stringResource(id = R.string.application_name),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Toggle Status Card
        LargeActionLabel(
            icon = ImageVector.vectorResource(
                id = if (clashRunning) R.drawable.ic_outline_check_circle else R.drawable.ic_outline_not_interested
            ),
            text = stringResource(id = if (clashRunning) R.string.running else R.string.stopped),
            subtext = if (clashRunning) {
                stringResource(id = R.string.format_traffic_forwarded, forwarded)
            } else {
                stringResource(id = R.string.tap_to_start)
            },
            backgroundColor = if (clashRunning) colorClashStarted else colorClashStopped,
            onClick = { onRequest(MainDesign.Request.ToggleStatus) },
            modifier = Modifier.padding(vertical = 5.dp)
        )

        // Proxy Card
        if (clashRunning) {
            LargeActionLabel(
                icon = ImageVector.vectorResource(id = R.drawable.ic_baseline_apps),
                text = stringResource(id = R.string.proxy),
                subtext = mode,
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                onClick = { onRequest(MainDesign.Request.OpenProxy) },
                modifier = Modifier.padding(vertical = 5.dp)
            )
        }

        // Profile Card
        LargeActionLabel(
            icon = ImageVector.vectorResource(id = R.drawable.ic_baseline_view_list),
            text = stringResource(id = R.string.profile),
            subtext = if (profileName != null) {
                stringResource(id = R.string.format_profile_activated, profileName)
            } else {
                stringResource(id = R.string.not_selected)
            },
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
            onClick = { onRequest(MainDesign.Request.OpenProfiles) },
            modifier = Modifier.padding(vertical = 5.dp)
        )

        // Providers Card
        if (clashRunning && hasProviders) {
            LargeActionLabel(
                icon = ImageVector.vectorResource(id = R.drawable.ic_baseline_swap_vertical_circle),
                text = stringResource(id = R.string.providers),
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                onClick = { onRequest(MainDesign.Request.OpenProviders) },
                modifier = Modifier.padding(vertical = 5.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Secondary Action Labels
        ActionLabel(
            icon = ImageVector.vectorResource(id = R.drawable.ic_baseline_assignment),
            text = stringResource(id = R.string.logs),
            onClick = { onRequest(MainDesign.Request.OpenLogs) },
            modifier = Modifier.padding(vertical = 5.dp)
        )

        ActionLabel(
            icon = ImageVector.vectorResource(id = R.drawable.ic_baseline_settings),
            text = stringResource(id = R.string.settings),
            onClick = { onRequest(MainDesign.Request.OpenSettings) },
            modifier = Modifier.padding(vertical = 5.dp)
        )

        ActionLabel(
            icon = ImageVector.vectorResource(id = R.drawable.ic_baseline_help_center),
            text = stringResource(id = R.string.help),
            onClick = { onRequest(MainDesign.Request.OpenHelp) },
            modifier = Modifier.padding(vertical = 5.dp)
        )

        ActionLabel(
            icon = ImageVector.vectorResource(id = R.drawable.ic_baseline_info),
            text = stringResource(id = R.string.about),
            onClick = { onRequest(MainDesign.Request.OpenAbout) },
            modifier = Modifier.padding(vertical = 5.dp)
        )
    }
}
