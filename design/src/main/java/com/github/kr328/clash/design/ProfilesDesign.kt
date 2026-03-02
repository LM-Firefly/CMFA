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
import com.github.kr328.clash.design.compose.ProfileItemUi
import com.github.kr328.clash.design.compose.ProfileMenuDialog
import com.github.kr328.clash.design.compose.ProfilesScreen
import com.github.kr328.clash.design.ui.ToastDuration
import com.github.kr328.clash.design.util.elapsedIntervalString
import com.github.kr328.clash.service.model.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfilesDesign(context: Context) : Design<ProfilesDesign.Request>(context) {
    sealed class Request {
        object UpdateAll : Request()
        object Create : Request()
        data class Active(val profile: Profile) : Request()
        data class Update(val profile: Profile) : Request()
        data class Edit(val profile: Profile) : Request()
        data class Duplicate(val profile: Profile) : Request()
        data class Delete(val profile: Profile) : Request()
    }

    private var profilesState by mutableStateOf<List<Profile>>(emptyList())
    private var menuProfileState by mutableStateOf<Profile?>(null)
    private var updatableState by mutableStateOf(false)
    private var elapsedTick by mutableLongStateOf(System.currentTimeMillis())

    private var allUpdating: Boolean = false

    override val root: View = ComposeView(context).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
        setContent {
            MaterialTheme {
                val items = profilesState.map {
                    ProfileItemUi(
                        name = it.name,
                        type = when (it.type) {
                            Profile.Type.File -> "File"
                            Profile.Type.Url -> "URL"
                            Profile.Type.External -> "External"
                        },
                        isActive = it.active,
                        elapsedTime = (elapsedTick - it.updatedAt).coerceAtLeast(0).elapsedIntervalString(context),
                        isPending = it.pending,
                        isImported = it.imported,
                        isFileType = it.type == Profile.Type.File
                    )
                }

                ProfilesScreen(
                    title = context.getString(R.string.profile),
                    profiles = items,
                    onBackClick = {
                        (context as? AppCompatActivity)?.onBackPressedDispatcher?.onBackPressed()
                    },
                    onCreateClick = { requestCreate() },
                    onUpdateAllClick = { requestUpdateAll() },
                    onProfileClick = { index, _ ->
                        profilesState.getOrNull(index)?.let { requestActive(it) }
                    },
                    onProfileMenuClick = { index, _ ->
                        menuProfileState = profilesState.getOrNull(index)
                    },
                    onProfileUpdate = { index, _ ->
                        profilesState.getOrNull(index)?.let { requests.trySend(Request.Update(it)) }
                    },
                    onProfileEdit = { index, _ ->
                        profilesState.getOrNull(index)?.let { requests.trySend(Request.Edit(it)) }
                    },
                    onProfileDuplicate = { index, _ ->
                        profilesState.getOrNull(index)?.let { requests.trySend(Request.Duplicate(it)) }
                    },
                    onProfileDelete = { index, _ ->
                        profilesState.getOrNull(index)?.let { requests.trySend(Request.Delete(it)) }
                    },
                    showUpdateAll = updatableState
                )
            }
        }
    }

    suspend fun patchProfiles(profiles: List<Profile>) {
        val updatable = withContext(Dispatchers.Default) {
            profiles.any { it.imported && it.type != Profile.Type.File }
        }

        withContext(Dispatchers.Main) {
            profilesState = profiles
            updatableState = updatable
        }
    }

    suspend fun requestSave(profile: Profile) {
        showToast(R.string.active_unsaved_tips, ToastDuration.Long) {
            setAction(R.string.edit) {
                requests.trySend(Request.Edit(profile))
            }
        }
    }

    fun updateElapsed() {
        elapsedTick = System.currentTimeMillis()
    }

    fun requestUpdateAll() {
        allUpdating = true
        requests.trySend(Request.UpdateAll)
    }

    fun finishUpdateAll() {
        allUpdating = false
    }

    fun requestCreate() {
        requests.trySend(Request.Create)
    }

    private fun requestActive(profile: Profile) {
        requests.trySend(Request.Active(profile))
    }
}
