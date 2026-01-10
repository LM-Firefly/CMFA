package com.github.kr328.clash.design.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import com.github.kr328.clash.design.R
import com.github.kr328.clash.design.util.resolveThemedColor

class ActivityBarLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr, defStyleRes) {
    init {
        setBackgroundColor(context.resolveThemedColor(com.google.android.material.R.attr.colorSurface))
        elevation = context.resources.getDimension(R.dimen.toolbar_elevation)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        super.dispatchTouchEvent(ev)

        return true
    }
}