package com.github.kr328.clash.design.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.github.kr328.clash.design.model.ProfilePageState
import com.github.kr328.clash.service.model.Profile

class ProfileAdapter(
    private val context: Context,
    private val onClicked: (Profile) -> Unit,
    private val onMenuClicked: (View, Profile) -> Unit,
) : RecyclerView.Adapter<ProfileAdapter.Holder>() {
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    var profiles: List<Profile> = emptyList()
    val states = ProfilePageState()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(FrameLayout(context))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // TODO: Implement profile item display UI
        val current = profiles[position]
        // onClicked and onMenuClicked callbacks available
    }

    override fun getItemCount(): Int {
        return profiles.size
    }
}


