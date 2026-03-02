package com.github.kr328.clash.design.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.github.kr328.clash.core.model.LogMessage

class LogMessageAdapter(
    private val context: Context,
    private val copy: (LogMessage) -> Unit,
) : RecyclerView.Adapter<LogMessageAdapter.Holder>() {
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    var messages: List<LogMessage> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(FrameLayout(context))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // TODO: Implement log message display UI
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}


