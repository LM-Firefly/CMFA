package com.github.kr328.clash.design.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.github.kr328.clash.design.preference.TextAdapter

class EditableTextListAdapter<T>(
    private val context: Context,
    val values: MutableList<T>,
    private val adapter: TextAdapter<T>,
) : RecyclerView.Adapter<EditableTextListAdapter.Holder>() {
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun addElement(text: String) {
        val value = adapter.to(text)
        notifyItemInserted(values.size)
        values.add(value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(FrameLayout(context))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // TODO: Implement editable text list binding
    }

    override fun getItemCount(): Int {
        return values.size
    }
}


