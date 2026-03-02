package com.github.kr328.clash.design.preference

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.github.kr328.clash.common.compat.getDrawableCompat

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
    // TODO: Implement clickable preference UI
    val view = FrameLayout(context)
    
    val impl = object : ClickablePreference {
        override var icon: Drawable? = if (icon != null) context.getDrawableCompat(icon) else null
        override var title: CharSequence = context.getText(title)
        override var summary: CharSequence? = if (summary != null) context.getText(summary) else null
        override val view: View = view
        
        override fun clicked(clicked: () -> Unit) {
            view.setOnClickListener { clicked() }
        }
    }

    impl.configure()
    addElement(impl)
    return impl
}
