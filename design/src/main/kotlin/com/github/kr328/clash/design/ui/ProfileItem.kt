package com.github.kr328.clash.design.ui

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
import com.github.kr328.clash.design.R
import com.github.kr328.clash.design.ui.component.*
import com.github.kr328.clash.design.util.*
import com.github.kr328.clash.service.model.Profile

@Composable
fun ProfileItem(
    profile: Profile,
    currentTime: Long,
    onClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    val context = LocalContext.current
    
    CommonCard(onClick = onClick) {
        // Title row with radio button and menu
        TitleRow(
            title = profile.name,
            leadingContent = {
                RadioButton(
                    selected = profile.active,
                    onClick = null,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            },
            trailingContent = {
                IconButton(
                    onClick = onMenuClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_more_vert),
                        contentDescription = stringResource(R.string.more),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )

        // Type and time info
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val typeText = if (profile.pending) {
                stringResource(R.string.format_type_unsaved, profile.type.toString(context))
            } else {
                profile.type.toString(context)
            }
            
            Text(
                text = typeText,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = (currentTime - profile.updatedAt).elapsedIntervalString(context),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Traffic usage section
        if (profile.total > 1) {
            UsageProgressBar(
                percentage = profile.getProfileUsagePercentage().toFloat(),
                modifier = Modifier.padding(top = 8.dp)
            )

            InfoRow(
                leftText = profile.getProfileRemainingTraffic(context),
                rightText = profile.getProfileUsedTraffic(context),
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Expiration and usage info
        if (profile.expire > 0 || profile.total > 1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (profile.expire > 0) {
                    val isExpired = profile.expire < System.currentTimeMillis()
                    Text(
                        text = profile.getProfileExpireInfo(context),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isExpired) Color(0xFFE53935) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                if (profile.total > 1 || profile.expire > 0) {
                    Text(
                        text = profile.getProfileUsagePercentageText(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
