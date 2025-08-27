package com.github.kr328.clash.design.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.github.kr328.clash.design.R
import com.github.kr328.clash.design.util.getPixels
import com.github.kr328.clash.design.util.layoutInflater
import com.github.kr328.clash.design.util.resolveThemedColor
import com.github.kr328.clash.design.util.roundedSelectableItemBackground

class PopupListAdapter(
    private val context: Context,
    private val texts: List<CharSequence>,
    private val selected: Int,
) : BaseAdapter() {
    private val colorPrimary = context.resolveThemedColor(androidx.appcompat.R.attr.colorPrimary)
    private val colorOnPrimary = context.resolveThemedColor(com.google.android.material.R.attr.colorOnPrimary)
    private val colorControlNormal = context.resolveThemedColor(androidx.appcompat.R.attr.colorControlNormal)
    private val itemRadius = context.getPixels(R.dimen.large_action_card_radius).toFloat()
    private val topRadius = context.getPixels(R.dimen.bottom_sheet_corner_radius).toFloat()
    private val defaultRadii = FloatArray(8) { itemRadius }
    private val topRadii = floatArrayOf(
        topRadius, topRadius,
        topRadius, topRadius,
        itemRadius, itemRadius,
        itemRadius, itemRadius
    )
    private val selectedBaseColor = Color.argb(
        200,
        Color.red(colorPrimary),
        Color.green(colorPrimary),
        Color.blue(colorPrimary)
    )

    private fun Drawable.copyDrawable(): Drawable {
        return constantState?.newDrawable()?.mutate() ?: mutate()
    }

    override fun getCount(): Int {
        return texts.size
    }

    override fun getItem(position: Int): Any {
        return texts[position]
    }

    override fun getItemId(position: Int): Long {
        return texts[position].hashCode().toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: context.layoutInflater
            .inflate(android.R.layout.simple_list_item_1, parent, false)

        val text: TextView = view.findViewById(android.R.id.text1)

        text.text = texts[position]

        val radii = if (position == 0) topRadii else defaultRadii
        val baseColor = if (position == selected) selectedBaseColor else Color.TRANSPARENT
        val background = context.roundedSelectableItemBackground(radii, baseColor)

        text.background = background.copyDrawable()
        text.setTextColor(if (position == selected) colorOnPrimary else colorControlNormal)

        return view
    }
}