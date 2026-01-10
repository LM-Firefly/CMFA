package com.github.kr328.clash.design.preference

import android.view.View
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.github.kr328.clash.design.R

fun PreferenceScreen.category(
    @StringRes text: Int,
) {
    val composeView = ComposeView(context).apply {
        setContent {
            Text(
                text = context.getString(text),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(R.dimen.item_header_margin),
                        top = dimensionResource(R.dimen.item_padding_vertical),
                        end = dimensionResource(R.dimen.item_tailing_margin),
                        bottom = dimensionResource(R.dimen.item_padding_vertical)
                    )
            )
        }
    }

    var isEnabled by mutableStateOf(true)

    addElement(object : Preference {
        override val view: View
            get() = composeView
        override var enabled: Boolean
            get() = isEnabled
            set(value) {
                isEnabled = value
                composeView.isEnabled = value
            }
    })
}