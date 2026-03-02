package com.github.kr328.clash.design.dialog

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import com.github.kr328.clash.design.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

typealias Validator = (String) -> Boolean
val ValidatorAcceptAll: Validator = { true }

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
    return suspendCancellableCoroutine { continuation ->
        val textInputLayout = TextInputLayout(this).apply {
            hint?.let { setHint(it) }
            setPadding(48, 8, 48, 8)
            isErrorEnabled = error != null
        }

        val editText = TextInputEditText(textInputLayout.context).apply {
            setText(initial)
        }

        textInputLayout.addView(editText)

        val container = FrameLayout(this).apply {
            addView(textInputLayout)
        }

        val builder = MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setView(container)
            .setCancelable(true)
            .setPositiveButton(R.string.ok) { _, _ ->
                val text = editText.text?.toString() ?: ""
                if (validator(text)) {
                    continuation.resume(text)
                } else {
                    continuation.resume(initial)
                }
            }
            .setNegativeButton(R.string.cancel) { _, _ ->
                continuation.resume(initial)
            }
            .setOnDismissListener { _ ->
                if (!continuation.isCompleted) {
                    continuation.resume(initial)
                }
            }

        if (reset != null) {
            builder.setNeutralButton(reset) { _, _ ->
                continuation.resume(null)
            }
        }

        val dialog = builder.create()

        continuation.invokeOnCancellation {
            dialog.dismiss()
        }

        dialog.setOnShowListener {
            val okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

            // 添加实时验证
            if (error != null) {
                editText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                    override fun afterTextChanged(s: Editable?) {
                        val text = s?.toString() ?: ""
                        if (validator(text)) {
                            textInputLayout.error = null
                            okButton.isEnabled = true
                        } else {
                            textInputLayout.error = error.toString()
                            okButton.isEnabled = false
                        }
                    }
                })

                // 初始验证
                val initialText = editText.text?.toString() ?: ""
                if (!validator(initialText)) {
                    textInputLayout.error = error.toString()
                    okButton.isEnabled = false
                }
            }

            // 选中所有文本
            editText.selectAll()
            editText.requestFocus()
        }

        dialog.show()
    }
}

suspend fun Context.requestConfirm(
    title: CharSequence,
    message: CharSequence? = null,
): Boolean {
    return suspendCancellableCoroutine { continuation ->
        val builder = MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setCancelable(true)
            .setPositiveButton(R.string.ok) { _, _ ->
                continuation.resume(true)
            }
            .setNegativeButton(R.string.cancel) { _, _ ->
                continuation.resume(false)
            }
            .setOnDismissListener { _ ->
                if (!continuation.isCompleted) {
                    continuation.resume(false)
                }
            }

        if (message != null) {
            builder.setMessage(message)
        }

        val dialog = builder.create()

        continuation.invokeOnCancellation {
            dialog.dismiss()
        }

        dialog.show()
    }
}

fun Context.requestTextInput(
    initial: String,
    title: CharSequence,
    hint: CharSequence? = null,
    error: CharSequence? = null,
    validator: Validator = ValidatorAcceptAll,
    callback: (String) -> Unit
) {
    // TODO: Implement callback version
}
