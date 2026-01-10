package com.github.kr328.clash.design.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.RecyclerView
import com.github.kr328.clash.design.preference.TextAdapter
import com.github.kr328.clash.design.ui.EditableTextListItem

class EditableTextListAdapter<T>(
    private val context: Context,
    val values: MutableList<T>,
    private val adapter: TextAdapter<T>,
) : RecyclerView.Adapter<EditableTextListAdapter.Holder>() {
    class Holder(val composeView: ComposeView) : RecyclerView.ViewHolder(composeView)

    fun addElement(text: String) {
        val value = adapter.to(text)

        values.add(value)
        notifyItemInserted(values.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        })
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val current = values[position]

        holder.composeView.setContent {
            EditableTextListItem(
                text = adapter.from(current),
                onDelete = {
                    val index = values.indexOf(current)

                    if (index >= 0) {
                        values.removeAt(index)
                        notifyItemRemoved(index)
                    }
                }
            )
        }
    }

    override fun getItemCount(): Int {
        return values.size
    }
}
