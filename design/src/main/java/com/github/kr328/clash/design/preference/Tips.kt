package com.github.kr328.clash.design.preference

import android.view.View
import android.widget.FrameLayout
import androidx.annotation.StringRes

interface TipsPreference : Preference {
    var text: CharSequence?
}

fun PreferenceScreen.tips(
    @StringRes text: Int,
    configure: TipsPreference.() -> Unit = {},
): TipsPreference {
    // TODO: Implement tips preference UI
    val view = FrameLayout(context)
    
    val impl = object : TipsPreference {
        override var text: CharSequence? = context.getString(text)
        override val view: View = view
        override var enabled: Boolean
            get() = view.isEnabled
            set(value) {
                view.isEnabled = value
            }
    }

    impl.configure()
    addElement(impl)
    return impl
}
