package com.github.kr328.clash.design.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    title: String,
    appTitle: String,
    networkTitle: String,
    overrideTitle: String,
    metaFeatureTitle: String,
    onBackClick: () -> Unit,
    onStartApp: () -> Unit,
    onStartNetwork: () -> Unit,
    onStartOverride: () -> Unit,
    onStartMetaFeature: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            BackTopAppBar(
                title = title,
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        SettingsContentContainer(padding) {
            ActionLabelCard(
                title = appTitle,
                icon = { 
                    Icon(
                        IcBaselineSettingsImageVector, 
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    ) 
                },
                onClick = onStartApp
            )
            ActionLabelCard(
                title = networkTitle,
                icon = { 
                    Icon(
                        IcBaselineDnsImageVector, 
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    ) 
                },
                onClick = onStartNetwork
            )
            ActionLabelCard(
                title = overrideTitle,
                icon = { 
                    Icon(
                        IcBaselineExtensionImageVector, 
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    ) 
                },
                onClick = onStartOverride
            )
            ActionLabelCard(
                title = metaFeatureTitle,
                icon = { 
                    Icon(
                        MetaIconImageVector, 
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    ) 
                },
                onClick = onStartMetaFeature
            )
        }
    }
}

data class HelpLinkItemUi(
    val title: String,
    val summary: String,
    val url: String,
)

@Composable
fun HelpScreen(
    title: String,
    tips: String,
    documentTitle: String,
    sourcesTitle: String,
    documentLinks: List<HelpLinkItemUi>,
    sourceLinks: List<HelpLinkItemUi>,
    onBackClick: () -> Unit,
    onOpenLink: (HelpLinkItemUi) -> Unit,
    modifier: Modifier = Modifier
) {
    SettingsCommonScreen(
        title = title,
        onBackClick = onBackClick,
        modifier = modifier
    ) {
        // 信息显示卡片
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = MaterialTheme.shapes.medium
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = tips,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        PreferenceCategory(title = documentTitle)
        documentLinks.forEach { link ->
            HelpLinkCard(
                title = link.title,
                summary = link.summary,
                onClick = { onOpenLink(link) }
            )
        }

        PreferenceCategory(title = sourcesTitle)
        sourceLinks.forEach { link ->
            HelpLinkCard(
                title = link.title,
                summary = link.summary,
                onClick = { onOpenLink(link) }
            )
        }
    }
}

@Composable
private fun HelpLinkCard(
    title: String,
    summary: String,
    onClick: () -> Unit,
    icon: (@Composable () -> Unit)? = null
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.invoke()

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = summary,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun SettingsCommonScreen(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            BackTopAppBar(
                title = title,
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        SettingsContentContainer(padding, content)
    }
}

@Composable
fun MetaFeatureSettingsScreen(
    title: String,
    onBackClick: () -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            BackTopAppBar(
                title = title,
                onBackClick = onBackClick,
                actions = {
                    IconButton(onClick = onClearClick) {
                        Icon(imageVector = IcBaselineRestoreImageVector, contentDescription = "清除")
                    }
                }
            )
        }
    ) { padding ->
        SettingsContentContainer(padding, content)
    }
}

@Composable
fun OverrideSettingsScreen(
    title: String,
    onBackClick: () -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            BackTopAppBar(
                title = title,
                onBackClick = onBackClick,
                actions = {
                    IconButton(onClick = onClearClick) {
                        Icon(imageVector = IcBaselineRestoreImageVector, contentDescription = "清除")
                    }
                }
            )
        }
    ) { padding ->
        SettingsContentContainer(padding, content)
    }
}

@Composable
private fun SettingsContentContainer(
    padding: PaddingValues,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = padding.calculateTopPadding())
            .verticalScroll(rememberScrollState())
            .padding(bottom = padding.calculateBottomPadding()),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        content()
    }
}

@Composable
fun AppSettingsScreen(
    title: String,
    autoRestart: Boolean,
    onAutoRestartChange: (Boolean) -> Unit,
    darkMode: String,
    onDarkModeClick: () -> Unit,
    hideAppIcon: Boolean,
    onHideAppIconChange: (Boolean) -> Unit,
    hideFromRecents: Boolean,
    onHideFromRecentsChange: (Boolean) -> Unit,
    dynamicNotification: Boolean,
    onDynamicNotificationChange: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SettingsCommonScreen(title = title, onBackClick = onBackClick, modifier = modifier) {
        PreferenceCategory("Behavior")
        SwitchPreference(
            title = "Auto Restart",
            summary = "Allow clash auto restart",
            checked = autoRestart,
            onCheckedChange = onAutoRestartChange,
            icon = { Icon(IcBaselineRestoreImageVector, contentDescription = null, modifier = Modifier.size(24.dp)) },
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        PreferenceCategory("Interface")
        ClickablePreference(
            title = "Dark Mode",
            summary = darkMode,
            onClick = onDarkModeClick,
            icon = { Icon(imageVector = IcBaselineBrightness4ImageVector, contentDescription = null, modifier = Modifier.size(24.dp)) },
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        SwitchPreference(
            title = "Hide App Icon",
            summary = "You can dial *#*#25274638#*#* to open this App",
            checked = hideAppIcon,
            onCheckedChange = onHideAppIconChange,
            icon = { Icon(imageVector = IcBaselineHideImageVector, contentDescription = null, modifier = Modifier.size(24.dp)) },
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        SwitchPreference(
            title = "Hide from Recents",
            summary = "Hide app from the Recent apps screen",
            checked = hideFromRecents,
            onCheckedChange = onHideFromRecentsChange,
            icon = { Icon(imageVector = IcBaselineViewListImageVector, contentDescription = null, modifier = Modifier.size(24.dp)) },
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        PreferenceCategory("Service")
        SwitchPreference(
            title = "Show Traffic",
            summary = "Auto refresh traffic in notification",
            checked = dynamicNotification,
            onCheckedChange = onDynamicNotificationChange,
            icon = { Icon(imageVector = IcBaselineDomainImageVector, contentDescription = null, modifier = Modifier.size(24.dp)) },
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Composable
fun NetworkSettingsScreen(
    title: String,
    enableVpn: Boolean,
    onEnableVpnChange: (Boolean) -> Unit,
    bypassPrivateNetwork: Boolean,
    onBypassPrivateNetworkChange: (Boolean) -> Unit,
    dnsHijacking: Boolean,
    onDnsHijackingChange: (Boolean) -> Unit,
    allowBypass: Boolean,
    onAllowBypassChange: (Boolean) -> Unit,
    allowIpv6: Boolean,
    onAllowIpv6Change: (Boolean) -> Unit,
    systemProxy: Boolean,
    onSystemProxyChange: (Boolean) -> Unit,
    systemProxyEnabled: Boolean,
    stackMode: String,
    onStackModeClick: () -> Unit,
    accessControlMode: String,
    onAccessControlModeClick: () -> Unit,
    accessControlListClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SettingsCommonScreen(title = title, onBackClick = onBackClick, modifier = modifier) {
        SwitchPreference(
            title = "Route System Traffic",
            summary = "Auto routing all system traffic via VpnService",
            checked = enableVpn,
            onCheckedChange = onEnableVpnChange,
            icon = { Icon(imageVector = VpnLockImageVector, contentDescription = null, modifier = Modifier.size(24.dp)) },
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        PreferenceCategory("VpnService Options")
        SwitchPreference(
            title = "Bypass Private Network",
            summary = "Bypass private network addresses",
            checked = bypassPrivateNetwork,
            onCheckedChange = onBypassPrivateNetworkChange,
            enabled = enableVpn,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        SwitchPreference(
            title = "DNS Hijacking",
            summary = "Handle all dns packet",
            checked = dnsHijacking,
            onCheckedChange = onDnsHijackingChange,
            enabled = enableVpn,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        SwitchPreference(
            title = "Allow Bypass",
            summary = "Allows all apps to bypass this VPN connection",
            checked = allowBypass,
            onCheckedChange = onAllowBypassChange,
            enabled = enableVpn,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        SwitchPreference(
            title = "Allow Ipv6",
            summary = "Allows ipv6 traffic via VpnService",
            checked = allowIpv6,
            onCheckedChange = onAllowIpv6Change,
            enabled = enableVpn,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        if (systemProxyEnabled) {
            SwitchPreference(
                title = "System Proxy",
                summary = "Attach http proxy to VpnService",
                checked = systemProxy,
                onCheckedChange = onSystemProxyChange,
                enabled = enableVpn,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        ClickablePreference(
            title = "Stack Mode",
            summary = stackMode,
            onClick = onStackModeClick,
            enabled = enableVpn,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        PreferenceCategory("Access Control")
        ClickablePreference(
            title = "Access Control Mode",
            summary = accessControlMode,
            onClick = onAccessControlModeClick,
            enabled = enableVpn,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        ClickablePreference(
            title = "Access Control Packages",
            summary = "Configure access permission for apps",
            onClick = accessControlListClick,
            enabled = enableVpn,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}
