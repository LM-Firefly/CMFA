package com.github.kr328.clash.design.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.github.kr328.clash.design.component.ProxyViewConfig
import com.github.kr328.clash.design.component.ProxyViewState

class ProxyAdapter(
    private val config: ProxyViewConfig,
    private val clicked: (String) -> Unit,
) : RecyclerView.Adapter<ProxyAdapter.Holder>() {
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    var selectable: Boolean = false
    var states: List<ProxyViewState> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(FrameLayout(parent.context))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // TODO: Implement proxy item display UI
    }

    override fun getItemCount(): Int {
        return states.size
    }
}


