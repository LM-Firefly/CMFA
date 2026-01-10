package com.github.kr328.clash.design.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.github.kr328.clash.core.model.Provider
import com.github.kr328.clash.design.R
import com.github.kr328.clash.design.model.ProviderState
import com.github.kr328.clash.design.ui.component.*
import com.github.kr328.clash.design.util.*

@Composable
fun ProviderItem(
    state: ProviderState,
    currentTime: Long,
    onUpdateClick: () -> Unit
) {
    val context = LocalContext.current
    val provider = state.provider
    
    CommonCard(onClick = {}) {
        // Title row with update button
        TitleRow(
            title = provider.name,
            trailingContent = {
                if (provider.vehicleType != Provider.VehicleType.Inline) {
                    Box(
                        modifier = Modifier.size(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Crossfade(targetState = state.updating) { updating ->
                            if (updating) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                IconButton(onClick = onUpdateClick) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_swap_vert),
                                        contentDescription = stringResource(R.string.update),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )

        // Type and time info
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = provider.type(context),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (state.updatedAt > 0) {
                Text(
                    text = (currentTime - state.updatedAt).elapsedIntervalString(context),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Subscription info section
        if (provider.hasSubscriptionInfo()) {
            UsageProgressBar(
                percentage = provider.getUsagePercentage().toFloat(),
                modifier = Modifier.padding(top = 8.dp)
            )

            InfoRow(
                leftText = provider.getRemainingTraffic(context),
                rightText = provider.getUsedTraffic(context),
                modifier = Modifier.padding(top = 4.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (provider.hasExpireInfo()) {
                    Text(
                        text = provider.getExpireInfo(context),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (provider.isExpired()) Color(0xFFE53935) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                Text(
                    text = provider.getUsagePercentageText(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
