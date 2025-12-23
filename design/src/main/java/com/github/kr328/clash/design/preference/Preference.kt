package com.github.kr328.clash.design.preference

import android.view.View

fun interface OnChangedListener {
    fun onChanged()
}

interface Preference {
    val view: View

    var enabled: Boolean
        get() = view.isEnabled
        set(value) {
            view.isEnabled = value
            // 不在根布局上设置clickable和focusable，避免拦截点击事件
            // 让Preference框架通过setOnClickListener处理点击
            view.alpha = if (value) 1.0f else 0.33f
        }
}