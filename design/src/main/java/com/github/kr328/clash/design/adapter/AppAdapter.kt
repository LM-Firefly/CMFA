package com.github.kr328.clash.design.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.github.kr328.clash.design.model.AppInfo

class AppAdapter(
    private val context: Context,
    private val selected: MutableSet<String>,
) : RecyclerView.Adapter<AppAdapter.Holder>() {
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    var apps: List<AppInfo> = emptyList()

    fun rebindAll() {
        notifyItemRangeChanged(0, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(FrameLayout(context))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // TODO: Implement app binding UI
    }

    override fun getItemCount(): Int {
        return apps.size
    }
}


