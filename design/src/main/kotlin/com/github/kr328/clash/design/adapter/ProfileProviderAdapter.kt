package com.github.kr328.clash.design.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.RecyclerView
import com.github.kr328.clash.design.model.ProfileProvider
import com.github.kr328.clash.design.ui.ProfileProviderItem

class ProfileProviderAdapter(
    private val context: Context,
    private val select: (ProfileProvider) -> Unit,
    private val detail: (ProfileProvider) -> Boolean,
) : RecyclerView.Adapter<ProfileProviderAdapter.Holder>() {
    class Holder(val composeView: ComposeView) : RecyclerView.ViewHolder(composeView)

    var providers: List<ProfileProvider> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        })
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val current = providers[position]

        holder.composeView.setContent {
            ProfileProviderItem(
                provider = current,
                onClick = {
                    select(current)
                },
                onLongClick = {
                    detail(current)
                }
            )
        }
    }

    override fun getItemCount(): Int {
        return providers.size
    }
}
