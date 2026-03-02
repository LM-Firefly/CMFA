package com.github.kr328.clash.design.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
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
 * 配置文件页面
 *
 * 替代: design_profiles.xml
 */
@Composable
fun ProfilesScreen(
    title: String,
    profiles: List<ProfileItemUi>,
    onBackClick: () -> Unit,
    onCreateClick: () -> Unit,
    onUpdateAllClick: () -> Unit,
    onProfileClick: (Int, ProfileItemUi) -> Unit,
    onProfileMenuClick: (Int, ProfileItemUi) -> Unit,
    onProfileUpdateClick: ((Int, ProfileItemUi) -> Unit)? = null,
    onProfileUpdate: ((Int, ProfileItemUi) -> Unit)? = null,
    onProfileEdit: ((Int, ProfileItemUi) -> Unit)? = null,
    onProfileDuplicate: ((Int, ProfileItemUi) -> Unit)? = null,
    onProfileDelete: ((Int, ProfileItemUi) -> Unit)? = null,
    showUpdateAll: Boolean = false,
    isUpdatingAll: Boolean = false,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            BackTopAppBar(
                title = title,
                onBackClick = onBackClick,
                actions = {
                    if (showUpdateAll) {
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
                    IconButton(onClick = onCreateClick) {
                        Icon(
                            imageVector = IcBaselineAddImageVector,
                            contentDescription = "新建"
                        )
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
            itemsIndexed(profiles) { index, profile ->
                ProfileCard(
                    name = profile.name,
                    type = profile.type,
                    isActive = profile.isActive,
                    elapsedTime = profile.elapsedTime,
                    isPending = profile.isPending,
                    onClick = { onProfileClick(index, profile) },
                    onMenuClick = { onProfileMenuClick(index, profile) },
                    onUpdateClick = if (onProfileUpdateClick != null) {
                        { onProfileUpdateClick(index, profile) }
                    } else {
                        null
                    },
                    isImported = profile.isImported,
                    isFileType = profile.isFileType,
                    onUpdate = if (onProfileUpdate != null) {
                        { onProfileUpdate(index, profile) }
                    } else null,
                    onEdit = if (onProfileEdit != null) {
                        { onProfileEdit(index, profile) }
                    } else null,
                    onDuplicate = if (onProfileDuplicate != null) {
                        { onProfileDuplicate(index, profile) }
                    } else null,
                    onDelete = if (onProfileDelete != null) {
                        { onProfileDelete(index, profile) }
                    } else null
                )
            }
        }
    }
}

data class ProfileItemUi(
    val name: String,
    val type: String,
    val isActive: Boolean,
    val elapsedTime: String?,
    val isPending: Boolean,
    val isImported: Boolean = false,
    val isFileType: Boolean = false
)
