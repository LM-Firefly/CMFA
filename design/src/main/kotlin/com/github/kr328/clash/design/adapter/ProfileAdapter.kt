package com.github.kr328.clash.design.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.RecyclerView
import com.github.kr328.clash.design.model.ProfilePageState
import com.github.kr328.clash.design.ui.ProfileItem
import com.github.kr328.clash.service.model.Profile

class ProfileAdapter(
    private val context: Context,
    private val onClicked: (Profile) -> Unit,
    private val onMenuClicked: (View, Profile) -> Unit,
) : RecyclerView.Adapter<ProfileAdapter.Holder>() {
    class Holder(val composeView: ComposeView) : RecyclerView.ViewHolder(composeView)

    private var currentTime by mutableStateOf(System.currentTimeMillis())

    var profiles: List<Profile> = emptyList()
    val states = ProfilePageState()

    fun updateElapsed() {
        currentTime = System.currentTimeMillis()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        })
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val current = profiles[position]

        holder.composeView.setContent {
            ProfileItem(
                profile = current,
                currentTime = currentTime,
                onClick = {
                    onClicked(current)
                },
                onMenuClick = {
                    onMenuClicked(holder.composeView, current)
                }
            )
        }
    }

    override fun getItemCount(): Int {
        return profiles.size
    }
}
