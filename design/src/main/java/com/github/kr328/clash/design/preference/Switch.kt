package com.github.kr328.clash.design.preference

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.github.kr328.clash.common.compat.getDrawableCompat
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
    // TODO: Implement switch preference UI
    val view = FrameLayout(context)
    
    val impl = object : SwitchPreference {
        override val view: View = view
        override var icon: Drawable? = if (icon != null) context.getDrawableCompat(icon) else null
        override var title: CharSequence? = if (title != null) context.getString(title) else null
        override var summary: CharSequence? = if (summary != null) context.getString(summary) else null
        override var listener: OnChangedListener? = null
        override var enabled: Boolean
            get() = view.isEnabled
            set(value) {
                view.isEnabled = value
                view.isFocusable = value
                view.isClickable = value
                view.alpha = if (value) 1.0f else 0.33f
            }
    }

    impl.configure()
    addElement(impl)
    return impl
}
