package com.github.kr328.clash.design.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.RecyclerView
import com.github.kr328.clash.design.model.File
import com.github.kr328.clash.design.ui.FileItem

class FileAdapter(
    private val context: Context,
    private val open: (File) -> Unit,
    private val more: (View, File) -> Unit,
) : RecyclerView.Adapter<FileAdapter.Holder>() {
    class Holder(val composeView: ComposeView) : RecyclerView.ViewHolder(composeView)

    private var currentTime by mutableStateOf(System.currentTimeMillis())

    var files: List<File> = emptyList()

    fun updateElapsed() {
        currentTime = System.currentTimeMillis()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        })
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val current = files[position]

        holder.composeView.setContent {
            FileItem(
                file = current,
                currentTime = currentTime,
                onClick = {
                    open(current)
                },
                onMenuClick = {
                    more(holder.composeView, current)
                }
            )
        }
    }

    override fun getItemCount(): Int {
        return files.size
    }
}
