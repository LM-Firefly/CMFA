package com.github.kr328.clash.design.preference

import android.view.View
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.github.kr328.clash.design.R
import com.github.kr328.clash.design.util.getHtml

interface TipsPreference : Preference {
    var text: CharSequence?
}

fun PreferenceScreen.tips(
    @StringRes text: Int,
    configure: TipsPreference.() -> Unit = {},
): TipsPreference {
    var currentText by mutableStateOf<CharSequence?>(context.getHtml(text))
    var isEnabled by mutableStateOf(true)

    val composeView = ComposeView(context).apply {
        setContent {
            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(vertical = dimensionResource(R.dimen.item_padding_vertical)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_outline_info),
                    contentDescription = null,
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.tips_icon_size))
                        .padding(horizontal = dimensionResource(R.dimen.tips_icon_margin))
                )

                currentText?.let {
                    Text(
                        text = it.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(end = dimensionResource(R.dimen.item_tailing_margin))
                    )
                }
            }
        }
    }

    val impl = object : TipsPreference {
        override var text: CharSequence?
            get() = currentText
            set(value) {
                currentText = value
            }
        override val view: View
            get() = composeView
        override var enabled: Boolean
            get() = isEnabled
            set(value) {
                isEnabled = value
                composeView.isEnabled = value
            }
    }

    impl.configure()

    addElement(impl)

    return impl
}