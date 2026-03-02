package com.github.kr328.clash.design.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * 新建配置文件页面
 *
 * 替代: design_new_profile.xml
 */
@Composable
fun NewProfileScreen(
    title: String,
    providers: List<ProfileProviderItemUi>,
    onBackClick: () -> Unit,
    onProviderClick: (ProfileProviderItemUi) -> Unit,
    onProviderLongClick: (ProfileProviderItemUi) -> Unit,
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
        LazyColumn(
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 4.dp,
                bottom = padding.calculateBottomPadding() + 8.dp
            )
        ) {
            items(items = providers, key = { it.key }) { provider ->
                SideloadProviderCard(
                    icon = provider.icon,
                    label = provider.name,
                    packageName = provider.summary,
                    onClick = { onProviderClick(provider) },
                    onLongClick = { onProviderLongClick(provider) }
                )
            }
        }
    }
}

data class ProfileProviderItemUi(
    val key: String,
    val icon: ImageVector,
    val name: String,
    val summary: String,
    val supportLongClick: Boolean = false,
)
