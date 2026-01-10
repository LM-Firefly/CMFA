package com.github.kr328.clash.design.dialog

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.kr328.clash.core.model.ProxySort
import com.github.kr328.clash.core.model.TunnelState
import com.github.kr328.clash.design.R

@Composable
fun ProxyMenuDialog(
    mode: TunnelState.Mode?,
    proxyLine: Int,
    proxySort: ProxySort,
    filterEnabled: Boolean,
    onModeChanged: (TunnelState.Mode?) -> Unit,
    onProxyLineChanged: (Int) -> Unit,
    onProxySortChanged: (ProxySort) -> Unit,
    onFilterChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp)
    ) {
        // Filter Section
        Text(
            text = stringResource(R.string.filter),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(
                horizontal = 24.dp,
                vertical = 16.dp
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .heightIn(min = 48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.not_selectable),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked = filterEnabled,
                onCheckedChange = onFilterChanged
            )
        }

        // Mode Section
        Text(
            text = stringResource(R.string.mode),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(
                horizontal = 24.dp,
                vertical = 16.dp
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = mode == null,
                onClick = { onModeChanged(null) },
                label = { Text(stringResource(R.string.dont_modify)) }
            )
            FilterChip(
                selected = mode == TunnelState.Mode.Direct,
                onClick = { onModeChanged(TunnelState.Mode.Direct) },
                label = { Text(stringResource(R.string.direct_mode)) }
            )
            FilterChip(
                selected = mode == TunnelState.Mode.Global,
                onClick = { onModeChanged(TunnelState.Mode.Global) },
                label = { Text(stringResource(R.string.global_mode)) }
            )
            FilterChip(
                selected = mode == TunnelState.Mode.Rule,
                onClick = { onModeChanged(TunnelState.Mode.Rule) },
                label = { Text(stringResource(R.string.rule_mode)) }
            )
        }

        // Layout Section
        Text(
            text = stringResource(R.string.layout),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(
                horizontal = 24.dp,
                vertical = 16.dp
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = proxyLine == 1,
                onClick = { onProxyLineChanged(1) },
                label = { Text(stringResource(R.string.single)) }
            )
            FilterChip(
                selected = proxyLine == 2,
                onClick = { onProxyLineChanged(2) },
                label = { Text(stringResource(R.string.doubles)) }
            )
            FilterChip(
                selected = proxyLine == 3,
                onClick = { onProxyLineChanged(3) },
                label = { Text(stringResource(R.string.multiple)) }
            )
        }

        // Sort Section
        Text(
            text = stringResource(R.string.sort),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(
                horizontal = 24.dp,
                vertical = 16.dp
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = proxySort == ProxySort.Default,
                onClick = { onProxySortChanged(ProxySort.Default) },
                label = { Text(stringResource(R.string.default_)) }
            )
            FilterChip(
                selected = proxySort == ProxySort.Title,
                onClick = { onProxySortChanged(ProxySort.Title) },
                label = { Text(stringResource(R.string.name)) }
            )
            FilterChip(
                selected = proxySort == ProxySort.Delay,
                onClick = { onProxySortChanged(ProxySort.Delay) },
                label = { Text(stringResource(R.string.delay)) }
            )
        }
    }
}
