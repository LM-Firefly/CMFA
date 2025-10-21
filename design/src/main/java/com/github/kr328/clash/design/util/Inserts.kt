package com.github.kr328.clash.design.util

import android.view.View
import androidx.core.view.WindowInsetsCompat
import com.github.kr328.clash.design.ui.Insets

fun View.setOnInsertsChangedListener(adaptLandscape: Boolean = true, listener: (Insets) -> Unit) {
    setOnApplyWindowInsetsListener { v, ins ->
        val compat = WindowInsetsCompat.toWindowInsetsCompat(ins)
        val insets = compat.getInsets(WindowInsetsCompat.Type.systemBars())

    val isLtr = v.layoutDirection == View.LAYOUT_DIRECTION_LTR
        val rInsets = if (isLtr) {
            Insets(
                insets.left,
                insets.top,
                insets.right,
                insets.bottom,
            )
        } else {
            Insets(
                insets.right,
                insets.top,
                insets.left,
                insets.bottom,
            )
        }

        listener(if (adaptLandscape) rInsets.landscape(v.context) else rInsets)

        compat.toWindowInsets()!!
    }

    requestApplyInsets()
}
