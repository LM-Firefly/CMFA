package com.github.kr328.clash.design

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.github.kr328.clash.design.compose.FileItemUi
import com.github.kr328.clash.design.compose.FilesScreen
import com.github.kr328.clash.design.dialog.requestModelTextInput
import com.github.kr328.clash.design.model.File
import com.github.kr328.clash.design.util.elapsedIntervalString
import com.github.kr328.clash.design.util.toBytesString
import com.github.kr328.clash.design.util.ValidatorFileName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FilesDesign(context: Context) : Design<FilesDesign.Request>(context) {
    sealed class Request {
        data class OpenFile(val file: File) : Request()
        data class OpenDirectory(val file: File) : Request()
        data class RenameFile(val file: File) : Request()
        data class DeleteFile(val file: File) : Request()
        data class ImportFile(val file: File?) : Request()
        data class ExportFile(val file: File) : Request()
        object PopStack : Request()
    }

    private var filesState by mutableStateOf<List<File>>(emptyList())
    private var currentInBaseDirState by mutableStateOf(false)
    private var configurationEditableState by mutableStateOf(false)
    private var elapsedTick by mutableLongStateOf(System.currentTimeMillis())

    override val root: View = ComposeView(context).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
        setContent {
            MaterialTheme {
                val items = filesState.map {
                    FileItemUi(
                        key = it.id,
                        name = it.name,
                        isDirectory = it.isDirectory,
                        size = it.size,
                        sizeText = if (it.isDirectory) null else it.size.toBytesString(),
                        modifiedText = if (it.isDirectory) null else {
                            (elapsedTick - it.lastModified).coerceAtLeast(0).elapsedIntervalString(context)
                        }
                    )
                }

                FilesScreen(
                    title = context.getString(R.string.files),
                    files = items,
                    currentInBaseDir = currentInBaseDirState,
                    configurationEditable = configurationEditableState,
                    onBackClick = { requests.trySend(Request.PopStack) },
                    onNewClick = { requests.trySend(Request.ImportFile(null)) },
                    onFileClick = { item ->
                        filesState.firstOrNull { it.id == item.key }?.let { requestOpen(it) }
                    },
                    onFileImportClick = { item ->
                        filesState.firstOrNull { it.id == item.key }?.let { requests.trySend(Request.ImportFile(it)) }
                    },
                    onFileExportClick = { item ->
                        filesState.firstOrNull { it.id == item.key }?.let { requests.trySend(Request.ExportFile(it)) }
                    },
                    onFileRenameClick = { item ->
                        filesState.firstOrNull { it.id == item.key }?.let { requests.trySend(Request.RenameFile(it)) }
                    },
                    onFileDeleteClick = { item ->
                        filesState.firstOrNull { it.id == item.key }?.let { requests.trySend(Request.DeleteFile(it)) }
                    }
                )
            }
        }
    }

    var configurationEditable: Boolean
        get() = configurationEditableState
        set(value) {
            configurationEditableState = value
        }

    suspend fun swapFiles(files: List<File>, currentInBaseDir: Boolean) {
        withContext(Dispatchers.Main) {
            filesState = files
            currentInBaseDirState = currentInBaseDir
        }
    }

    fun updateElapsed() {
        elapsedTick = System.currentTimeMillis()
    }

    suspend fun requestFileName(name: String): String {
        return context.requestModelTextInput(
            initial = name,
            title = context.getText(R.string.file_name),
            hint = context.getText(R.string.file_name),
            error = context.getText(R.string.invalid_file_name),
            validator = ValidatorFileName,
        )
    }

    private fun requestOpen(file: File) {
        if (file.isDirectory) {
            requests.trySend(Request.OpenDirectory(file))
        } else {
            requests.trySend(Request.OpenFile(file))
        }
    }
}
