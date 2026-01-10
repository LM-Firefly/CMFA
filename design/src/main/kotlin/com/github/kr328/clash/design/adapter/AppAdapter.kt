package com.github.kr328.clash.design.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.RecyclerView
import com.github.kr328.clash.design.model.AppInfo
import com.github.kr328.clash.design.ui.AppItem

class AppAdapter(
    private val context: Context,
    private val selected: MutableSet<String>,
) : RecyclerView.Adapter<AppAdapter.Holder>() {
    class Holder(val composeView: ComposeView) : RecyclerView.ViewHolder(composeView)

    var apps: List<AppInfo> = emptyList()

    fun rebindAll() {
        notifyItemRangeChanged(0, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        })
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val current = apps[position]

        holder.composeView.setContent {
            AppItem(
                app = current,
                selected = current.packageName in selected,
                onClick = {
                    if (current.packageName in selected) {
                        selected.remove(current.packageName)
                    } else {
                        selected.add(current.packageName)
                    }
                    notifyItemChanged(position)
                }
            )
        }
    }

    override fun getItemCount(): Int {
        return apps.size
    }
}
