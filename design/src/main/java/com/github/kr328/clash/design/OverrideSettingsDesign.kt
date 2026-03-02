package com.github.kr328.clash.design

import android.content.Context
import android.view.View
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import com.github.kr328.clash.core.model.ConfigurationOverride
import com.github.kr328.clash.core.model.LogMessage
import com.github.kr328.clash.core.model.TunnelState
import com.github.kr328.clash.design.compose.ClickablePreference
import com.github.kr328.clash.design.compose.OverrideSettingsScreen
import com.github.kr328.clash.design.compose.PreferenceCategory
import com.github.kr328.clash.design.dialog.requestConfirm
import com.github.kr328.clash.design.dialog.requestModelListInput
import com.github.kr328.clash.design.dialog.requestModelTextInput
import kotlinx.coroutines.launch

class OverrideSettingsDesign(
    context: Context,
    private val configuration: ConfigurationOverride
) : Design<OverrideSettingsDesign.Request>(context) {
    enum class Request {
        ResetOverride
    }

    private data class Choice<T>(
        val label: String,
        val value: T,
    )

    private val renderTick = mutableStateOf(0)

    override val root: View = ComposeView(context).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
        setContent {
            renderTick.value
            MaterialTheme {
                val dnsEnabled = configuration.dns.enable != false

                OverrideSettingsScreen(
                    title = context.getString(R.string.override),
                    onBackClick = { (context as? android.app.Activity)?.finish() },
                    onClearClick = {
                        launch { requests.send(Request.ResetOverride) }
                    }
                ) {
                    PreferenceCategory(context.getString(R.string.general))
                    ClickablePreference(title = context.getString(R.string.http_port), summary = portSummary(configuration.httpPort), onClick = { launch { editPort(context.getText(R.string.http_port), configuration.httpPort) { configuration.httpPort = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.socks_port), summary = portSummary(configuration.socksPort), onClick = { launch { editPort(context.getText(R.string.socks_port), configuration.socksPort) { configuration.socksPort = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.redirect_port), summary = portSummary(configuration.redirectPort), onClick = { launch { editPort(context.getText(R.string.redirect_port), configuration.redirectPort) { configuration.redirectPort = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.tproxy_port), summary = portSummary(configuration.tproxyPort), onClick = { launch { editPort(context.getText(R.string.tproxy_port), configuration.tproxyPort) { configuration.tproxyPort = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.mixed_port), summary = portSummary(configuration.mixedPort), onClick = { launch { editPort(context.getText(R.string.mixed_port), configuration.mixedPort) { configuration.mixedPort = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.authentication), summary = listSummary(configuration.authentication), onClick = { launch { editStringList(context.getText(R.string.authentication), configuration.authentication) { configuration.authentication = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.allow_lan), summary = nullableBooleanSummary(configuration.allowLan), onClick = { launch { editNullableBoolean(context.getText(R.string.allow_lan), configuration.allowLan) { configuration.allowLan = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.ipv6), summary = nullableBooleanSummary(configuration.ipv6), onClick = { launch { editNullableBoolean(context.getText(R.string.ipv6), configuration.ipv6) { configuration.ipv6 = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.bind_address), summary = textSummary(configuration.bindAddress), onClick = { launch { editNullableString(context.getText(R.string.bind_address), configuration.bindAddress) { configuration.bindAddress = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.external_controller), summary = textSummary(configuration.externalController), onClick = { launch { editNullableString(context.getText(R.string.external_controller), configuration.externalController) { configuration.externalController = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.external_controller_tls), summary = textSummary(configuration.externalControllerTLS), onClick = { launch { editNullableString(context.getText(R.string.external_controller_tls), configuration.externalControllerTLS) { configuration.externalControllerTLS = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.allow_origins), summary = listSummary(configuration.externalControllerCors.allowOrigins), onClick = { launch { editStringList(context.getText(R.string.allow_origins), configuration.externalControllerCors.allowOrigins) { configuration.externalControllerCors.allowOrigins = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.allow_private_network), summary = nullableBooleanSummary(configuration.externalControllerCors.allowPrivateNetwork), onClick = { launch { editNullableBoolean(context.getText(R.string.allow_private_network), configuration.externalControllerCors.allowPrivateNetwork) { configuration.externalControllerCors.allowPrivateNetwork = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.secret), summary = textSummary(configuration.secret), onClick = { launch { editNullableString(context.getText(R.string.secret), configuration.secret) { configuration.secret = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.mode), summary = modeSummary(configuration.mode), onClick = { launch { editMode() } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.log_level), summary = logLevelSummary(configuration.logLevel), onClick = { launch { editLogLevel() } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.hosts), summary = mapSummary(configuration.hosts), onClick = { launch { editStringMap(context.getText(R.string.hosts), configuration.hosts) { configuration.hosts = it } } }, modifier = Modifier.padding(horizontal = 8.dp))

                    PreferenceCategory(context.getString(R.string.dns))
                    ClickablePreference(title = context.getString(R.string.strategy), summary = strategySummary(configuration.dns.enable), onClick = { launch { editDnsStrategy() } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.prefer_h3), summary = nullableBooleanSummary(configuration.dns.preferH3), enabled = dnsEnabled, onClick = { launch { editNullableBoolean(context.getText(R.string.prefer_h3), configuration.dns.preferH3) { configuration.dns.preferH3 = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.listen), summary = textSummary(configuration.dns.listen), enabled = dnsEnabled, onClick = { launch { editNullableString(context.getText(R.string.listen), configuration.dns.listen) { configuration.dns.listen = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.append_system_dns), summary = nullableBooleanSummary(configuration.app.appendSystemDns), enabled = dnsEnabled, onClick = { launch { editNullableBoolean(context.getText(R.string.append_system_dns), configuration.app.appendSystemDns) { configuration.app.appendSystemDns = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.ipv6), summary = nullableBooleanSummary(configuration.dns.ipv6), enabled = dnsEnabled, onClick = { launch { editNullableBoolean(context.getText(R.string.ipv6), configuration.dns.ipv6) { configuration.dns.ipv6 = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.use_hosts), summary = nullableBooleanSummary(configuration.dns.useHosts), enabled = dnsEnabled, onClick = { launch { editNullableBoolean(context.getText(R.string.use_hosts), configuration.dns.useHosts) { configuration.dns.useHosts = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.enhanced_mode), summary = enhancedModeSummary(configuration.dns.enhancedMode), enabled = dnsEnabled, onClick = { launch { editEnhancedMode() } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.name_server), summary = listSummary(configuration.dns.nameServer), enabled = dnsEnabled, onClick = { launch { editStringList(context.getText(R.string.name_server), configuration.dns.nameServer) { configuration.dns.nameServer = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.fallback), summary = listSummary(configuration.dns.fallback), enabled = dnsEnabled, onClick = { launch { editStringList(context.getText(R.string.fallback), configuration.dns.fallback) { configuration.dns.fallback = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.default_name_server), summary = listSummary(configuration.dns.defaultServer), enabled = dnsEnabled, onClick = { launch { editStringList(context.getText(R.string.default_name_server), configuration.dns.defaultServer) { configuration.dns.defaultServer = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.fakeip_filter), summary = listSummary(configuration.dns.fakeIpFilter), enabled = dnsEnabled, onClick = { launch { editStringList(context.getText(R.string.fakeip_filter), configuration.dns.fakeIpFilter) { configuration.dns.fakeIpFilter = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.fakeip_filter_mode), summary = fakeIpFilterModeSummary(configuration.dns.fakeIPFilterMode), enabled = dnsEnabled, onClick = { launch { editFakeIpFilterMode() } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.geoip_fallback), summary = nullableBooleanSummary(configuration.dns.fallbackFilter.geoIp), enabled = dnsEnabled, onClick = { launch { editNullableBoolean(context.getText(R.string.geoip_fallback), configuration.dns.fallbackFilter.geoIp) { configuration.dns.fallbackFilter.geoIp = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.geoip_fallback_code), summary = textSummary(configuration.dns.fallbackFilter.geoIpCode), enabled = dnsEnabled, onClick = { launch { editNullableString(context.getText(R.string.geoip_fallback_code), configuration.dns.fallbackFilter.geoIpCode) { configuration.dns.fallbackFilter.geoIpCode = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.domain_fallback), summary = listSummary(configuration.dns.fallbackFilter.domain), enabled = dnsEnabled, onClick = { launch { editStringList(context.getText(R.string.domain_fallback), configuration.dns.fallbackFilter.domain) { configuration.dns.fallbackFilter.domain = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.ipcidr_fallback), summary = listSummary(configuration.dns.fallbackFilter.ipcidr), enabled = dnsEnabled, onClick = { launch { editStringList(context.getText(R.string.ipcidr_fallback), configuration.dns.fallbackFilter.ipcidr) { configuration.dns.fallbackFilter.ipcidr = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.name_server_policy), summary = mapSummary(configuration.dns.nameserverPolicy), enabled = dnsEnabled, onClick = { launch { editStringMap(context.getText(R.string.name_server_policy), configuration.dns.nameserverPolicy) { configuration.dns.nameserverPolicy = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                }
            }
        }
    }

    suspend fun requestResetConfirm(): Boolean {
        return context.requestConfirm(
            title = context.getText(R.string.reset_override_settings),
            message = context.getText(R.string.reset_override_settings_message)
        )
    }

    private suspend fun editPort(title: CharSequence, current: Int?, set: (Int?) -> Unit) {
        val text = context.requestModelTextInput(
            initial = current?.toString(),
            title = title,
            reset = context.getText(R.string.dont_modify),
            hint = title
        )

        if (text == null) {
            set(null)
        } else {
            val parsed = text.trim().toIntOrNull()
            set(parsed)
        }
        bump()
    }

    private suspend fun editNullableString(title: CharSequence, current: String?, set: (String?) -> Unit) {
        val text = context.requestModelTextInput(
            initial = current,
            title = title,
            reset = context.getText(R.string.dont_modify),
            hint = title
        )
        set(text?.takeIf { it.isNotBlank() })
        bump()
    }

    private suspend fun editStringList(title: CharSequence, current: List<String>?, set: (List<String>?) -> Unit) {
        val text = context.requestModelTextInput(
            initial = current?.joinToString(", "),
            title = title,
            reset = context.getText(R.string.dont_modify),
            hint = "item1, item2"
        )

        if (text == null) {
            set(null)
        } else {
            val values = text.split(',', '\n').map { it.trim() }.filter { it.isNotEmpty() }
            set(values)
        }
        bump()
    }

    private suspend fun editStringMap(title: CharSequence, current: Map<String, String>?, set: (Map<String, String>?) -> Unit) {
        val initial = current?.entries?.joinToString("\n") { "${it.key}=${it.value}" }
        val text = context.requestModelTextInput(
            initial = initial,
            title = title,
            reset = context.getText(R.string.dont_modify),
            hint = "key=value"
        )

        if (text == null) {
            set(null)
        } else {
            val map = text.lines()
                .map { it.trim() }
                .filter { it.contains('=') }
                .associate {
                    val i = it.indexOf('=')
                    it.substring(0, i).trim() to it.substring(i + 1).trim()
                }
            set(map)
        }
        bump()
    }

    private suspend fun editNullableBoolean(title: CharSequence, current: Boolean?, set: (Boolean?) -> Unit) {
        val choices = listOf(
            Choice(context.getString(R.string.dont_modify), null),
            Choice(context.getString(R.string.enabled), true),
            Choice(context.getString(R.string.disabled), false),
        )
        val selected = context.requestModelListInput(choices, title) { it.label } ?: return
        set(selected.value)
        bump()
    }

    private suspend fun editMode() {
        val choices = listOf(
            Choice(context.getString(R.string.dont_modify), null),
            Choice(context.getString(R.string.direct_mode), TunnelState.Mode.Direct),
            Choice(context.getString(R.string.global_mode), TunnelState.Mode.Global),
            Choice(context.getString(R.string.rule_mode), TunnelState.Mode.Rule),
            Choice(context.getString(R.string.script_mode), TunnelState.Mode.Script),
        )
        val selected = context.requestModelListInput(choices, context.getText(R.string.mode)) { it.label } ?: return
        configuration.mode = selected.value
        bump()
    }

    private suspend fun editLogLevel() {
        val choices = listOf(
            Choice(context.getString(R.string.dont_modify), null),
            Choice(context.getString(R.string.info), LogMessage.Level.Info),
            Choice(context.getString(R.string.warning), LogMessage.Level.Warning),
            Choice(context.getString(R.string.error), LogMessage.Level.Error),
            Choice(context.getString(R.string.debug), LogMessage.Level.Debug),
            Choice(context.getString(R.string.silent), LogMessage.Level.Silent),
        )
        val selected = context.requestModelListInput(choices, context.getText(R.string.log_level)) { it.label } ?: return
        configuration.logLevel = selected.value
        bump()
    }

    private suspend fun editDnsStrategy() {
        val choices = listOf(
            Choice(context.getString(R.string.dont_modify), null),
            Choice(context.getString(R.string.force_enable), true),
            Choice(context.getString(R.string.use_built_in), false),
        )
        val selected = context.requestModelListInput(choices, context.getText(R.string.strategy)) { it.label } ?: return
        configuration.dns.enable = selected.value
        bump()
    }

    private suspend fun editEnhancedMode() {
        val choices = listOf(
            Choice(context.getString(R.string.dont_modify), null),
            Choice(context.getString(R.string.mapping), ConfigurationOverride.DnsEnhancedMode.Mapping),
            Choice(context.getString(R.string.fakeip), ConfigurationOverride.DnsEnhancedMode.FakeIp),
        )
        val selected = context.requestModelListInput(choices, context.getText(R.string.enhanced_mode)) { it.label } ?: return
        configuration.dns.enhancedMode = selected.value
        bump()
    }

    private suspend fun editFakeIpFilterMode() {
        val choices = listOf(
            Choice(context.getString(R.string.dont_modify), null),
            Choice(context.getString(R.string.blacklist), ConfigurationOverride.FilterMode.BlackList),
            Choice(context.getString(R.string.whitelist), ConfigurationOverride.FilterMode.WhiteList),
        )
        val selected = context.requestModelListInput(choices, context.getText(R.string.fakeip_filter_mode)) { it.label } ?: return
        configuration.dns.fakeIPFilterMode = selected.value
        bump()
    }

    private fun bump() {
        renderTick.value = renderTick.value + 1
    }

    private fun textSummary(value: String?): String =
        if (value.isNullOrBlank()) context.getString(R.string.dont_modify) else value

    private fun portSummary(value: Int?): String =
        value?.toString() ?: context.getString(R.string.dont_modify)

    private fun listSummary(value: List<String>?): String =
        when {
            value == null -> context.getString(R.string.dont_modify)
            value.isEmpty() -> context.getString(R.string.empty)
            else -> value.joinToString(", ")
        }

    private fun mapSummary(value: Map<String, String>?): String =
        when {
            value == null -> context.getString(R.string.dont_modify)
            value.isEmpty() -> context.getString(R.string.empty)
            else -> context.getString(R.string.format_elements, value.size)
        }

    private fun modeSummary(value: TunnelState.Mode?): String =
        when (value) {
            null -> context.getString(R.string.dont_modify)
            TunnelState.Mode.Direct -> context.getString(R.string.direct_mode)
            TunnelState.Mode.Global -> context.getString(R.string.global_mode)
            TunnelState.Mode.Rule -> context.getString(R.string.rule_mode)
            TunnelState.Mode.Script -> context.getString(R.string.script_mode)
        }

    private fun logLevelSummary(value: LogMessage.Level?): String =
        when (value) {
            null -> context.getString(R.string.dont_modify)
            LogMessage.Level.Info -> context.getString(R.string.info)
            LogMessage.Level.Warning -> context.getString(R.string.warning)
            LogMessage.Level.Error -> context.getString(R.string.error)
            LogMessage.Level.Debug -> context.getString(R.string.debug)
            LogMessage.Level.Silent -> context.getString(R.string.silent)
            else -> context.getString(R.string.dont_modify)
        }

    private fun strategySummary(value: Boolean?): String =
        when (value) {
            null -> context.getString(R.string.dont_modify)
            true -> context.getString(R.string.force_enable)
            false -> context.getString(R.string.use_built_in)
        }

    private fun enhancedModeSummary(value: ConfigurationOverride.DnsEnhancedMode?): String =
        when (value) {
            null -> context.getString(R.string.dont_modify)
            ConfigurationOverride.DnsEnhancedMode.None -> context.getString(R.string.dont_modify)
            ConfigurationOverride.DnsEnhancedMode.Mapping -> context.getString(R.string.mapping)
            ConfigurationOverride.DnsEnhancedMode.FakeIp -> context.getString(R.string.fakeip)
        }

    private fun fakeIpFilterModeSummary(value: ConfigurationOverride.FilterMode?): String =
        when (value) {
            null -> context.getString(R.string.dont_modify)
            ConfigurationOverride.FilterMode.BlackList -> context.getString(R.string.blacklist)
            ConfigurationOverride.FilterMode.WhiteList -> context.getString(R.string.whitelist)
        }

    private fun nullableBooleanSummary(value: Boolean?): String =
        when (value) {
            null -> context.getString(R.string.dont_modify)
            true -> context.getString(R.string.enabled)
            false -> context.getString(R.string.disabled)
        }
}
