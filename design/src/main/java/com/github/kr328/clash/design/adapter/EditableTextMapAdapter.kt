package com.github.kr328.clash.design.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.github.kr328.clash.design.preference.TextAdapter

class EditableTextMapAdapter<K, V>(
    private val context: Context,
    val values: MutableList<Pair<K, V>>,
    private val keyAdapter: TextAdapter<K>,
    private val valueAdapter: TextAdapter<V>,
) : RecyclerView.Adapter<EditableTextMapAdapter.Holder>() {
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun addElement(key: String, value: String) {
        val keyValue = keyAdapter.to(key)
        val valueValue = valueAdapter.to(value)

        notifyItemInserted(values.size)
        values.add(keyValue to valueValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(FrameLayout(context))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // TODO: Implement editing UI for map items
    }

    override fun getItemCount(): Int {
        return values.size
    }
}


