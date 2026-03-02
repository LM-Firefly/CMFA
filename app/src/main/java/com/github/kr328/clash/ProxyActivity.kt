package com.github.kr328.clash

import com.github.kr328.clash.common.util.intent
import com.github.kr328.clash.core.Clash
import com.github.kr328.clash.core.model.Proxy
import com.github.kr328.clash.core.model.TunnelState
import com.github.kr328.clash.design.ProxyDesign
import com.github.kr328.clash.design.compose.ProxyLayout
import com.github.kr328.clash.design.compose.ProxyMode
import com.github.kr328.clash.design.model.ProxyState
import com.github.kr328.clash.util.withClash
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

class ProxyActivity : BaseActivity<ProxyDesign>() {
    override suspend fun main() {
        val mode = withClash { queryOverride(Clash.OverrideSlot.Session).mode }
        val names = withClash { queryProxyGroupNames(uiStore.proxyExcludeNotSelectable) }
        val states = List(names.size) { ProxyState("?") }
        val unorderedStates = names.indices.map { names[it] to states[it] }.toMap()
        val reloadLock = Semaphore(10)

        val design = ProxyDesign(
            this,
            mode,
            names,
            uiStore
        )

        setContentDesign(design)

        design.requests.send(ProxyDesign.Request.ReloadAll)

        while (isActive) {
            select<Unit> {
                events.onReceive {
                    when (it) {
                        Event.ProfileLoaded -> {
                            val newNames = withClash {
                                queryProxyGroupNames(uiStore.proxyExcludeNotSelectable)
                            }

                            if (newNames != names) {
                                startActivity(ProxyActivity::class.intent)

                                finish()
                            }
                        }
                        else -> Unit
                    }
                }
                design.requests.onReceive {
                    when (it) {
                        ProxyDesign.Request.ReLaunch -> {
                            startActivity(ProxyActivity::class.intent)

                            finish()
                        }
                        ProxyDesign.Request.ReloadAll -> {
                            names.indices.forEach { idx ->
                                design.requests.trySend(ProxyDesign.Request.Reload(idx))
                            }
                        }
                        is ProxyDesign.Request.Reload -> {
                            launch {
                                val group = reloadLock.withPermit {
                                    withClash {
                                        queryProxyGroup(names[it.index], uiStore.proxySort)
                                    }
                                }
                                val state = states[it.index]
                                state.now = group.now
                                state.fixed = group.fixed
                                val selectable = when (group.type) {
                                    Proxy.Type.Selector -> true
                                    Proxy.Type.URLTest -> true
                                    Proxy.Type.Fallback -> true
                                    else -> false
                                }
                                design.updateGroup(
                                    it.index,
                                    group.proxies,
                                    selectable,
                                    state,
                                    unorderedStates
                                )
                            }
                        }
                        is ProxyDesign.Request.Select -> {
                            val currentGroup = names[it.index]
                            val currentState = states[it.index]
                            withClash {
                                if (currentState.fixed == it.name) {
                                    patchForceSelector(currentGroup, "")
                                } else {
                                    patchForceSelector(currentGroup, it.name)
                                }
                            }

                            design.requests.send(ProxyDesign.Request.Reload(it.index))
                        }
                        is ProxyDesign.Request.UrlTest -> {
                            launch {
                                try {
                                    withClash {
                                        healthCheck(names[it.index])
                                    }

                                    design.requests.send(ProxyDesign.Request.Reload(it.index))
                                } finally {
                                    design.setUrlTestingFinished()
                                }
                            }
                        }
                        is ProxyDesign.Request.PatchMode -> {
                            design.showModeSwitchTips()

                            withClash {
                                val o = queryOverride(Clash.OverrideSlot.Session)

                                o.mode = it.mode

                                patchOverride(Clash.OverrideSlot.Session, o)
                            }
                        }
                        is ProxyDesign.Request.SetFilterNotSelectable -> {
                            uiStore.proxyExcludeNotSelectable = it.notSelectable
                            design.requests.send(ProxyDesign.Request.ReLaunch)
                        }
                        is ProxyDesign.Request.SetProxyMode -> {
                            val targetMode = when (it.mode) {
                                ProxyMode.DEFAULT -> null
                                ProxyMode.DIRECT -> TunnelState.Mode.Direct
                                ProxyMode.GLOBAL -> TunnelState.Mode.Global
                                ProxyMode.RULE -> TunnelState.Mode.Rule
                            }
                            design.requests.send(ProxyDesign.Request.PatchMode(targetMode))
                        }
                        is ProxyDesign.Request.SetProxyLayout -> {
                            uiStore.proxyLine = it.layout.lines
                        }
                        is ProxyDesign.Request.SetProxySort -> {
                            val coreSort = when (it.sort) {
                                com.github.kr328.clash.design.compose.ProxySort.DEFAULT -> com.github.kr328.clash.core.model.ProxySort.Default
                                com.github.kr328.clash.design.compose.ProxySort.NAME -> com.github.kr328.clash.core.model.ProxySort.Title
                                com.github.kr328.clash.design.compose.ProxySort.DELAY -> com.github.kr328.clash.core.model.ProxySort.Delay
                            }
                            uiStore.proxySort = coreSort
                            names.indices.forEach { idx ->
                                design.requests.trySend(ProxyDesign.Request.Reload(idx))
                            }
                        }
                    }
                }
            }
        }
    }
}