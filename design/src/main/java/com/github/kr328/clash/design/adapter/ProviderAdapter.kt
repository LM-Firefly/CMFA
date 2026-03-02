package com.github.kr328.clash.design.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.github.kr328.clash.core.model.Provider
import com.github.kr328.clash.design.model.ProviderState

class ProviderAdapter(
    private val context: Context,
    providers: List<Provider>,
    private val requestUpdate: (Int, Provider) -> Unit,
) : RecyclerView.Adapter<ProviderAdapter.Holder>() {
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    val states = providers.map { ProviderState(it, it.updatedAt, false) }

    fun notifyUpdated(index: Int) {
        states[index].apply {
            updating = false
        }
        notifyItemChanged(index)
    }

    fun notifyChanged(index: Int, provider: Provider) {
        states[index].apply {
            this.provider = provider
            updating = false
            updatedAt = provider.updatedAt
        }
        notifyItemChanged(index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(FrameLayout(context))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // TODO: Implement provider item display UI
    }

    override fun getItemCount(): Int {
        return states.size
    }
}


