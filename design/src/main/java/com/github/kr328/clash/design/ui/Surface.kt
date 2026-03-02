package com.github.kr328.clash.design.ui

import androidx.databinding.BaseObservable

class Surface : BaseObservable() {
    var insets: Insets = Insets.EMPTY
        set(value) {
            field = value
            notifyChange()
        }
}
