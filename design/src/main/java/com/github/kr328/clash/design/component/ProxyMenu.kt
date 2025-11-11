package com.github.kr328.clash.design.component

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.appcompat.widget.PopupMenu
import com.github.kr328.clash.core.model.ProxySort
import com.github.kr328.clash.core.model.TunnelState
import com.github.kr328.clash.design.ProxyDesign
import com.github.kr328.clash.design.R
import com.github.kr328.clash.design.store.UiStore
import com.github.kr328.clash.design.util.applyRoundedSelectableBackground
import com.github.kr328.clash.design.util.getPixels
import com.github.kr328.clash.design.util.roundedSurfaceBackground
import kotlinx.coroutines.channels.Channel

class ProxyMenu(
    context: Context,
    menuView: View,
    mode: TunnelState.Mode?,
    private val uiStore: UiStore,
    private val requests: Channel<ProxyDesign.Request>,
    private val updateConfig: () -> Unit,
) : PopupMenu.OnMenuItemClickListener {
    private val menu = PopupMenu(context, menuView)
    private val radius = context.getPixels(R.dimen.large_action_card_radius).toFloat()
    private val popupBackground = context.roundedSurfaceBackground(radius)

    fun show() {
        menu.setPopupWindowBackground()
        menu.show()
        configureListView()
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        item.isChecked = !item.isChecked

        when (item.itemId) {
            R.id.not_selectable -> {
                uiStore.proxyExcludeNotSelectable = item.isChecked

                requests.trySend(ProxyDesign.Request.ReLaunch)
            }
            R.id.single -> {
                uiStore.proxyLine = 1

                updateConfig()

                requests.trySend(ProxyDesign.Request.ReloadAll)
            }
            R.id.doubles -> {
                uiStore.proxyLine = 2

                updateConfig()

                requests.trySend(ProxyDesign.Request.ReloadAll)
            }
            R.id.multiple -> {
                uiStore.proxyLine = 3

                updateConfig()

                requests.trySend(ProxyDesign.Request.ReloadAll)
            }
            R.id.default_ -> {
                uiStore.proxySort = ProxySort.Default

                requests.trySend(ProxyDesign.Request.ReloadAll)
            }
            R.id.name -> {
                uiStore.proxySort = ProxySort.Title

                requests.trySend(ProxyDesign.Request.ReloadAll)
            }
            R.id.delay -> {
                uiStore.proxySort = ProxySort.Delay

                requests.trySend(ProxyDesign.Request.ReloadAll)
            }
            R.id.dont_modify -> {
                requests.trySend(ProxyDesign.Request.PatchMode(null))
            }
            R.id.direct_mode -> {
                requests.trySend(ProxyDesign.Request.PatchMode(TunnelState.Mode.Direct))
            }
            R.id.global_mode -> {
                requests.trySend(ProxyDesign.Request.PatchMode(TunnelState.Mode.Global))
            }
            R.id.rule_mode -> {
                requests.trySend(ProxyDesign.Request.PatchMode(TunnelState.Mode.Rule))
            }
            else -> return false
        }

        return true
    }

    init {
        menu.menuInflater.inflate(R.menu.menu_proxy, menu.menu)

        menu.menu.apply {
            findItem(R.id.not_selectable).isChecked = uiStore.proxyExcludeNotSelectable

            when (uiStore.proxyLine){
                1 -> findItem(R.id.single).isChecked = true
                2 -> findItem(R.id.doubles).isChecked = true
                3 -> findItem(R.id.multiple).isChecked = true
            }

            when (uiStore.proxySort) {
                ProxySort.Default -> findItem(R.id.default_).isChecked = true
                ProxySort.Title -> findItem(R.id.name).isChecked = true
                ProxySort.Delay -> findItem(R.id.delay).isChecked = true
            }

            when (mode) {
                null -> findItem(R.id.dont_modify).isChecked = true
                TunnelState.Mode.Direct -> findItem(R.id.direct_mode).isChecked = true
                TunnelState.Mode.Global -> findItem(R.id.global_mode).isChecked = true
                TunnelState.Mode.Rule -> findItem(R.id.rule_mode).isChecked = true
                else -> {}
            }
        }

        menu.setOnMenuItemClickListener(this)
    }

    private fun configureListView() {
        val listView = menu.listView() ?: return
        val background = popupBackground.constantState?.newDrawable()?.mutate() ?: popupBackground.mutate()
        listView.background = background
        listView.setPadding(
            listView.paddingLeft,
            listView.paddingTop,
            listView.paddingRight,
            listView.paddingBottom,
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listView.clipToOutline = true
        }
        listView.divider = null
        listView.dividerHeight = 0
        listView.selector = ColorDrawable(Color.TRANSPARENT)

        fun cornerRadii(index: Int, total: Int): FloatArray {
            val top = if (index == 0) radius else 0f
            val bottom = if (index == total - 1) radius else 0f
            return floatArrayOf(
                top, top,
                top, top,
                bottom, bottom,
                bottom, bottom,
            )
        }

        fun applyRipple(child: View?, index: Int, total: Int) {
            if (child == null) return
            child.isClickable = true
            child.isFocusable = true
            child.applyRoundedSelectableBackground(cornerRadii(index, total))
        }

        fun refreshChildBackgrounds() {
            val total = listView.adapter?.count ?: listView.childCount
            for (index in 0 until listView.childCount) {
                applyRipple(listView.getChildAt(index), index, total)
            }
        }

        refreshChildBackgrounds()

        listView.setOnHierarchyChangeListener(object : ViewGroup.OnHierarchyChangeListener {
            override fun onChildViewAdded(parent: View?, child: View?) {
                if (parent === listView) {
                    listView.post { refreshChildBackgrounds() }
                }
            }
            override fun onChildViewRemoved(parent: View?, child: View?) = Unit
        })
    }

    private fun PopupMenu.listView(): ListView? {
        return try {
            val field = PopupMenu::class.java.getDeclaredField("mPopup").apply {
                isAccessible = true
            }
            val helper = field.get(this)
            val method = helper.javaClass.getDeclaredMethod("getListView").apply {
                isAccessible = true
            }
            method.invoke(helper) as? ListView
        } catch (_: Throwable) {
            null
        }
    }

    private fun PopupMenu.setPopupWindowBackground() {
        try {
            val field = PopupMenu::class.java.getDeclaredField("mPopup").apply {
                isAccessible = true
            }
            val helper = field.get(this)
            val getPopup = helper.javaClass.getDeclaredMethod("getPopup").apply {
                isAccessible = true
            }
            val popup = getPopup.invoke(helper)
            val setBackground = popup.javaClass.getMethod("setBackgroundDrawable", Drawable::class.java)
            val drawable = popupBackground.constantState?.newDrawable()?.mutate() ?: popupBackground.mutate()
            setBackground.invoke(popup, drawable)
        } catch (_: Throwable) {
            // Ignore and rely on listView background fallback
        }
    }
}
