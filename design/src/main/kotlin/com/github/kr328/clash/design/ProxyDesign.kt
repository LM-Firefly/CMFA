package com.github.kr328.clash.design

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.viewpager2.widget.ViewPager2
import com.github.kr328.clash.core.model.Proxy
import com.github.kr328.clash.core.model.TunnelState
import com.github.kr328.clash.design.adapter.ProxyAdapter
import com.github.kr328.clash.design.adapter.ProxyPageAdapter
import com.github.kr328.clash.design.component.ProxyMenu
import com.github.kr328.clash.design.component.ProxyViewConfig
import com.github.kr328.clash.design.model.ProxyState
import com.github.kr328.clash.design.store.UiStore
import com.github.kr328.clash.design.ui.ProxyScreen
import com.github.kr328.clash.design.util.insets
import com.github.kr328.clash.design.util.resolveThemedColor
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProxyDesign(
    context: Context,
    overrideMode: TunnelState.Mode?,
    groupNames: List<String>,
    uiStore: UiStore,
) : Design<ProxyDesign.Request>(context) {
    sealed class Request {
        object ReloadAll : Request()
        object ReLaunch : Request()

        data class PatchMode(val mode: TunnelState.Mode?) : Request()
        data class Reload(val index: Int) : Request()
        data class Select(val index: Int, val name: String) : Request()
        data class UrlTest(val index: Int) : Request()
    }

    private val composeView: ComposeView = ComposeView(context).apply {
        setBackgroundColor(android.graphics.Color.TRANSPARENT)
    }

    private val insets = context.insets
    private var isEmpty by mutableStateOf(groupNames.isEmpty())
    private var urlTesting by mutableStateOf(false)

    private var config = ProxyViewConfig(context, uiStore.proxyLine)

    private val menu: ProxyMenu by lazy {
        ProxyMenu(context, composeView, overrideMode, uiStore, requests) {
            config.proxyLine = uiStore.proxyLine
        }
    }

    private var currentAdapter: ProxyPageAdapter? = null
    private var pageAdapter: ProxyPageAdapter? = null

    override val root: View
        get() = composeView

    suspend fun updateGroup(
        position: Int,
        proxies: List<Proxy>,
        selectable: Boolean,
        parent: ProxyState,
        links: Map<String, ProxyState>
    ) {
        pageAdapter?.updateAdapter(position, proxies, selectable, parent, links)
        updateUrlTestButtonStatus()
    }

    suspend fun requestRedrawVisible() {
        withContext(Dispatchers.Main) {
            pageAdapter?.requestRedrawVisible()
        }
    }

    suspend fun showModeSwitchTips() {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, R.string.mode_switch_tips, Toast.LENGTH_LONG).show()
        }
    }

    init {
        composeView.setContent {
            ProxyScreen(
                isEmpty = isEmpty,
                urlTesting = urlTesting,
                insets = insets,
                onUrlTest = { requestUrlTesting() },
                onMenu = { menu.show() }
            )
        }

        if (groupNames.isNotEmpty()) {
            pageAdapter = ProxyPageAdapter(
                surface,
                config,
                List(groupNames.size) { index ->
                    ProxyAdapter(config) { name ->
                        requests.trySend(Request.Select(index, name))
                    }
                }
            ) {
                updateUrlTestButtonStatus()
            }
        }
    }

    fun requestUrlTesting() {
        if (!isEmpty && pageAdapter != null) {
            urlTesting = true
            requests.trySend(Request.UrlTest(0))
            updateUrlTestButtonStatus()
        }
    }

    private fun updateUrlTestButtonStatus() {
        // UI will automatically update based on urlTesting state
    }
}