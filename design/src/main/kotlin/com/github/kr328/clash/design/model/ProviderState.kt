package com.github.kr328.clash.design.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.github.kr328.clash.core.model.Provider
import com.github.kr328.clash.design.BR

class ProviderState(
    provider: Provider,
    updatedAt: Long,
    updating: Boolean,
) : BaseObservable() {
    var provider: Provider = provider
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.provider)
        }

    var updatedAt: Long = updatedAt
        @Bindable get
        set(value) {
            field = value

            notifyPropertyChanged(BR.updatedAt)
        }

    var updating: Boolean = updating
        @Bindable get
        set(value) {
            field = value

            notifyPropertyChanged(BR.updating)
        }
}