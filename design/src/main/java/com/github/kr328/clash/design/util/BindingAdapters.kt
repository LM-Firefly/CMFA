package com.github.kr328.clash.design.util

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.RadioButton
import androidx.databinding.BindingAdapter
import com.google.android.material.radiobutton.MaterialRadioButton
import androidx.core.content.ContextCompat
import com.github.kr328.clash.design.R

/**
 * Custom binding adapters for Data Binding expressions
 * to handle type conversions and compatibility with AGP 8.13+
 */
object BindingAdapters {

    // region backgroundCompat
    @JvmStatic
    @BindingAdapter("backgroundDrawable")
    fun setBackgroundDrawable(view: View, drawable: Drawable?) {
        view.background = drawable
    }
    // endregion

    /**
     * Set checked state for RadioButton
     */
    // region checkedCompat
    @JvmStatic
    @BindingAdapter("checkedCompat")
    fun setChecked(view: RadioButton, checked: Boolean) { view.isChecked = checked }

    /**
     * Set checked state for MaterialRadioButton
     */
    @JvmStatic
    @BindingAdapter("checkedCompat")
    fun setChecked(view: MaterialRadioButton, checked: Boolean) { view.isChecked = checked }

    /**
     * Set checked state for CheckBox
     */
    @JvmStatic
    @BindingAdapter("checkedCompat")
    fun setChecked(view: CheckBox, checked: Boolean) { view.isChecked = checked }
    // endregion

    /**
     * Set minimum height with float calculation support
     */
    // region minHeightCompat
    @JvmStatic
    @BindingAdapter("minHeightCompat")
    fun setMinHeight(view: View, height: Float) { view.minimumHeight = height.toInt() }

    /**
     * Set minimum height with int value
     */
    @JvmStatic
    @BindingAdapter("minHeightCompat")
    fun setMinHeight(view: View, height: Int) { view.minimumHeight = height }
    // endregion

    /**
     * Set progress for ProgressBar with float to int conversion
     */
    // region progressCompat
    @JvmStatic
    @BindingAdapter("progressCompat")
    fun setProgress(view: ProgressBar, progress: Float) { view.progress = progress.toInt() }

    /**
     * Set progress for ProgressBar with int value
     */
    @JvmStatic
    @BindingAdapter("progressCompat")
    fun setProgress(view: ProgressBar, progress: Int) { view.progress = progress }

    /**
     * Set progress for ProgressBar with long to int conversion
     */
    @JvmStatic
    @BindingAdapter("progressCompat")
    fun setProgress(view: ProgressBar, progress: Long) { view.progress = progress.toInt() }
    // endregion

    // region clickable/focusable compat
    @JvmStatic
    @BindingAdapter("clickableCompat")
    fun setClickable(view: View, value: Boolean) { view.isClickable = value }

    @JvmStatic
    @BindingAdapter("focusableCompat")
    fun setFocusable(view: View, value: Boolean) { view.isFocusable = value }
    // endregion

    // region visibilityCompat
    @JvmStatic
    @BindingAdapter("visibilityCompat")
    fun setVisibility(view: View, visibility: Int) { view.visibility = visibility }

    @JvmStatic
    @BindingAdapter("visibilityCompat")
    fun setVisibility(view: View, show: Boolean) { view.visibility = if (show) View.VISIBLE else View.GONE }
    // endregion

    // region padding compat (dimension int)
    @JvmStatic
    @BindingAdapter("paddingTopCompat")
    fun setPaddingTop(view: View, paddingTop: Int) { view.setPadding(view.paddingLeft, paddingTop, view.paddingRight, view.paddingBottom) }

    @JvmStatic
    @BindingAdapter("paddingBottomCompat")
    fun setPaddingBottom(view: View, paddingBottom: Int) { view.setPadding(view.paddingLeft, view.paddingTop, view.paddingRight, paddingBottom) }
    // endregion

    /**
     * Set file icon based on whether it's a directory
     */
    @JvmStatic
    @BindingAdapter("fileIcon")
    fun setFileIcon(view: View, isDirectory: Boolean) {
        val iconRes = if (isDirectory) {
            R.drawable.ic_outline_folder
        } else {
            R.drawable.ic_outline_article
        }
        view.background = ContextCompat.getDrawable(view.context, iconRes)
    }
}
