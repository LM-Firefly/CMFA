package com.github.kr328.clash.design.dialog

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.github.kr328.clash.design.R
import com.github.kr328.clash.design.util.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun Context.requestModelTextInput(
    initial: String,
    title: CharSequence,
    hint: CharSequence? = null,
    error: CharSequence? = null,
    validator: Validator = ValidatorAcceptAll,
): String {
    return this.requestModelTextInput(initial, title, null, hint, error, validator)!!
}

suspend fun Context.requestModelTextInput(
    initial: String?,
    title: CharSequence,
    reset: CharSequence?,
    hint: CharSequence? = null,
    error: CharSequence? = null,
    validator: Validator = ValidatorAcceptAll,
): String? {
    return suspendCancellableCoroutine {
        var currentText by mutableStateOf(initial ?: "")
        var isValid by mutableStateOf(true)

        val composeView = ComposeView(this).apply {
            setContent {
                var textFieldValue by remember {
                    mutableStateOf(
                        TextFieldValue(
                            text = initial ?: "",
                            selection = TextRange(0, initial?.length ?: 0)
                        )
                    )
                }
                var showError by remember { mutableStateOf(false) }

                LaunchedEffect(textFieldValue.text) {
                    currentText = textFieldValue.text
                    isValid = validator(textFieldValue.text)
                    showError = !isValid && error != null
                }

                OutlinedTextField(
                    value = textFieldValue,
                    onValueChange = { textFieldValue = it },
                    label = hint?.let { { Text(it.toString()) } },
                    isError = showError,
                    supportingText = if (showError && error != null) {
                        { Text(error.toString()) }
                    } else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = dimensionResource(R.dimen.dialog_padding),
                            vertical = dimensionResource(R.dimen.dialog_padding)
                        )
                )
            }
        }

        val builder = MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setView(composeView)
            .setCancelable(true)
            .setPositiveButton(R.string.ok) { _, _ ->
                if (isValid)
                    it.resume(currentText)
                else
                    it.resume(initial)
            }
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .setOnDismissListener { _ ->
                if (!it.isCompleted)
                    it.resume(initial)
            }

        if (reset != null) {
            builder.setNeutralButton(reset) { _, _ ->
                it.resume(null)
            }
        }

        val dialog = builder.create()

        dialog.setOnShowListener {
            // Update positive button state based on validation
            dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).isEnabled = isValid
        }

        it.invokeOnCancellation {
            dialog.dismiss()
        }

        dialog.show()
    }
}