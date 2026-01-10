package com.github.kr328.clash.design

import android.app.Dialog
import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.github.kr328.clash.core.model.TunnelState
import com.github.kr328.clash.core.util.trafficTotal
import com.github.kr328.clash.design.databinding.DesignAboutBinding
import com.github.kr328.clash.design.databinding.DesignMainBinding
import com.github.kr328.clash.design.util.layoutInflater
import com.github.kr328.clash.design.util.resolveThemedColor
import com.github.kr328.clash.design.util.root
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainDesign(context: Context) : Design<MainDesign.Request>(context) {
    enum class Request {
        ToggleStatus,
        OpenProxy,
        OpenProfiles,
        OpenProviders,
        OpenLogs,
        OpenSettings,
        OpenHelp,
        OpenAbout,
    }

    private val binding = DesignMainBinding
        .inflate(context.layoutInflater, context.root, false)

    override val root: View
        get() = binding.root

    suspend fun setProfileName(name: String?) {
        withContext(Dispatchers.Main) {
            binding.profileName = name
        }
    }

    suspend fun setClashRunning(running: Boolean) {
        withContext(Dispatchers.Main) {
            binding.clashRunning = running
        }
    }

    suspend fun setForwarded(value: Long) {
        withContext(Dispatchers.Main) {
            binding.forwarded = value.trafficTotal()
        }
    }

    suspend fun setMode(mode: TunnelState.Mode) {
        withContext(Dispatchers.Main) {
            binding.mode = when (mode) {
                TunnelState.Mode.Direct -> context.getString(R.string.direct_mode)
                TunnelState.Mode.Global -> context.getString(R.string.global_mode)
                TunnelState.Mode.Rule -> context.getString(R.string.rule_mode)
                else -> context.getString(R.string.rule_mode)
            }
        }
    }

    suspend fun setHasProviders(has: Boolean) {
        withContext(Dispatchers.Main) {
            binding.hasProviders = has
        }
    }

    suspend fun showAbout(versionName: String) {
        withContext(Dispatchers.Main) {
            val binding = DesignAboutBinding.inflate(context.layoutInflater).apply {
                this.versionName = versionName
            }

            Dialog(context).apply {
                setContentView(binding.root)
                window?.apply {
                    setLayout(
                        (context.resources.displayMetrics.widthPixels * 0.9).toInt(),
                        android.view.ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    setBackgroundDrawableResource(android.R.color.transparent)
                }
                show()
            }
        }
    }

    init {
        binding.self = this

        binding.colorClashStarted = context.resolveThemedColor(androidx.appcompat.R.attr.colorPrimary)
        binding.colorClashStopped = context.resolveThemedColor(R.attr.colorClashStopped)
    }

    fun request(request: Request) {
        requests.trySend(request)
    }

    fun onRequestToggleStatus(view: View) {
        request(Request.ToggleStatus)
    }

    fun onRequestOpenProxy(view: View) {
        request(Request.OpenProxy)
    }

    fun onRequestOpenProfiles(view: View) {
        request(Request.OpenProfiles)
    }

    fun onRequestOpenProviders(view: View) {
        request(Request.OpenProviders)
    }

    fun onRequestOpenLogs(view: View) {
        request(Request.OpenLogs)
    }

    fun onRequestOpenSettings(view: View) {
        request(Request.OpenSettings)
    }

    fun onRequestOpenHelp(view: View) {
        request(Request.OpenHelp)
    }

    fun onRequestOpenAbout(view: View) {
        request(Request.OpenAbout)
    }
}