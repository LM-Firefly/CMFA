package com.github.kr328.clash.design.preference

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.github.kr328.clash.common.compat.getDrawableCompat
import com.github.kr328.clash.design.R

interface ClickablePreference : Preference {
    var title: CharSequence

    var icon: Drawable?
    var summary: CharSequence?

    fun clicked(clicked: () -> Unit)
}

fun PreferenceScreen.clickable(
    @StringRes title: Int,
    @DrawableRes icon: Int? = null,
    @StringRes summary: Int? = null,
    configure: ClickablePreference.() -> Unit = {}
): ClickablePreference {
    var currentTitle by mutableStateOf<CharSequence>(context.getText(title))
    var currentIcon by mutableStateOf<Drawable?>(icon?.let { context.getDrawableCompat(it) })
    var currentSummary by mutableStateOf<CharSequence?>(summary?.let { context.getText(it) })
    var clickHandler by mutableStateOf<(() -> Unit)?>(null)
    var isEnabled by mutableStateOf(true)

    val composeView = ComposeView(context).apply {
        setContent {
            Card(
                onClick = { clickHandler?.invoke() },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .heightIn(min = dimensionResource(R.dimen.item_min_height)),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0f)
                ),
                shape = MaterialTheme.shapes.medium,
                enabled = isEnabled
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = dimensionResource(R.dimen.item_header_margin),
                            end = dimensionResource(R.dimen.item_tailing_margin),
                            top = dimensionResource(R.dimen.item_padding_vertical),
                            bottom = dimensionResource(R.dimen.item_padding_vertical)
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icon
                    if (currentIcon != null) {
                        Box(
                            modifier = Modifier
                                .size(dimensionResource(R.dimen.item_header_component_size))
                        ) {
                            currentIcon?.let { drawable ->
                                Image(
                                    bitmap = drawable.toBitmap().asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.item_header_margin)))
                    }

                    // Title and Summary
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = currentTitle.toString(),
                            style = MaterialTheme.typography.bodyLarge
                        )

                        if (currentSummary != null) {
                            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.item_text_margin)))
                            Text(
                                text = currentSummary.toString(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }

    val impl = object : ClickablePreference {
        override var icon: Drawable?
            get() = currentIcon
            set(value) {
                currentIcon = value
            }
        override var title: CharSequence
            get() = currentTitle
            set(value) {
                currentTitle = value
            }
        override var summary: CharSequence?
            get() = currentSummary
            set(value) {
                currentSummary = value
            }
        override val view: View
            get() = composeView
        override var enabled: Boolean
            get() = isEnabled
            set(value) {
                isEnabled = value
                composeView.isEnabled = value
            }

        override fun clicked(clicked: () -> Unit) {
            clickHandler = clicked
        }
    }

    impl.configure()

    addElement(impl)

    return impl
}