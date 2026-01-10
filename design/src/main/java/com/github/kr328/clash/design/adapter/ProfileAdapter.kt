package com.github.kr328.clash.design.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.kr328.clash.design.databinding.AdapterProfileBinding
import com.github.kr328.clash.design.model.ProfilePageState
import com.github.kr328.clash.design.model.ProxyPageState
import com.github.kr328.clash.design.ui.ObservableCurrentTime
import com.github.kr328.clash.design.util.*
import com.github.kr328.clash.service.model.Profile

class ProfileAdapter(
    private val context: Context,
    private val onClicked: (Profile) -> Unit,
    private val onMenuClicked: (View, Profile) -> Unit,
) : RecyclerView.Adapter<ProfileAdapter.Holder>() {
    class Holder(val binding: AdapterProfileBinding) : RecyclerView.ViewHolder(binding.root)

    private val currentTime = ObservableCurrentTime()

    var profiles: List<Profile> = emptyList()
    val states = ProfilePageState()

    fun updateElapsed() {
        currentTime.update()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            AdapterProfileBinding
                .inflate(context.layoutInflater, parent, false)
                .also { it.currentTime = currentTime }
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val current = profiles[position]
        val binding = holder.binding
        if (current === binding.profile)
            return
        binding.profile = current
        binding.setClicked {
            onClicked(current)
        }
        binding.setMenu { view ->
            onMenuClicked(view, current)
        }
        if (current.total > 1) {
            val percentage = current.getProfileUsagePercentage()
            val color = when {
                percentage >= 90 -> 0xFFE53935.toInt() // Red
                percentage >= 70 -> 0xFFFFC107.toInt() // Amber
                else -> 0xFF4CAF50.toInt() // Green
            }
            binding.progressBar.progressTintList = android.content.res.ColorStateList.valueOf(color)
        }
        if (current.expire > 0 && current.expire < System.currentTimeMillis()) {
            binding.expireText.setTextColor(0xFFE53935.toInt())
        }
    }

    override fun getItemCount(): Int {
        return profiles.size
    }
}