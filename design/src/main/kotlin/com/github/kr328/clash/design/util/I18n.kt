package com.github.kr328.clash.design.util

import android.content.Context
import com.github.kr328.clash.common.compat.preferredLocale
import com.github.kr328.clash.core.model.Provider
import com.github.kr328.clash.design.R
import com.github.kr328.clash.service.model.Profile
import java.text.SimpleDateFormat
import java.util.*

private const val DATE_DATE_ONLY = "yyyy-MM-dd"
private const val DATE_TIME_ONLY = "HH:mm:ss.SSS"
private const val DATE_ALL = "$DATE_DATE_ONLY $DATE_TIME_ONLY"

fun Profile.Type.toString(context: Context): String {
    return when (this) {
        Profile.Type.File -> context.getString(R.string.file)
        Profile.Type.Url -> context.getString(R.string.url)
        Profile.Type.External -> context.getString(R.string.external)
    }
}

fun Provider.type(context: Context): String {
    val type = when (type) {
        Provider.Type.Proxy -> context.getString(R.string.proxy)
        Provider.Type.Rule -> context.getString(R.string.rule)
    }

    val vehicle = when (vehicleType) {
        Provider.VehicleType.HTTP -> context.getString(R.string.http)
        Provider.VehicleType.File -> context.getString(R.string.file)
        Provider.VehicleType.Inline -> context.getString(R.string.inline)
        Provider.VehicleType.Compatible -> context.getString(R.string.compatible)
    }

    return context.getString(R.string.format_provider_type, type, vehicle)
}

fun Provider.subtitle(context: Context): String {
    val typeStr = type(context)
    val subInfo = subscriptionInfo ?: return typeStr

    if (subInfo.total == 0L && subInfo.expire == 0L) {
        return typeStr
    }

    val used = subInfo.upload + subInfo.download
    val total = subInfo.total
    val expire = subInfo.expire

    val usedStr = used.toBytesString()
    val totalStr = if (total > 0) total.toBytesString() else "∞"
    val expireStr = if (expire > 0) Date(expire * 1000).format(context, includeDate = true, includeTime = false) else "∞"

    return "$typeStr\n$usedStr / $totalStr  $expireStr"
}

@JvmOverloads
fun Date.format(
    context: Context,
    includeDate: Boolean = true,
    includeTime: Boolean = true,
): String {
    val locale = context.resources.configuration.preferredLocale

    return when {
        includeDate && includeTime ->
            SimpleDateFormat(DATE_ALL, locale).format(this)
        includeDate ->
            SimpleDateFormat(DATE_DATE_ONLY, locale).format(this)
        includeTime ->
            SimpleDateFormat(DATE_TIME_ONLY, locale).format(this)
        else -> ""
    }
}

fun Long.toBytesString(): String {
    // 处理负数和0
    if (this <= 0) return "0 B"
    
    return when {
        this > 1024.0 * 1024 * 1024 * 1024 * 1024 * 1024 ->
            String.format("%.2f EiB", (this.toDouble() / 1024 / 1024 / 1024 / 1024 / 1024 / 1024))
        this > 1024.0 * 1024 * 1024 * 1024 * 1024 ->
            String.format("%.2f PiB", (this.toDouble() / 1024 / 1024 / 1024 / 1024 / 1024))
        this > 1024.0 * 1024 * 1024 * 1024 ->
            String.format("%.2f TiB", (this.toDouble() / 1024 / 1024 / 1024 / 1024))
        this > 1024 * 1024 * 1024 ->
            String.format("%.2f GiB", (this.toDouble() / 1024 / 1024 / 1024))
        this > 1024 * 1024 ->
            String.format("%.2f MiB", (this.toDouble() / 1024 / 1024))
        this > 1024 ->
            String.format("%.2f KiB", (this.toDouble() / 1024))
        else ->
            "$this B"
    }
}

fun Double.toProgress(): Int = toInt()
fun Long.toDateStr(): String {
    val simpleDateFormat =SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    return simpleDateFormat.format(Date(this))
}

// Provider subscription info helpers
fun Provider.hasSubscriptionInfo(): Boolean {
    val subInfo = subscriptionInfo ?: return false
    return subInfo.total > 0L || subInfo.expire > 0L
}

fun Provider.hasExpireInfo(): Boolean {
    return subscriptionInfo?.expire?.let { it > 0L } ?: false
}

fun Provider.isExpired(): Boolean {
    val expire = subscriptionInfo?.expire ?: return false
    return expire > 0L && (expire * 1000) < System.currentTimeMillis()
}

fun Provider.getUsagePercentage(): Int {
    val subInfo = subscriptionInfo ?: return 0
    if (subInfo.total <= 0L) return 0
    val upload = subInfo.upload.toULong()
    val download = subInfo.download.toULong()
    val used = upload + download
    val total = subInfo.total.toULong()
    val percentage = (used.toDouble() / total.toDouble() * 100).toInt()
    // 允许超过100%以显示超额使用情况
    return percentage.coerceAtLeast(0)
}

fun Provider.getUsagePercentageText(): String {
    val subInfo = subscriptionInfo ?: return ""
    if (subInfo.total <= 0L) return ""
    val upload = subInfo.upload.toULong()
    val download = subInfo.download.toULong()
    val used = upload + download
    val total = subInfo.total.toULong()
    val percentage = (used.toDouble() / total.toDouble() * 100)
    return String.format("%.2f%%", percentage.coerceAtLeast(0.0))
}

// Profile subscription info helpers
fun Profile.getProfileUsagePercentage(): Int {
    if (total <= 1L) return 0
    val used = (upload + download).toULong()
    val totalULong = total.toULong()
    val percentage = (used.toDouble() / totalULong.toDouble() * 100).toInt()
    return percentage.coerceAtLeast(0)
}

fun Profile.getProfileUsagePercentageText(): String {
    if (total <= 1L) return ""
    val used = (upload + download).toDouble()
    val totalDouble = total.toDouble()
    val percentage = (used / totalDouble * 100)
    return String.format("%.2f%%", percentage.coerceAtLeast(0.0))
}

fun Profile.getProfileRemainingTraffic(context: Context): String {
    if (total <= 1L) return ""
    val used = (upload + download).toULong()
    val totalULong = total.toULong()
    val remaining = if (totalULong > used) (totalULong - used).toLong() else 0L
    return context.getString(R.string.remaining_traffic, remaining.toBytesString())
}

fun Profile.getProfileUsedTraffic(context: Context): String {
    if (total <= 1L) return ""
    val used = (upload + download)
    return context.getString(R.string.used_traffic, used.toBytesString(), total.toBytesString())
}

fun Profile.getProfileExpireInfo(context: Context): String {
    if (expire <= 0L) return ""
    val expireDate = Date(expire)
    val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(expireDate)
    val now = System.currentTimeMillis()
    val diff = expire - now
    return if (diff > 0) {
        val days = diff / (1000 * 60 * 60 * 24)
        context.getString(R.string.expire_info_days, dateStr, days)
    } else {
        context.getString(R.string.expire_info_expired, dateStr)
    }
}

fun Provider.getProgressColor(context: Context): Int {
    val percentage = getUsagePercentage()
    return when {
        percentage >= 90 -> context.getColor(R.color.design_default_color_error)
        percentage >= 70 -> 0xFFFFC107.toInt() // Amber/Warning
        else -> 0xFF4CAF50.toInt() // Green
    }
}

fun Provider.getRemainingTraffic(context: Context): String {
    val subInfo = subscriptionInfo ?: return ""
    if (subInfo.total <= 0L) return ""
    val upload = subInfo.upload.toULong()
    val download = subInfo.download.toULong()
    val used = upload + download
    val total = subInfo.total.toULong()
    val remaining = if (total > used) (total - used).toLong() else 0L
    return context.getString(R.string.remaining_traffic, remaining.toBytesString())
}

fun Provider.getUsedTraffic(context: Context): String {
    val subInfo = subscriptionInfo ?: return ""
    if (subInfo.total <= 0L) return ""
    val upload = subInfo.upload.toULong()
    val download = subInfo.download.toULong()
    val used = (upload + download).toLong()
    val total = subInfo.total
    return context.getString(R.string.used_traffic, used.toBytesString(), total.toBytesString())
}

fun Provider.getExpireInfo(context: Context): String {
    val expire = subscriptionInfo?.expire ?: return ""
    if (expire <= 0L) return ""
    
    val expireDate = Date(expire * 1000)
    val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(expireDate)
    
    val now = System.currentTimeMillis()
    val diff = expire * 1000 - now
    
    return if (diff > 0) {
        val days = diff / (1000 * 60 * 60 * 24)
        context.getString(R.string.expire_info_days, dateStr, days)
    } else {
        context.getString(R.string.expire_info_expired, dateStr)
    }
}
