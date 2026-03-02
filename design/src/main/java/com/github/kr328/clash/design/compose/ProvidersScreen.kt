package com.github.kr328.clash.design.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * 提供者页面
 *
 * 替代: design_providers.xml
 */
@Composable
fun ProvidersScreen(
    title: String,
    providers: List<ProviderItemUi>,
    onBackClick: () -> Unit,
    onUpdateAllClick: () -> Unit,
    onProviderUpdateClick: (ProviderItemUi) -> Unit,
    modifier: Modifier = Modifier,
    isUpdatingAll: Boolean = false
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            BackTopAppBar(
                title = title,
                onBackClick = onBackClick,
                actions = {
                    if (isUpdatingAll) {
                        RotatingIcon(
                            imageVector = IcBaselineSyncImageVector,
                            contentDescription = "更新全部",
                            modifier = Modifier
                                .size(48.dp)
                                .padding(12.dp)
                        )
                    } else {
                        IconButton(onClick = onUpdateAllClick) {
                            Icon(
                                imageVector = IcBaselineSyncImageVector,
                                contentDescription = "更新全部"
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 4.dp,
                bottom = padding.calculateBottomPadding() + 8.dp
            )
        ) {
            items(providers) { provider ->
                ProviderCard(
                    name = provider.name,
                    type = provider.type,
                    elapsedTime = provider.elapsedTime,
                    isUpdating = provider.isUpdating,
                    showUpdateButton = provider.showUpdateButton,
                    onUpdateClick = { onProviderUpdateClick(provider) },
                    remainingTraffic = provider.remainingTraffic,
                    usedTraffic = provider.usedTraffic,
                    usagePercentage = provider.usagePercentage,
                    usagePercentageText = provider.usagePercentageText,
                    expireInfo = provider.expireInfo,
                    hasSubscriptionInfo = provider.hasSubscriptionInfo,
                    hasExpireInfo = provider.hasExpireInfo
                )
            }
        }
    }
}

data class ProviderItemUi(
    val name: String,
    val type: String,
    val elapsedTime: String?,
    val isUpdating: Boolean,
    val showUpdateButton: Boolean,
    val remainingTraffic: String? = null,
    val usedTraffic: String? = null,
    val usagePercentage: Int? = null,
    val usagePercentageText: String? = null,
    val expireInfo: String? = null,
    val hasSubscriptionInfo: Boolean = false,
    val hasExpireInfo: Boolean = false
)
