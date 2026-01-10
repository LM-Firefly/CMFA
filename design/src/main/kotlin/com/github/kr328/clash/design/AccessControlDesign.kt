package com.github.kr328.clash.design

import android.content.Context
import android.view.View
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import com.github.kr328.clash.design.component.AccessControlMenu
import com.github.kr328.clash.design.dialog.SearchDialog
import com.github.kr328.clash.design.model.AppInfo
import com.github.kr328.clash.design.store.UiStore
import com.github.kr328.clash.design.ui.AccessControlScreen
import com.github.kr328.clash.design.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class AccessControlDesign(
    context: Context,
    uiStore: UiStore,
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

    private val composeView: ComposeView = ComposeView(context).apply {
        setBackgroundColor(android.graphics.Color.TRANSPARENT)
    }

    private val insets = context.insets
    private var apps by mutableStateOf<List<AppInfo>>(emptyList())
    private var searchKeyword by mutableStateOf("")
    private var filteredApps by mutableStateOf<List<AppInfo>>(emptyList())

    private val menu: AccessControlMenu by lazy {
        AccessControlMenu(context, composeView, uiStore, requests)
    }

    val appsList: List<AppInfo>
        get() = apps

    override val root: View
        get() = composeView

    suspend fun patchApps(apps: List<AppInfo>) {
        this.apps = apps
    }

    suspend fun rebindAll() {
        // Recomposition is automatic with Compose
    }

    init {
        composeView.setContent {
            AccessControlScreen(
                apps = apps,
                selectedApps = selected,
                insets = insets,
                onSearch = {
                    launch {
                        try {
                            requestSearch()
                        } finally {
                            withContext(NonCancellable) {
                                rebindAll()
                            }
                        }
                    }
                },
                onOpenMenu = {
                    menu.show()
                },
                onSelectApp = { packageName ->
                    if (packageName in selected) {
                        selected.remove(packageName)
                    } else {
                        selected.add(packageName)
                    }
                }
            )
        }
    }

    private suspend fun requestSearch() {
        coroutineScope {
            searchKeyword = ""
            filteredApps = emptyList()
            val filter = Channel<Unit>(Channel.CONFLATED)

            val searchComposeView = ComposeView(context).apply {
                setContent {
                    SearchDialog(
                        apps = filteredApps,
                        selectedApps = selected,
                        keyword = searchKeyword,
                        insets = insets,
                        onKeywordChange = { keyword ->
                            searchKeyword = keyword
                            filter.trySend(Unit)
                        },
                        onClose = {
                            cancel()
                        },
                        onSelectApp = { packageName ->
                            if (packageName in selected) {
                                selected.remove(packageName)
                            } else {
                                selected.add(packageName)
                            }
                        }
                    )
                }
            }

            val dialog = com.github.kr328.clash.design.dialog.FullScreenDialog(context)
            dialog.setContentView(searchComposeView)
            dialog.setOnDismissListener {
                cancel()
            }
            dialog.show()

            while (isActive) {
                filter.receive()

                val keyword = searchKeyword

                filteredApps = if (keyword.isEmpty()) {
                    emptyList()
                } else {
                    withContext(Dispatchers.Default) {
                        apps.filter {
                            it.label.contains(keyword, ignoreCase = true) ||
                                    it.packageName.contains(keyword, ignoreCase = true)
                        }
                    }
                }

                delay(200)
            }
        }
    }
}