package com.github.kr328.clash.design.util

import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.github.kr328.clash.design.view.ActionLabel
import com.github.kr328.clash.design.view.LargeActionLabel

@BindingAdapter("android:minHeight")
fun bindMinHeight(view: View, value: Float) {
    view.minimumHeight = value.toInt()
}

@BindingAdapter("android:layout_marginHorizontal")
fun bindLayoutMarginHorizontal(view: View, margin: Float) {
    val params = view.layoutParams as? ViewGroup.MarginLayoutParams ?: return
    params.marginStart = margin.toInt()
    params.marginEnd = margin.toInt()
    view.layoutParams = params
}

@BindingAdapter("android:layout_marginVertical")
fun bindLayoutMarginVertical(view: View, margin: Float) {
    val params = view.layoutParams as? ViewGroup.MarginLayoutParams ?: return
    params.topMargin = margin.toInt()
    params.bottomMargin = margin.toInt()
    view.layoutParams = params
}

@BindingAdapter("android:layout_marginTop")
fun bindLayoutMarginTop(view: View, margin: Float) {
    val params = view.layoutParams as? ViewGroup.MarginLayoutParams ?: return
    params.topMargin = margin.toInt()
    view.layoutParams = params
}

@BindingAdapter("android:layout_marginBottom")
fun bindLayoutMarginBottom(view: View, margin: Float) {
    val params = view.layoutParams as? ViewGroup.MarginLayoutParams ?: return
    params.bottomMargin = margin.toInt()
    view.layoutParams = params
}

@BindingAdapter("android:layout_marginStart")
fun bindLayoutMarginStart(view: View, margin: Float) {
    val params = view.layoutParams as? ViewGroup.MarginLayoutParams ?: return
    params.marginStart = margin.toInt()
    view.layoutParams = params
}

@BindingAdapter("android:layout_marginEnd")
fun bindLayoutMarginEnd(view: View, margin: Float) {
    val params = view.layoutParams as? ViewGroup.MarginLayoutParams ?: return
    params.marginEnd = margin.toInt()
    view.layoutParams = params
}

@BindingAdapter("android:paddingHorizontal")
fun bindPaddingHorizontal(view: View, padding: Float) {
    view.setPadding(padding.toInt(), view.paddingTop, padding.toInt(), view.paddingBottom)
}

@BindingAdapter("android:paddingVertical")
fun bindPaddingVertical(view: View, padding: Float) {
    view.setPadding(view.paddingLeft, padding.toInt(), view.paddingRight, padding.toInt())
}

@BindingAdapter("android:onClick")
fun bindOnClickActionLabel(view: ActionLabel, listener: View.OnClickListener?) {
    view.setOnClickListener(listener)
}

@BindingAdapter("android:onClick")
fun bindOnClickLargeActionLabel(view: LargeActionLabel, listener: View.OnClickListener?) {
    view.setOnClickListener(listener)
}

@BindingAdapter("tint")
fun bindImageTint(view: ImageView, tint: ColorStateList?) {
    view.imageTintList = tint
}
