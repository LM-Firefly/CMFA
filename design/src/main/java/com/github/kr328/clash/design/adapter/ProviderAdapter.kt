package com.github.kr328.clash.design.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.kr328.clash.core.model.Provider
import com.github.kr328.clash.design.R
import com.github.kr328.clash.design.databinding.AdapterProviderBinding
import com.github.kr328.clash.design.model.ProviderState
import com.github.kr328.clash.design.ui.ObservableCurrentTime
import com.github.kr328.clash.design.util.*

class ProviderAdapter(
    private val context: Context,
    providers: List<Provider>,
    private val requestUpdate: (Int, Provider) -> Unit,
) : RecyclerView.Adapter<ProviderAdapter.Holder>() {
    class Holder(val binding: AdapterProviderBinding) : RecyclerView.ViewHolder(binding.root)

    private val currentTime = ObservableCurrentTime()

    val states = providers.map { ProviderState(it, it.updatedAt, false) }

    fun updateElapsed() {
        currentTime.update()
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
        return Holder(
            AdapterProviderBinding
                .inflate(context.layoutInflater, parent, false)
                .also { it.currentTime = currentTime }
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val state = states[position]

        holder.binding.provider = state.provider
        holder.binding.state = state
        if (state.provider.vehicleType == Provider.VehicleType.Inline) {
            holder.binding.endView.visibility = View.GONE
        } else {
            holder.binding.endView.visibility = View.VISIBLE
            holder.binding.update = View.OnClickListener {
                state.updating = true
                requestUpdate(position, state.provider)
            }
        }
        
        // 设置进度条颜色
        state.provider.subscriptionInfo?.let { info ->
            if (info.total > 0) {
                val percentage = state.provider.getUsagePercentage()
                val color = when {
                    percentage >= 90 -> 0xFFE53935.toInt() // Red
                    percentage >= 70 -> 0xFFFFC107.toInt() // Amber
                    else -> 0xFF4CAF50.toInt() // Green
                }
                holder.binding.progressBar.progressTintList = android.content.res.ColorStateList.valueOf(color)
            }
        }
        
        // 设置过期文本颜色
        if (state.provider.isExpired()) {
            holder.binding.expireText.setTextColor(0xFFE53935.toInt())
        }
    }

    override fun getItemCount(): Int {
        return states.size
    }
}