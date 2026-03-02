package com.github.kr328.clash.design

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.github.kr328.clash.core.model.Proxy
import com.github.kr328.clash.core.model.TunnelState
import com.github.kr328.clash.design.compose.ProxyItemUi
import com.github.kr328.clash.design.compose.ProxyLayout
import com.github.kr328.clash.design.compose.ProxyMode
import com.github.kr328.clash.design.compose.ProxyScreen
import com.github.kr328.clash.design.compose.ProxySort
import com.github.kr328.clash.design.model.ProxyState
import com.github.kr328.clash.design.store.UiStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProxyDesign(
    context: Context,
    val mode: TunnelState.Mode?,
    val groupNames: List<String>,
    val uiStore: UiStore
) : Design<ProxyDesign.Request>(context) {
    sealed class Request {
        object ReloadAll : Request()
        object ReLaunch : Request()
        data class PatchMode(val mode: TunnelState.Mode?) : Request()
        data class Reload(val index: Int) : Request()
        data class Select(val index: Int, val name: String) : Request()
        data class UrlTest(val index: Int) : Request()
        data class SetFilterNotSelectable(val notSelectable: Boolean) : Request()
        data class SetProxyMode(val mode: ProxyMode) : Request()
        data class SetProxyLayout(val layout: ProxyLayout) : Request()
        data class SetProxySort(val sort: ProxySort) : Request()
    }

    // UI State
    private var selectedTabState by mutableStateOf(0)
    private var proxyTabsState by mutableStateOf(groupNames)
    private var proxyListsState by mutableStateOf<List<List<ProxyItemUi>>>(
        List(groupNames.size) { emptyList() }
    )
    private var urlTestingState by mutableStateOf(false)
    private var horizontalScrollingState by mutableStateOf(false)
    private var verticalBottomScrolledState by mutableStateOf(false)
    private var proxyColumnsState by mutableStateOf(uiStore.proxyLine.coerceIn(1, 3))
    
    // Menu state
    private var showMenuState by mutableStateOf(false)
    private var filterNotSelectableState by mutableStateOf(uiStore.proxyExcludeNotSelectable)
    private var selectedModeState by mutableStateOf(
        when (mode) {
            null -> ProxyMode.DEFAULT
            TunnelState.Mode.Direct -> ProxyMode.DIRECT
            TunnelState.Mode.Global -> ProxyMode.GLOBAL
            else -> ProxyMode.RULE
        }
    )
    private var selectedLayoutState by mutableStateOf(
        when (uiStore.proxyLine) {
            1 -> ProxyLayout.SINGLE
            2 -> ProxyLayout.DOUBLE
            else -> ProxyLayout.MULTIPLE
        }
    )
    private var selectedSortState by mutableStateOf(
        when (uiStore.proxySort) {
            com.github.kr328.clash.core.model.ProxySort.Default -> ProxySort.DEFAULT
            com.github.kr328.clash.core.model.ProxySort.Title -> ProxySort.NAME
            com.github.kr328.clash.core.model.ProxySort.Delay -> ProxySort.DELAY
        }
    )

    override val root: View = ComposeView(context).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
        setContent {
            MaterialTheme {
                ProxyScreen(
                    title = context.getString(R.string.proxy),
                    tabs = proxyTabsState,
                    selectedTabIndex = selectedTabState,
                    proxies = proxyListsState.getOrNull(selectedTabState) ?: emptyList(),
                    proxyColumns = proxyColumnsState,
                    showFloatingTestButton = !verticalBottomScrolledState && !horizontalScrollingState && !urlTestingState,
                    isTestingDelay = urlTestingState,
                    menuExpanded = showMenuState,
                    filterNotSelectable = filterNotSelectableState,
                    selectedMode = selectedModeState,
                    selectedLayout = selectedLayoutState,
                    selectedSort = selectedSortState,
                    onBackClick = {
                        (context as? AppCompatActivity)?.onBackPressedDispatcher?.onBackPressed()
                    },
                    onDelayTestClick = {
                        requestUrlTesting()
                    },
                    onMenuClick = {
                        showMenuState = true
                    },
                    onMenuDismiss = {
                        showMenuState = false
                    },
                    onFilterChange = { value ->
                        filterNotSelectableState = value
                        requests.trySend(Request.SetFilterNotSelectable(value))
                    },
                    onModeChange = { mode ->
                        selectedModeState = mode
                        requests.trySend(Request.SetProxyMode(mode))
                    },
                    onLayoutChange = { layout ->
                        selectedLayoutState = layout
                        proxyColumnsState = layout.lines
                        requests.trySend(Request.SetProxyLayout(layout))
                    },
                    onSortChange = { sort ->
                        selectedSortState = sort
                        requests.trySend(Request.SetProxySort(sort))
                    },
                    onTabSelected = { newIndex ->
                        selectedTabState = newIndex
                        uiStore.proxyLastGroup = groupNames.getOrNull(newIndex) ?: ""
                    },
                    onProxyClick = { proxy ->
                        requests.trySend(Request.Select(selectedTabState, proxy.name))
                    }
                )
            }
        }
    }

    private var proxyGroupStates: List<ProxyState> = List(groupNames.size) { ProxyState("?") }

    init {
        // Restore last selected tab
        val lastGroupIndex = groupNames.indexOf(uiStore.proxyLastGroup)
        if (lastGroupIndex >= 0) {
            selectedTabState = lastGroupIndex
        }
    }

    suspend fun updateGroup(
        index: Int,
        proxies: List<Proxy>,
        selectable: Boolean,
        state: ProxyState,
        unorderedStates: Map<String, ProxyState>
    ) {
        withContext(Dispatchers.Main) {
            proxyGroupStates = proxyGroupStates.toMutableList().apply {
                this[index] = state
            }

            val currentList = proxyListsState[index].toMutableList()
            currentList.clear()

            // Convert proxies to UI items
            for (proxy in proxies) {
                val chainPath = buildChainPath(proxy.name, unorderedStates, proxies)
                val endNode = if (chainPath.size > 1) chainPath.lastOrNull() else null

                currentList.add(
                    ProxyItemUi(
                        name = proxy.name.replace('\n', ' ').trim(),
                        type = proxy.type.toString().replace('\n', ' ').trim(),
                        delay = if (proxy.delay in 0..Short.MAX_VALUE) "${proxy.delay}ms" else null,
                        isSelected = proxy.name == state.now,
                        isPinned = proxy.name == state.fixed,
                        endNode = endNode
                    )
                )
            }

            val newLists = proxyListsState.toMutableList()
            newLists[index] = currentList
            proxyListsState = newLists
        }
    }

    suspend fun requestRedrawVisible() {
        // Update UI as needed
    }

    suspend fun showModeSwitchTips() {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, R.string.mode_switch_tips, Toast.LENGTH_LONG).show()
        }
    }

    fun requestUrlTesting() {
        urlTestingState = true
        requests.trySend(Request.UrlTest(selectedTabState))
    }

    fun setUrlTestingFinished() {
        urlTestingState = false
    }

    /**
     * 构建完整的代理链路
     * 参照 YumeBox 的实现，递归跟随策略组的当前选择构建完整路径
     * 例如：SUB1 → SUB2 → 香港-节点A 会返回 [SUB1, SUB2, 香港-节点A]
     */
    private fun buildChainPath(
        proxyName: String,
        unorderedStates: Map<String, ProxyState>,
        allProxies: List<Proxy>
    ): List<String> {
        val path = mutableListOf<String>()
        buildChainPathRecursive(proxyName, unorderedStates, allProxies, mutableSetOf(), path)
        return path
    }

    private fun buildChainPathRecursive(
        proxyName: String,
        unorderedStates: Map<String, ProxyState>,
        allProxies: List<Proxy>,
        visited: MutableSet<String>,
        path: MutableList<String>
    ) {
        if (proxyName in visited) {
            return  // 循环检测
        }
        visited.add(proxyName)
        path.add(proxyName)
        val proxy = allProxies.find { it.name == proxyName } ?: return
        if (!proxy.type.group) {
            return
        }
        val nextNode = unorderedStates[proxyName]?.now ?: return
        if (nextNode.isBlank() || nextNode == proxyName) {
            return  // 无选择或自指，终止
        }
        buildChainPathRecursive(nextNode, unorderedStates, allProxies, visited, path)
    }
}
