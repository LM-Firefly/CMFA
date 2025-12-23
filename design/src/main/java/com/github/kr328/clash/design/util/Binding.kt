package com.github.kr328.clash.design.util

import android.view.View
import androidx.databinding.BindingAdapter
import com.github.kr328.clash.design.view.ActionLabel
import com.github.kr328.clash.design.view.LargeActionLabel

@BindingAdapter("android:minHeight")
fun bindMinHeight(view: View, value: Float) {
    view.minimumHeight = value.toInt()
}

@BindingAdapter("android:onClick")
fun bindOnClickActionLabel(view: ActionLabel, listener: View.OnClickListener?) {
    view.setOnClickListener(listener)
}

@BindingAdapter("android:onClick")
fun bindOnClickLargeActionLabel(view: LargeActionLabel, listener: View.OnClickListener?) {
    view.setOnClickListener(listener)
}
