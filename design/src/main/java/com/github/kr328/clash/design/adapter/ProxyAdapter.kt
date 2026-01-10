package com.github.kr328.clash.design.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.kr328.clash.design.databinding.AdapterProxyBinding
import com.github.kr328.clash.design.component.ProxyViewConfig
import com.github.kr328.clash.design.component.ProxyViewState
import com.github.kr328.clash.design.util.layoutInflater

class ProxyAdapter(
    private val config: ProxyViewConfig,
    private val clicked: (String) -> Unit,
) : RecyclerView.Adapter<ProxyAdapter.Holder>() {
    class Holder(val binding: AdapterProxyBinding) : RecyclerView.ViewHolder(binding.root)

    var selectable: Boolean = false
    var states: List<ProxyViewState> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(AdapterProxyBinding.inflate(config.context.layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val current = states[position]

        holder.binding.apply {
            state = current

            root.setOnClickListener {
                clicked(current.proxy.name)
            }

            val isSelector = selectable

            root.isFocusable = isSelector
            root.isClickable = isSelector

            current.update(true)
            executePendingBindings()
        }
    }

    override fun getItemCount(): Int {
        return states.size
    }
}
