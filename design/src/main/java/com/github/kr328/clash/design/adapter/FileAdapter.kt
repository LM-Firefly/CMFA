package com.github.kr328.clash.design.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.github.kr328.clash.design.model.File

class FileAdapter(
    private val context: Context,
    private val open: (File) -> Unit,
    private val more: (View, File) -> Unit,
) : RecyclerView.Adapter<FileAdapter.Holder>() {
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    var files: List<File> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(FrameLayout(context))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // TODO: Implement file binding UI
    }

    override fun getItemCount(): Int {
        return files.size
    }
}


