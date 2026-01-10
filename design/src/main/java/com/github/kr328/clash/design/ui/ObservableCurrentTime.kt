package com.github.kr328.clash.design.ui

import androidx.databinding.BaseObservable

class ObservableCurrentTime : BaseObservable() {
    var value: Long = System.currentTimeMillis()
        private set(value) {
            field = value
            notifyChange()
        }

    fun update() {
        value = System.currentTimeMillis()
    }
}