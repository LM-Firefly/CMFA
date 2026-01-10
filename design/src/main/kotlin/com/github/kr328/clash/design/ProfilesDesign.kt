package com.github.kr328.clash.design

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import com.github.kr328.clash.design.dialog.ProfilesMenuDialog
import com.github.kr328.clash.design.ui.ProfilesScreen
import com.github.kr328.clash.design.ui.ToastDuration
import com.github.kr328.clash.design.util.*
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

    private val composeView: androidx.compose.ui.platform.ComposeView =
        androidx.compose.ui.platform.ComposeView(context).apply {
            setBackgroundColor(android.graphics.Color.TRANSPARENT)
        }

    private val insets = context.insets
    private var profiles by mutableStateOf<List<Profile>>(emptyList())
    private var isUpdatingAll by mutableStateOf(false)
    private var currentMenuProfile by mutableStateOf<Profile?>(null)

    override val root: View
        get() = composeView

    suspend fun patchProfiles(profiles: List<Profile>) {
        this.profiles = profiles
    }

    suspend fun requestSave(profile: Profile) {
        showToast(R.string.active_unsaved_tips, ToastDuration.Long) {
            setAction(R.string.edit) {
                requests.trySend(Request.Edit(profile))
            }
        }
    }

    fun updateElapsed() {
        // Recomposition is automatic with Compose
    }

    init {
        composeView.setContent {
            androidx.compose.material3.Surface {
                ProfilesScreen(
                    profiles = profiles,
                    isUpdatingAll = isUpdatingAll,
                    insets = insets,
                    onUpdateAll = { requestUpdateAll() },
                    onCreate = { requestCreate() },
                    onSelectProfile = { profile -> requestActive(profile) },
                    onShowMenu = { profile -> showMenu(profile) }
                )
                
                currentMenuProfile?.let { profile ->
                    ProfilesMenuDialog(
                        profile = profile,
                        onUpdate = {
                            requests.trySend(Request.Update(profile))
                            currentMenuProfile = null
                        },
                        onEdit = {
                            requests.trySend(Request.Edit(profile))
                            currentMenuProfile = null
                        },
                        onDuplicate = {
                            requests.trySend(Request.Duplicate(profile))
                            currentMenuProfile = null
                        },
                        onDelete = {
                            requests.trySend(Request.Delete(profile))
                            currentMenuProfile = null
                        },
                        onDismiss = {
                            currentMenuProfile = null
                        }
                    )
                }
            }
        }
    }

    private fun showMenu(profile: Profile) {
        currentMenuProfile = profile
    }

    fun requestUpdateAll() {
        isUpdatingAll = true
        requests.trySend(Request.UpdateAll)
    }

    fun finishUpdateAll() {
        isUpdatingAll = false
    }

    fun requestCreate() {
        requests.trySend(Request.Create)
    }

    private fun requestActive(profile: Profile) {
        requests.trySend(Request.Active(profile))
    }
}