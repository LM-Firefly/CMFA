package com.github.kr328.clash.design.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.github.kr328.clash.design.model.ProfileProvider

class ProfileProviderAdapter(
    private val context: Context,
    private val select: (ProfileProvider) -> Unit,
    private val detail: (ProfileProvider) -> Boolean,
) : RecyclerView.Adapter<ProfileProviderAdapter.Holder>() {
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    var providers: List<ProfileProvider> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(FrameLayout(context))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // TODO: Implement profile provider item display UI
        val current = providers[position]
        // select and detail callbacks available
    }

    override fun getItemCount(): Int {
        return providers.size
    }
}


