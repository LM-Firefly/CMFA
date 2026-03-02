package com.github.kr328.clash.design

import android.content.Context
import android.view.View
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.appcompat.app.AppCompatActivity
import com.github.kr328.clash.design.compose.AccessAppItemUi
import com.github.kr328.clash.design.compose.AccessControlScreen
import com.github.kr328.clash.design.compose.AccessControlSearchDialog
import com.github.kr328.clash.design.model.AppInfo
import com.github.kr328.clash.design.model.AppInfoSort
import com.github.kr328.clash.design.store.UiStore
import com.github.kr328.clash.design.util.*
import kotlinx.coroutines.*

class AccessControlDesign(
    context: Context,
    private val uiStore: UiStore,
    private val selected: MutableSet<String>,
) : Design<AccessControlDesign.Request>(context) {
    enum class Request {
        ReloadApps,
        SelectAll,
        SelectNone,
        SelectInvert,
        Import,
        Export,
    }

    private val composeView = ComposeView(context)
    private var allApps by mutableStateOf<List<AppInfo>>(emptyList())
    private val appsState = mutableStateOf<List<AccessAppItemUi>>(emptyList())

    val apps: List<AppInfo>
        get() = allApps

    override val root: View
        get() = composeView

    suspend fun patchApps(apps: List<AppInfo>) {
        withContext(Dispatchers.Main) {
            allApps = apps
            updateAppsState()
        }
    }

    suspend fun rebindAll() {
        withContext(Dispatchers.Main) {
            updateAppsState()
        }
    }

    init {
        composeView.setContent {
            var menuExpanded by remember { mutableStateOf(false) }
            var showSearchDialog by remember { mutableStateOf(false) }
            var searchQuery by remember { mutableStateOf("") }
            var searchResults by remember { mutableStateOf<List<AccessAppItemUi>>(emptyList()) }
            var hideSystemApps by remember { mutableStateOf(!uiStore.accessControlSystemApp) }
            var sort by remember { mutableStateOf(uiStore.accessControlSort) }
            var reverse by remember { mutableStateOf(uiStore.accessControlReverse) }
            
            MaterialTheme {
                AccessControlScreen(
                    title = "访问控制",
                    apps = appsState.value,
                    menuExpanded = menuExpanded,
                    hideSystemApps = hideSystemApps,
                    sort = sort,
                    reverse = reverse,
                    onBackClick = {
                        (context as? AppCompatActivity)?.onBackPressedDispatcher?.onBackPressed()
                    },
                    onSearchClick = {
                        showSearchDialog = true
                        searchQuery = ""
                    },
                    onMenuClick = { menuExpanded = true },
                    onMenuDismiss = { menuExpanded = false },
                    onSelectAll = { requests.trySend(Request.SelectAll) },
                    onSelectNone = { requests.trySend(Request.SelectNone) },
                    onSelectInvert = { requests.trySend(Request.SelectInvert) },
                    onToggleHideSystemApps = {
                        hideSystemApps = !hideSystemApps
                        uiStore.accessControlSystemApp = !hideSystemApps
                        requests.trySend(Request.ReloadApps)
                    },
                    onSortChange = { newSort ->
                        sort = newSort
                        uiStore.accessControlSort = newSort
                        requests.trySend(Request.ReloadApps)
                    },
                    onToggleReverse = {
                        reverse = !reverse
                        uiStore.accessControlReverse = reverse
                        requests.trySend(Request.ReloadApps)
                    },
                    onImportFromClipboard = { requests.trySend(Request.Import) },
                    onExportToClipboard = { requests.trySend(Request.Export) },
                    onAppToggle = { app ->
                        if (app.selected) {
                            selected.remove(app.packageName)
                        } else {
                            selected.add(app.packageName)
                        }
                        updateAppsState()
                    }
                )
                
                if (showSearchDialog) {
                    // 实时更新搜索结果
                    LaunchedEffect(searchQuery) {
                        if (searchQuery.isEmpty()) {
                            searchResults = emptyList()
                        } else {
                            delay(200) // 防抖
                            searchResults = withContext(Dispatchers.Default) {
                                allApps
                                    .filter {
                                        it.label.contains(searchQuery, ignoreCase = true) ||
                                        it.packageName.contains(searchQuery, ignoreCase = true)
                                    }
                                    .map { app ->
                                        AccessAppItemUi(
                                            label = app.label,
                                            packageName = app.packageName,
                                            selected = app.packageName in selected
                                        )
                                    }
                            }
                        }
                    }
                    
                    AccessControlSearchDialog(
                        searchQuery = searchQuery,
                        onSearchQueryChange = { searchQuery = it },
                        apps = searchResults,
                        onAppToggle = { app ->
                            if (app.selected) {
                                selected.remove(app.packageName)
                            } else {
                                selected.add(app.packageName)
                            }
                            // 更新搜索结果中的选中状态
                            searchResults = searchResults.map {
                                if (it.packageName == app.packageName) {
                                    it.copy(selected = !it.selected)
                                } else {
                                    it
                                }
                            }
                            // 更新主列表
                            updateAppsState()
                        },
                        onDismiss = { 
                            showSearchDialog = false
                            searchQuery = ""
                        }
                    )
                }
            }
        }
    }

    private fun updateAppsState() {
        appsState.value = allApps.map { app ->
            AccessAppItemUi(
                label = app.label,
                packageName = app.packageName,
                selected = app.packageName in selected
            )
        }
    }
}
