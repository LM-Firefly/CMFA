package com.github.kr328.clash.design.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.github.kr328.clash.design.R

class ActionTextField @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr, defStyleRes) {
    private var _icon by mutableStateOf<Drawable?>(null)
    private var _title by mutableStateOf<CharSequence?>(null)
    private var _text by mutableStateOf<CharSequence?>(null)
    private var _placeholder by mutableStateOf<CharSequence?>(null)
    private var _enabled by mutableStateOf(true)
    private var _clickListener by mutableStateOf<OnClickListener?>(null)

    private val composeView = ComposeView(context)

    var icon: Drawable?
        get() = _icon
        set(value) {
            _icon = value
        }

    var title: CharSequence?
        get() = _title
        set(value) {
            _title = value
        }

    var text: CharSequence?
        get() = _text
        set(value) {
            _text = if (_enabled) value else context.getText(R.string.unavailable)
        }

    var placeholder: CharSequence?
        get() = _placeholder
        set(value) {
            _placeholder = value
        }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        _enabled = enabled
        text = _text
    }

    override fun setOnClickListener(l: OnClickListener?) {
        _clickListener = l
    }

    private fun updateContent() {
        composeView.setContent {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = dimensionResource(R.dimen.item_min_height))
                    .alpha(if (_enabled) 1.0f else 0.33f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clickable(enabled = _enabled) {
                            _clickListener?.onClick(this@ActionTextField)
                        }
                        .padding(vertical = dimensionResource(R.dimen.item_padding_vertical)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icon
                    _icon?.let { drawable ->
                        Box(
                            modifier = Modifier
                                .size(dimensionResource(R.dimen.item_header_component_size))
                                .padding(start = dimensionResource(R.dimen.item_header_margin))
                        ) {
                            Image(
                                bitmap = drawable.toBitmap().asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    // Content
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                start = dimensionResource(R.dimen.item_header_margin),
                                end = dimensionResource(R.dimen.item_tailing_margin)
                            )
                    ) {
                        _title?.let {
                            Text(
                                text = it.toString(),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }

                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.item_text_margin)))

                        Text(
                            text = _text?.toString() ?: _placeholder?.toString() ?: "",
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (_text != null) MaterialTheme.colorScheme.onSurface
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Edit icon
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = context.getString(R.string.edit),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.item_tailing_component_size))
                            .padding(
                                start = dimensionResource(R.dimen.toolbar_image_action_padding),
                                top = dimensionResource(R.dimen.toolbar_image_action_padding),
                                end = dimensionResource(R.dimen.toolbar_image_action_padding) + dimensionResource(R.dimen.item_tailing_margin),
                                bottom = dimensionResource(R.dimen.toolbar_image_action_padding)
                            )
                    )
                }

                // Divider
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier.padding(start = dimensionResource(R.dimen.item_header_margin))
                )
            }
        }
    }

    init {
        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.ActionTextField,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                _enabled = getBoolean(R.styleable.ActionTextField_enabled, true)
                _icon = getDrawable(R.styleable.ActionTextField_icon)
                _title = getString(R.styleable.ActionTextField_title)
                _text = getString(R.styleable.ActionTextField_text)
                _placeholder = getString(R.styleable.ActionTextField_placeholder)
            } finally {
                recycle()
            }
        }

        addView(composeView)
        updateContent()
    }
}