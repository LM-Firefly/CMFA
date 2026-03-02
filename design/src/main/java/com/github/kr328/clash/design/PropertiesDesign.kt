package com.github.kr328.clash.design

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.github.kr328.clash.core.model.FetchStatus
import com.github.kr328.clash.design.compose.PropertiesScreen
import com.github.kr328.clash.design.dialog.requestModelTextInput
import com.github.kr328.clash.design.dialog.withModelProgressBar
import com.github.kr328.clash.design.dialog.requestConfirm
import com.github.kr328.clash.design.util.ValidatorAutoUpdateInterval
import com.github.kr328.clash.design.util.ValidatorHttpUrl
import com.github.kr328.clash.design.util.ValidatorNotBlank
import com.github.kr328.clash.design.util.getHtml
import com.github.kr328.clash.service.model.Profile
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import java.util.*

class PropertiesDesign(context: Context) : Design<PropertiesDesign.Request>(context) {
    enum class Request {
        BrowseFiles,
        Commit
    }

    private var profileState by mutableStateOf(
        Profile(
            uuid = UUID.randomUUID(),
            name = "",
            type = Profile.Type.File,
            source = "",
            active = false,
            interval = 0,
            upload = 0,
            download = 0,
            total = 0,
            expire = 0,
            updatedAt = 0,
            imported = false,
            pending = false
        )
    )
    private var processingState by mutableStateOf(false)

    override val root: View = ComposeView(context).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
        setContent {
            MaterialTheme {
                val profile = profileState

                PropertiesScreen(
                    title = context.getString(R.string.properties),
                    tips = context.getHtml(R.string.tips_properties).toString(),
                    nameTitle = context.getString(R.string.name),
                    urlTitle = context.getString(R.string.url),
                    autoUpdateTitle = context.getString(R.string.auto_update),
                    disabledText = context.getString(R.string.disabled),
                    browseFilesTitle = context.getString(R.string.browse_files),
                    browseFilesSummary = context.getString(R.string.browse_configuration_providers),
                    name = profile.name,
                    source = profile.source,
                    intervalText = if (profile.interval == 0L) {
                        context.getString(R.string.disabled)
                    } else {
                        context.getString(R.string.format_minutes, profile.interval / 1000 / 60)
                    },
                    processing = processingState,
                    sourceEnabled = profile.type != Profile.Type.File,
                    intervalEnabled = profile.type != Profile.Type.File,
                    onBackClick = {
                        (context as? AppCompatActivity)?.onBackPressedDispatcher?.onBackPressed()
                    },
                    onInputName = { inputName() },
                    onInputUrl = { inputUrl() },
                    onInputInterval = { inputInterval() },
                    onBrowseFiles = { requestBrowseFiles() },
                    onSave = { requestCommit() }
                )
            }
        }
    }

    var profile: Profile = Profile(
        uuid = UUID.randomUUID(),
        name = "",
        type = Profile.Type.File,
        source = "",
        active = false,
        interval = 0,
        upload = 0,
        download = 0,
        total = 0,
        expire = 0,
        updatedAt = 0,
        imported = false,
        pending = false
    )
        get() = profileState
        set(value) {
            profileState = value
            field = value
        }

    var progressing: Boolean = false
        get() = processingState
        private set

    suspend fun requestExitWithoutSaving(): Boolean {
        return context.requestConfirm(
            title = context.getText(R.string.exit_without_save),
            message = context.getText(R.string.exit_without_save_warning)
        )
    }

    suspend fun withProcessing(executeTask: suspend (suspend (FetchStatus) -> Unit) -> Unit) {
        processingState = true
        try {
            context.withModelProgressBar {
                configure {
                    isIndeterminate = true
                    text = context.getString(R.string.initializing)
                }

                executeTask { status ->
                    configure {
                        isIndeterminate = status.max <= 0
                        max = status.max
                        progress = status.progress
                        text = when (status.action) {
                            FetchStatus.Action.FetchConfiguration -> {
                                context.getString(
                                    R.string.format_fetching_configuration,
                                    status.args.firstOrNull() ?: ""
                                )
                            }

                            FetchStatus.Action.FetchProviders -> {
                                context.getString(
                                    R.string.format_fetching_provider,
                                    status.args.firstOrNull() ?: ""
                                )
                            }

                            FetchStatus.Action.Verifying -> context.getString(R.string.verifying)
                        }
                    }
                }
            }
        } finally {
            processingState = false
        }
    }

    fun inputName() {
        launch {
            val name = context.requestModelTextInput(
                initial = profile.name,
                title = context.getText(R.string.name),
                hint = context.getText(R.string.profile_name),
                error = context.getText(R.string.should_not_be_blank),
                validator = ValidatorNotBlank
            )

            if (name != profile.name) {
                profile = profile.copy(name = name)
            }
        }
    }

    fun inputUrl() {
        if (profile.type == Profile.Type.File || profile.type == Profile.Type.External) {
            return
        }

        launch {
            val url = context.requestModelTextInput(
                initial = profile.source,
                title = context.getText(R.string.url),
                hint = context.getText(R.string.profile_url),
                error = context.getText(R.string.accept_http_content),
                validator = ValidatorHttpUrl
            )

            if (url != profile.source) {
                profile = profile.copy(source = url)
            }
        }
    }

    fun inputInterval() {
        if (profile.type == Profile.Type.File) {
            return
        }

        launch {
            var minutes = TimeUnit.MILLISECONDS.toMinutes(profile.interval)

            minutes = context.requestModelTextInput(
                initial = if (minutes == 0L) "" else minutes.toString(),
                title = context.getText(R.string.auto_update),
                hint = context.getText(R.string.auto_update_minutes),
                error = context.getText(R.string.at_least_15_minutes),
                validator = ValidatorAutoUpdateInterval
            ).toLongOrNull() ?: 0

            val interval = TimeUnit.MINUTES.toMillis(minutes)

            if (interval != profile.interval) {
                profile = profile.copy(interval = interval)
            }
        }
    }

    fun requestCommit() {
        requests.trySend(Request.Commit)
    }

    fun requestBrowseFiles() {
        requests.trySend(Request.BrowseFiles)
    }
}
