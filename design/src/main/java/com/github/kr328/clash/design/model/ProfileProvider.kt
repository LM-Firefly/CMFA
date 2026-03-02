package com.github.kr328.clash.design.model

import android.content.Context
import android.content.Intent
import androidx.compose.ui.graphics.vector.ImageVector
import com.github.kr328.clash.design.R
import com.github.kr328.clash.design.compose.IcBaselineAppsImageVector
import com.github.kr328.clash.design.compose.IcBaselineAttachFileImageVector
import com.github.kr328.clash.design.compose.IcBaselineCloudDownloadImageVector
import com.github.kr328.clash.design.compose.IcBaselineQrCodeScannerImageVector

sealed class ProfileProvider {
    class File(private val context: Context) : ProfileProvider() {
        override val name: String
            get() = context.getString(R.string.file)
        override val summary: String
            get() = context.getString(R.string.import_from_file)
        override val icon: ImageVector
            get() = IcBaselineAttachFileImageVector


    }

    class Url(private val context: Context) : ProfileProvider() {
        override val name: String
            get() = context.getString(R.string.url)
        override val summary: String
            get() = context.getString(R.string.import_from_url)
        override val icon: ImageVector
            get() = IcBaselineCloudDownloadImageVector
    }

    class QR(private val context: Context) : ProfileProvider() {
        override val name: String
            get() = context.getString(R.string.qr)
        override val summary: String
            get() = context.getString(R.string.import_from_qr)
        override val icon: ImageVector
            get() = IcBaselineQrCodeScannerImageVector
    }
    class External(
        override val name: String,
        override val summary: String,
        override val icon: ImageVector = IcBaselineAppsImageVector,
        val intent: Intent,
    ) : ProfileProvider()

    abstract val name: String
    abstract val summary: String
    abstract val icon: ImageVector
}
