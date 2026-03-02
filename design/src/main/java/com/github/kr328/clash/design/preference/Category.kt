package com.github.kr328.clash.design.preference

import android.view.View
import android.widget.FrameLayout
import androidx.annotation.StringRes

fun PreferenceScreen.category(
    @StringRes text: Int,
) {
    // TODO: Implement category preference UI
    val view = FrameLayout(context)
    
    addElement(object : Preference {
        override val view: View = view
        override var enabled: Boolean
            get() = view.isEnabled
            set(value) {
                view.isEnabled = value
            }
    })
}
