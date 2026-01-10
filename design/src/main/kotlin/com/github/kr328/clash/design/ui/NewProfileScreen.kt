package com.github.kr328.clash.design.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.kr328.clash.design.R
import com.github.kr328.clash.design.model.ProfileProvider

@Composable
fun NewProfileScreen(
    providers: List<ProfileProvider>,
    insets: Insets,
    onSelect: (ProfileProvider) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.new_profile),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Provider List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 0.dp,
                    bottom = insets.bottom.dp,
                    start = insets.start.dp,
                    end = insets.end.dp
                ),
            contentPadding = PaddingValues(top = 8.dp)
        ) {
            items(providers, key = { it.javaClass.simpleName }) { provider ->
                ProviderListItem(
                    provider = provider,
                    onClick = { onSelect(provider) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun ProviderListItem(
    provider: ProfileProvider,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val title = when (provider) {
        is ProfileProvider.File -> stringResource(id = R.string.from_file)
        is ProfileProvider.External -> stringResource(id = R.string.from_url)
        is ProfileProvider.Url -> stringResource(id = R.string.from_url)
        is ProfileProvider.QR -> stringResource(id = R.string.from_qr)
    }

    val subtitle = when (provider) {
        is ProfileProvider.File -> stringResource(id = R.string.import_from_file)
        is ProfileProvider.External -> provider.name
        is ProfileProvider.Url -> stringResource(id = R.string.import_from_url)
        is ProfileProvider.QR -> stringResource(id = R.string.import_from_qr)
    }

    Surface(
        modifier = modifier
            .padding(4.dp)
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (provider is ProfileProvider.QR) {
                Icon(
                    imageVector = Icons.Default.QrCode2,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(start = 8.dp)
                )
            }
        }
    }
}
