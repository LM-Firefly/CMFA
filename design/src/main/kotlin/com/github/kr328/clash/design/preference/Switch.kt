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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.github.kr328.clash.common.compat.getDrawableCompat
import com.github.kr328.clash.design.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.reflect.KMutableProperty0

interface SwitchPreference : Preference {
    var icon: Drawable?
    var title: CharSequence?
    var summary: CharSequence?
    var listener: OnChangedListener?
}

fun PreferenceScreen.switch(
    value: KMutableProperty0<Boolean>,
    @DrawableRes icon: Int? = null,
    @StringRes title: Int? = null,
    @StringRes summary: Int? = null,
    configure: SwitchPreference.() -> Unit = {},
): SwitchPreference {
    var currentIcon by mutableStateOf<Drawable?>(icon?.let { context.getDrawableCompat(it) })
    var currentTitle by mutableStateOf<CharSequence?>(title?.let { context.getString(it) })
    var currentSummary by mutableStateOf<CharSequence?>(summary?.let { context.getString(it) })
    var isChecked by mutableStateOf(false)
    var isEnabled by mutableStateOf(true)
    var changeListener by mutableStateOf<OnChangedListener?>(null)

    val composeView = ComposeView(context).apply {
        setContent {
            Card(
                onClick = {
                    if (isEnabled) {
                        isChecked = !isChecked
                        launch(Dispatchers.Main) {
                            withContext(Dispatchers.IO) {
                                value.set(isChecked)
                            }
                            changeListener?.onChanged()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = dimensionResource(R.dimen.item_min_height))
                    .alpha(if (isEnabled) 1.0f else 0.33f),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0f)
                ),
                shape = MaterialTheme.shapes.medium,
                enabled = isEnabled
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = dimensionResource(R.dimen.item_tailing_margin)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icon
                    if (currentIcon != null) {
                        Box(
                            modifier = Modifier
                                .size(dimensionResource(R.dimen.item_header_component_size))
                                .padding(start = dimensionResource(R.dimen.item_header_margin))
                        ) {
                            currentIcon?.let { drawable ->
                                Image(
                                    bitmap = drawable.toBitmap().asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }

                    // Text content
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = dimensionResource(R.dimen.item_header_margin)),
                        verticalArrangement = Arrangement.Center
                    ) {
                        currentTitle?.let {
                            Text(
                                text = it.toString(),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        currentSummary?.let {
                            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.item_text_margin)))
                            Text(
                                text = it.toString(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Switch
                    Switch(
                        checked = isChecked,
                        onCheckedChange = null,
                        enabled = isEnabled
                    )
                }
            }
        }
    }

    val impl = object : SwitchPreference {
        override val view: View
            get() = composeView
        override var icon: Drawable?
            get() = currentIcon
            set(value) {
                currentIcon = value
            }
        override var title: CharSequence?
            get() = currentTitle
            set(value) {
                currentTitle = value
            }
        override var summary: CharSequence?
            get() = currentSummary
            set(value) {
                currentSummary = value
            }
        override var listener: OnChangedListener?
            get() = changeListener
            set(value) {
                changeListener = value
            }
        override var enabled: Boolean
            get() = isEnabled
            set(value) {
                isEnabled = value
            }
    }

    impl.configure()

    addElement(impl)

    launch(Dispatchers.Main) {
        val initialValue = withContext(Dispatchers.IO) {
            value.get()
        }
        isChecked = initialValue
    }

    return impl
}