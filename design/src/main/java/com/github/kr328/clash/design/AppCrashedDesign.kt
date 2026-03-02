package com.github.kr328.clash.design
import android.content.Context
import android.view.View
import androidx.compose.ui.platform.ComposeView
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

class AppCrashedDesign(context: Context) : Design<Unit>(context) {
    override val root: View = ComposeView(context).apply { setContent { MaterialTheme { Text("App Crashed") } } }
    fun setAppLogs(logs: String) { }
}
