package com.github.kr328.clash.design.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.github.kr328.clash.design.R
import com.github.kr328.clash.design.util.getPixels
import com.github.kr328.clash.design.util.resolveThemedColor
import com.github.kr328.clash.design.util.selectableItemBackground
import com.google.android.material.card.MaterialCardView

class LargeActionLabel @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : MaterialCardView(context, attributeSet, defStyleAttr) {
    private var _icon by mutableStateOf<Drawable?>(null)
    private var _iconTint by mutableStateOf<ColorStateList?>(null)
    private var _text by mutableStateOf<CharSequence?>(null)
    private var _subtext by mutableStateOf<CharSequence?>(null)

    var icon: Drawable?
        get() = _icon
        set(value) {
            _icon = value
        }

    var iconTint: ColorStateList?
        get() = _iconTint
        set(value) {
            _iconTint = value
        }

    var text: CharSequence?
        get() = _text
        set(value) {
            _text = value
        }

    var subtext: CharSequence?
        get() = _subtext
        set(value) {
            _subtext = value
        }

    init {
        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.LargeActionLabel,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                _icon = getDrawable(R.styleable.LargeActionLabel_icon)
                _iconTint = getColorStateList(R.styleable.LargeActionLabel_iconTint)
                    ?: ColorStateList.valueOf(context.resolveThemedColor(androidx.appcompat.R.attr.colorPrimary))
                _text = getString(R.styleable.LargeActionLabel_text)
                _subtext = getString(R.styleable.LargeActionLabel_subtext)
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

        val composeView = ComposeView(context).apply {
            setContent {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = dimensionResource(R.dimen.large_action_card_min_height))
                        .padding(
                            vertical = dimensionResource(R.dimen.large_item_padding_vertical),
                            horizontal = 20.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icon
                    _icon?.let { drawable ->
                        val tintColor = _iconTint?.defaultColor?.let { Color(it) }
                        Image(
                            bitmap = drawable.toBitmap().asImageBitmap(),
                            contentDescription = null,
                            colorFilter = tintColor?.let { ColorFilter.tint(it) },
                            modifier = Modifier
                                .size(dimensionResource(R.dimen.large_item_header_component_size))
                                .padding(horizontal = dimensionResource(R.dimen.large_item_header_margin_horizontal))
                        )
                    }

                    // Text content
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                start = dimensionResource(R.dimen.large_item_header_margin_horizontal),
                                end = dimensionResource(R.dimen.large_item_tailing_margin_horizontal)
                            ),
                        verticalArrangement = Arrangement.Center
                    ) {
                        _text?.let { textValue ->
                            Text(
                                text = textValue.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        _subtext?.let { subtextValue ->
                            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.large_item_text_margin)))
                            Text(
                                text = subtextValue.toString(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        addView(composeView)
    }
}