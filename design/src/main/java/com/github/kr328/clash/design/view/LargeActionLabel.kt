package com.github.kr328.clash.design.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import com.github.kr328.clash.design.databinding.ComponentLargeActionLabelBinding
import com.github.kr328.clash.design.R
import com.github.kr328.clash.design.util.getPixels
import com.github.kr328.clash.design.util.layoutInflater
import com.github.kr328.clash.design.util.resolveThemedColor
import com.github.kr328.clash.design.util.selectableItemBackground
import com.google.android.material.card.MaterialCardView

class LargeActionLabel @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : MaterialCardView(context, attributeSet, defStyleAttr) {
    private val binding = ComponentLargeActionLabelBinding
        .inflate(context.layoutInflater, this, true)

    var icon: Drawable?
        get() = binding.icon
        set(value) {
            binding.icon = value
        }

    var iconTint: ColorStateList?
        get() = binding.iconTint
        set(value) {
            binding.iconTint = value
        }

    var text: CharSequence?
        get() = binding.text
        set(value) {
            binding.text = value
        }

    var subtext: CharSequence?
        get() = binding.subtext
        set(value) {
            binding.subtext = value
        }

    init {
        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.LargeActionLabel,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                icon = getDrawable(R.styleable.LargeActionLabel_icon)
                iconTint = getColorStateList(R.styleable.LargeActionLabel_iconTint)
                    ?: ColorStateList.valueOf(context.resolveThemedColor(androidx.appcompat.R.attr.colorPrimary))
                text = getString(R.styleable.LargeActionLabel_text)
                subtext = getString(R.styleable.LargeActionLabel_subtext)
            } finally {
                recycle()
            }
        }

        isClickable = true
        isFocusable = true
        foreground = context.selectableItemBackground
        minimumHeight = context.getPixels(R.dimen.large_action_card_min_height)
        radius = context.getPixels(R.dimen.large_action_card_radius).toFloat()
        cardElevation = context.getPixels(R.dimen.large_action_card_elevation).toFloat()
        setCardBackgroundColor(context.resolveThemedColor(com.google.android.material.R.attr.colorSurface))
    }
}