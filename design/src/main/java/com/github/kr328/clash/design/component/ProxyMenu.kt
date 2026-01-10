package com.github.kr328.clash.design.component

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.github.kr328.clash.core.model.ProxySort
import com.github.kr328.clash.core.model.TunnelState
import com.github.kr328.clash.design.ProxyDesign
import com.github.kr328.clash.design.R
import com.github.kr328.clash.design.databinding.DialogProxyMenuBinding
import com.github.kr328.clash.design.dialog.AppBottomSheetDialog
import com.github.kr328.clash.design.store.UiStore
import kotlinx.coroutines.channels.Channel

class ProxyMenu(
    private val context: Context,
    view: View,
    private val mode: TunnelState.Mode?,
    private val uiStore: UiStore,
    private val requests: Channel<ProxyDesign.Request>,
    private val updateConfig: () -> Unit,
) {
    fun show() {
        val binding = DialogProxyMenuBinding.inflate(LayoutInflater.from(context))
        val dialog = AppBottomSheetDialog(context)
        dialog.setContentView(binding.root)
        binding.filterSwitch.isChecked = uiStore.proxyExcludeNotSelectable
        when (mode) {
            null -> binding.modeDefault.isChecked = true
            TunnelState.Mode.Direct -> binding.modeDirect.isChecked = true
            TunnelState.Mode.Global -> binding.modeGlobal.isChecked = true
            TunnelState.Mode.Rule -> binding.modeRule.isChecked = true
            else -> {}
        }
        when (uiStore.proxyLine) {
            1 -> binding.layoutSingle.isChecked = true
            2 -> binding.layoutDouble.isChecked = true
            3 -> binding.layoutMultiple.isChecked = true
        }
        when (uiStore.proxySort) {
            ProxySort.Default -> binding.sortDefault.isChecked = true
            ProxySort.Title -> binding.sortName.isChecked = true
            ProxySort.Delay -> binding.sortDelay.isChecked = true
        }
        binding.filterContainer.setOnClickListener {
            binding.filterSwitch.isChecked = !binding.filterSwitch.isChecked
        }
        binding.filterSwitch.setOnCheckedChangeListener { _, isChecked ->
            uiStore.proxyExcludeNotSelectable = isChecked
            requests.trySend(ProxyDesign.Request.ReLaunch)
        }
        binding.modeGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            val checkedId = checkedIds.firstOrNull() ?: View.NO_ID
            val newMode = when (checkedId) {
                R.id.mode_default -> null
                R.id.mode_direct -> TunnelState.Mode.Direct
                R.id.mode_global -> TunnelState.Mode.Global
                R.id.mode_rule -> TunnelState.Mode.Rule
                else -> return@setOnCheckedStateChangeListener
            }
            if (newMode != mode) {
                requests.trySend(ProxyDesign.Request.PatchMode(newMode))
                dialog.dismiss()
            }
        }
        binding.layoutGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            val checkedId = checkedIds.firstOrNull() ?: View.NO_ID
            val lines = when (checkedId) {
                R.id.layout_single -> 1
                R.id.layout_double -> 2
                R.id.layout_multiple -> 3
                else -> return@setOnCheckedStateChangeListener
            }
            if (uiStore.proxyLine != lines) {
                uiStore.proxyLine = lines
                updateConfig()
                requests.trySend(ProxyDesign.Request.ReloadAll)
                dialog.dismiss()
            }
        }
        binding.sortGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            val checkedId = checkedIds.firstOrNull() ?: View.NO_ID
            val sort = when (checkedId) {
                R.id.sort_default -> ProxySort.Default
                R.id.sort_name -> ProxySort.Title
                R.id.sort_delay -> ProxySort.Delay
                else -> return@setOnCheckedStateChangeListener
            }
            if (uiStore.proxySort != sort) {
                uiStore.proxySort = sort
                requests.trySend(ProxyDesign.Request.ReloadAll)
                dialog.dismiss()
            }
        }
        dialog.show()
    }
}