package com.github.kr328.clash.design.model

import androidx.databinding.BaseObservable
import com.github.kr328.clash.core.model.Provider

class ProviderState(
    provider: Provider,
    updatedAt: Long,
    updating: Boolean,
) : BaseObservable() {
    var provider: Provider = provider
        set(value) {
            field = value
            notifyChange()
        }
    var updatedAt: Long = updatedAt
        set(value) {
            field = value
            notifyChange()
        }

    var updating: Boolean = updating
        set(value) {
            field = value
            notifyChange()
        }
}
