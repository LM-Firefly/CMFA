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
import com.github.kr328.clash.design.compose.ClickablePreference
import com.github.kr328.clash.design.compose.MetaFeatureSettingsScreen
import com.github.kr328.clash.design.compose.PreferenceCategory
import com.github.kr328.clash.design.dialog.requestConfirm
import com.github.kr328.clash.design.dialog.requestModelListInput
import com.github.kr328.clash.design.dialog.requestModelTextInput
import kotlinx.coroutines.launch

class MetaFeatureSettingsDesign(
    context: Context,
    private val configuration: ConfigurationOverride
) : Design<MetaFeatureSettingsDesign.Request>(context) {
    enum class Request {
        ResetOverride,
        ImportGeoIp,
        ImportGeoSite,
        ImportCountry,
        ImportASN,
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
                val snifferEnabled = configuration.sniffer.enable != false

                MetaFeatureSettingsScreen(
                    title = context.getString(R.string.meta_features),
                    onBackClick = { (context as? android.app.Activity)?.finish() },
                    onClearClick = {
                        launch { requests.send(Request.ResetOverride) }
                    }
                ) {
                    PreferenceCategory(context.getString(R.string.settings))
                    ClickablePreference(title = context.getString(R.string.unified_delay), summary = nullableBooleanSummary(configuration.unifiedDelay), onClick = { launch { editNullableBoolean(context.getText(R.string.unified_delay), configuration.unifiedDelay) { configuration.unifiedDelay = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.geodata_mode), summary = nullableBooleanSummary(configuration.geodataMode), onClick = { launch { editNullableBoolean(context.getText(R.string.geodata_mode), configuration.geodataMode) { configuration.geodataMode = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.tcp_concurrent), summary = nullableBooleanSummary(configuration.tcpConcurrent), onClick = { launch { editNullableBoolean(context.getText(R.string.tcp_concurrent), configuration.tcpConcurrent) { configuration.tcpConcurrent = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.find_process_mode), summary = findProcessModeSummary(configuration.findProcessMode), onClick = { launch { editFindProcessMode() } }, modifier = Modifier.padding(horizontal = 8.dp))

                    PreferenceCategory(context.getString(R.string.sniffer_setting))
                    ClickablePreference(title = context.getString(R.string.strategy), summary = nullableBooleanSummary(configuration.sniffer.enable), onClick = { launch { editNullableBoolean(context.getText(R.string.strategy), configuration.sniffer.enable) { configuration.sniffer.enable = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.sniff_http_ports), summary = listSummary(configuration.sniffer.sniff.http.ports), enabled = snifferEnabled, onClick = { launch { editStringList(context.getText(R.string.sniff_http_ports), configuration.sniffer.sniff.http.ports) { configuration.sniffer.sniff.http.ports = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.sniff_http_override_destination), summary = nullableBooleanSummary(configuration.sniffer.sniff.http.overrideDestination), enabled = snifferEnabled, onClick = { launch { editNullableBoolean(context.getText(R.string.sniff_http_override_destination), configuration.sniffer.sniff.http.overrideDestination) { configuration.sniffer.sniff.http.overrideDestination = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.sniff_tls_ports), summary = listSummary(configuration.sniffer.sniff.tls.ports), enabled = snifferEnabled, onClick = { launch { editStringList(context.getText(R.string.sniff_tls_ports), configuration.sniffer.sniff.tls.ports) { configuration.sniffer.sniff.tls.ports = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.sniff_tls_override_destination), summary = nullableBooleanSummary(configuration.sniffer.sniff.tls.overrideDestination), enabled = snifferEnabled, onClick = { launch { editNullableBoolean(context.getText(R.string.sniff_tls_override_destination), configuration.sniffer.sniff.tls.overrideDestination) { configuration.sniffer.sniff.tls.overrideDestination = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.sniff_quic_ports), summary = listSummary(configuration.sniffer.sniff.quic.ports), enabled = snifferEnabled, onClick = { launch { editStringList(context.getText(R.string.sniff_quic_ports), configuration.sniffer.sniff.quic.ports) { configuration.sniffer.sniff.quic.ports = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.sniff_quic_override_destination), summary = nullableBooleanSummary(configuration.sniffer.sniff.quic.overrideDestination), enabled = snifferEnabled, onClick = { launch { editNullableBoolean(context.getText(R.string.sniff_quic_override_destination), configuration.sniffer.sniff.quic.overrideDestination) { configuration.sniffer.sniff.quic.overrideDestination = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.force_dns_mapping), summary = nullableBooleanSummary(configuration.sniffer.forceDnsMapping), enabled = snifferEnabled, onClick = { launch { editNullableBoolean(context.getText(R.string.force_dns_mapping), configuration.sniffer.forceDnsMapping) { configuration.sniffer.forceDnsMapping = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.parse_pure_ip), summary = nullableBooleanSummary(configuration.sniffer.parsePureIp), enabled = snifferEnabled, onClick = { launch { editNullableBoolean(context.getText(R.string.parse_pure_ip), configuration.sniffer.parsePureIp) { configuration.sniffer.parsePureIp = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.override_destination), summary = nullableBooleanSummary(configuration.sniffer.overrideDestination), enabled = snifferEnabled, onClick = { launch { editNullableBoolean(context.getText(R.string.override_destination), configuration.sniffer.overrideDestination) { configuration.sniffer.overrideDestination = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.force_domain), summary = listSummary(configuration.sniffer.forceDomain), enabled = snifferEnabled, onClick = { launch { editStringList(context.getText(R.string.force_domain), configuration.sniffer.forceDomain) { configuration.sniffer.forceDomain = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.skip_domain), summary = listSummary(configuration.sniffer.skipDomain), enabled = snifferEnabled, onClick = { launch { editStringList(context.getText(R.string.skip_domain), configuration.sniffer.skipDomain) { configuration.sniffer.skipDomain = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.skip_src_address), summary = listSummary(configuration.sniffer.skipSrcAddress), enabled = snifferEnabled, onClick = { launch { editStringList(context.getText(R.string.skip_src_address), configuration.sniffer.skipSrcAddress) { configuration.sniffer.skipSrcAddress = it } } }, modifier = Modifier.padding(horizontal = 8.dp))
                    ClickablePreference(title = context.getString(R.string.skip_dst_address), summary = listSummary(configuration.sniffer.skipDstAddress), enabled = snifferEnabled, onClick = { launch { editStringList(context.getText(R.string.skip_dst_address), configuration.sniffer.skipDstAddress) { configuration.sniffer.skipDstAddress = it } } }, modifier = Modifier.padding(horizontal = 8.dp))

                    PreferenceCategory(context.getString(R.string.geox_files))
                    ClickablePreference(
                        title = context.getString(R.string.import_geoip_file),
                        summary = context.getString(R.string.press_to_import),
                        onClick = { launch { requests.send(Request.ImportGeoIp) } },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    ClickablePreference(
                        title = context.getString(R.string.import_geosite_file),
                        summary = context.getString(R.string.press_to_import),
                        onClick = { launch { requests.send(Request.ImportGeoSite) } },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    ClickablePreference(
                        title = context.getString(R.string.import_country_file),
                        summary = context.getString(R.string.press_to_import),
                        onClick = { launch { requests.send(Request.ImportCountry) } },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    ClickablePreference(
                        title = context.getString(R.string.import_asn_file),
                        summary = context.getString(R.string.press_to_import),
                        onClick = { launch { requests.send(Request.ImportASN) } },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
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
            set(text.split(',', '\n').map { it.trim() }.filter { it.isNotEmpty() })
        }
        bump()
    }

    private suspend fun editFindProcessMode() {
        val choices = listOf(
            Choice(context.getString(R.string.dont_modify), null),
            Choice(context.getString(R.string.off), ConfigurationOverride.FindProcessMode.Off),
            Choice(context.getString(R.string.strict), ConfigurationOverride.FindProcessMode.Strict),
            Choice(context.getString(R.string.always), ConfigurationOverride.FindProcessMode.Always),
        )
        val selected = context.requestModelListInput(choices, context.getText(R.string.find_process_mode)) { it.label } ?: return
        configuration.findProcessMode = selected.value
        bump()
    }

    private fun findProcessModeSummary(value: ConfigurationOverride.FindProcessMode?): String =
        when (value) {
            null -> context.getString(R.string.dont_modify)
            ConfigurationOverride.FindProcessMode.Off -> context.getString(R.string.off)
            ConfigurationOverride.FindProcessMode.Strict -> context.getString(R.string.strict)
            ConfigurationOverride.FindProcessMode.Always -> context.getString(R.string.always)
        }

    private fun textSummary(value: String?): String =
        if (value.isNullOrBlank()) context.getString(R.string.dont_modify) else value

    private fun listSummary(value: List<String>?): String =
        when {
            value == null -> context.getString(R.string.dont_modify)
            value.isEmpty() -> context.getString(R.string.empty)
            else -> value.joinToString(", ")
        }

    private fun nullableBooleanSummary(value: Boolean?): String =
        when (value) {
            null -> context.getString(R.string.dont_modify)
            true -> context.getString(R.string.enabled)
            false -> context.getString(R.string.disabled)
        }

    private fun bump() {
        renderTick.value = renderTick.value + 1
    }
}
