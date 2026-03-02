package com.github.kr328.clash.design.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.github.kr328.clash.design.model.LogFile

class LogFileAdapter(
    private val context: Context,
    private val open: (LogFile) -> Unit,
) : RecyclerView.Adapter<LogFileAdapter.Holder>() {
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    var logs: List<LogFile> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(FrameLayout(context))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // TODO: Implement log file display UI
    }

    override fun getItemCount(): Int {
        return logs.size
    }
}


