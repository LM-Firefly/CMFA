package com.github.kr328.clash.design.theme

import androidx.compose.ui.graphics.Color

/**
 * Unified color configuration for light and dark themes
 */
object Colors {
    // Primary colors
    val ClashLight = Color(0xFF1e4376)
    val ClashDark = Color(0xFF1976d2)

    // Background and surface
    val LightBackground = Color(0xFFFAFAFA)
    val DarkBackground = Color(0xFF121212)
    val DarkSurface = Color(0xFF202020)

    // Overlay and system
    val SystemUiOverlay = Color(0x50000000)

    // Control states
    val LightClashStopped = Color(0xFF808080)
    val LightControlDisabled = Color(0xFFD3D3D3)
    val DarkControlDisabled = Color(0xFF808080)

    // Error
    val Error = Color(0xFFB00020)

    // Launcher
    val LauncherBackground = Color(0xFFFFFFFF)
    val ServiceClash = Color(0xFF1E4376)

    // Light theme palette
    object Light {
        val primary = ClashLight
        val onPrimary = Color.White
        val secondary = ClashLight
        val onSecondary = Color.White
        val background = LightBackground
        val surface = Color.White
        val onSurface = Color.Black
        val onSurfaceVariant = Color.Gray
        val error = Error
        val clashStopped = LightClashStopped
        val controlDisabled = LightControlDisabled
        val logo = primary
        val statusBar = SystemUiOverlay
        val navigationBar = SystemUiOverlay
    }

    // Dark theme palette
    object Dark {
        val primary = ClashDark
        val onPrimary = Color.White
        val secondary = ClashDark
        val onSecondary = Color.White
        val background = DarkBackground
        val surface = DarkSurface
        val onSurface = Color.White
        val onSurfaceVariant = Color.White
        val error = Error
        val clashStopped = DarkSurface
        val controlDisabled = DarkControlDisabled
        val logo = onSurface
        val statusBar = Color.Transparent
        val navigationBar = Color.Transparent
    }
}
