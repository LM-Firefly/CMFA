package com.github.kr328.clash.design.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.RecyclerView
import com.github.kr328.clash.design.preference.TextAdapter
import com.github.kr328.clash.design.ui.EditableTextMapItem

class EditableTextMapAdapter<K, V>(
    private val context: Context,
    val values: MutableList<Pair<K, V>>,
    private val keyAdapter: TextAdapter<K>,
    private val valueAdapter: TextAdapter<V>,
) : RecyclerView.Adapter<EditableTextMapAdapter.Holder>() {
    class Holder(val composeView: ComposeView) : RecyclerView.ViewHolder(composeView)

    fun addElement(key: String, value: String) {
        val keyValue = keyAdapter.to(key)
        val valueValue = valueAdapter.to(value)

        values.add(keyValue to valueValue)
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
            EditableTextMapItem(
                key = keyAdapter.from(current.first),
                value = valueAdapter.from(current.second),
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
