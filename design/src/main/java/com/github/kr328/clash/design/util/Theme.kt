package com.github.kr328.clash.design.util

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.util.StateSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import com.github.kr328.clash.common.compat.getDrawableCompat
import com.github.kr328.clash.design.R

interface ClickableScope {
    fun focusable(defaultValue: Boolean): Boolean
    fun clickable(defaultValue: Boolean): Boolean
    fun background(): Drawable?
    fun foreground(): Drawable?
}

val Context.selectableItemBackground: Drawable?
    get() {
        return getDrawableCompat(resolveThemedResourceId(android.R.attr.selectableItemBackground))
    }

fun Context.roundedSelectableItemBackground(radius: Float, baseColor: Int = Color.TRANSPARENT): Drawable {
    val radii = FloatArray(8) { radius }
    return roundedSelectableItemBackground(radii, baseColor)
}

fun Context.roundedSelectableItemBackground(cornerRadii: FloatArray, baseColor: Int = Color.TRANSPARENT): Drawable {
    val highlight = resolveThemedColor(androidx.appcompat.R.attr.colorControlHighlight)
    val radii = FloatArray(8) { index ->
        cornerRadii.getOrNull(index) ?: cornerRadii.lastOrNull() ?: 0f
    }
    val base = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        setCornerRadii(radii)
        setColor(baseColor)
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val mask = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setCornerRadii(radii)
            setColor(Color.WHITE)
        }

        return RippleDrawable(ColorStateList.valueOf(highlight), base, mask)
    }

    return StateListDrawable().apply {
        val pressed = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setCornerRadii(radii)
            setColor(highlight)
        }

        addState(intArrayOf(android.R.attr.state_pressed), pressed)
        addState(StateSet.WILD_CARD, base)
    }
}

fun Context.roundedSurfaceBackground(radius: Float): Drawable {
    return GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        cornerRadius = radius
        setColor(resolveThemedColor(com.google.android.material.R.attr.colorSurface))
    }
}

fun View.applyRoundedSelectableBackground(radius: Float, baseColor: Int = Color.TRANSPARENT) {
    val drawable = context.roundedSelectableItemBackground(radius, baseColor)
    background = drawable.constantState?.newDrawable()?.mutate() ?: drawable.mutate()
}

fun View.applyRoundedSelectableBackground(cornerRadii: FloatArray, baseColor: Int = Color.TRANSPARENT) {
    val drawable = context.roundedSelectableItemBackground(cornerRadii, baseColor)
    background = drawable.constantState?.newDrawable()?.mutate() ?: drawable.mutate()
}

fun Context.resolveClickableAttrs(
    attributeSet: AttributeSet?,
    @AttrRes defaultAttrRes: Int = 0,
    @StyleRes defaultStyleRes: Int = 0,
    block: ClickableScope.() -> Unit,
) {
    theme.obtainStyledAttributes(
        attributeSet,
        R.styleable.Clickable,
        defaultAttrRes,
        defaultStyleRes
    ).apply {
        val impl = object : ClickableScope {
            override fun focusable(defaultValue: Boolean): Boolean {
                return getBoolean(R.styleable.Clickable_android_focusable, defaultValue)
            }

            override fun clickable(defaultValue: Boolean): Boolean {
                return getBoolean(R.styleable.Clickable_android_clickable, defaultValue)
            }

            override fun background(): Drawable? {
                return getDrawable(R.styleable.Clickable_android_background)
            }

            override fun foreground(): Drawable? {
                return getDrawable(R.styleable.Clickable_android_focusable)
            }

        }

        impl.apply(block)

        recycle()
    }
}

fun Context.resolveThemedColor(@AttrRes resId: Int): Int {
    return TypedValue().apply {
        theme.resolveAttribute(resId, this, true)
    }.data
}

fun Context.resolveThemedBoolean(@AttrRes resId: Int): Boolean {
    return TypedValue().apply {
        theme.resolveAttribute(resId, this, true)
    }.data != 0
}

fun Context.resolveThemedResourceId(@AttrRes resId: Int): Int {
    return TypedValue().apply {
        theme.resolveAttribute(resId, this, true)
    }.resourceId
}
