package com.github.kr328.clash.design.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.RecyclerView
import com.github.kr328.clash.core.model.Provider
import com.github.kr328.clash.design.model.ProviderState
import com.github.kr328.clash.design.ui.ProviderItem

class ProviderAdapter(
    private val context: Context,
    providers: List<Provider>,
    private val requestUpdate: (Int, Provider) -> Unit,
) : RecyclerView.Adapter<ProviderAdapter.Holder>() {
    class Holder(val composeView: ComposeView) : RecyclerView.ViewHolder(composeView)

    private var currentTime by mutableStateOf(System.currentTimeMillis())

    val states = providers.map { ProviderState(it, it.updatedAt, false) }

    fun updateElapsed() {
        currentTime = System.currentTimeMillis()
    }

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
        return Holder(ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        })
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val state = states[position]

        holder.composeView.setContent {
            ProviderItem(
                state = state,
                currentTime = currentTime,
                onUpdateClick = {
                    state.updating = true
                    requestUpdate(position, state.provider)
                }
            )
        }
    }

    override fun getItemCount(): Int {
        return states.size
    }
}
