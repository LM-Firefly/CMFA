package com.github.kr328.clash.design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/**
 * Material Design 3 Light Color Scheme
 */
val LightColorScheme = lightColorScheme(
    primary = Colors.Light.primary,
    onPrimary = Colors.Light.onPrimary,
    secondary = Colors.Light.secondary,
    onSecondary = Colors.Light.onSecondary,
    error = Colors.Light.error,
    background = Colors.Light.background,
    surface = Colors.Light.surface,
    onSurface = Colors.Light.onSurface,
    onSurfaceVariant = Colors.Light.onSurfaceVariant
)

/**
 * Material Design 3 Dark Color Scheme
 */
val DarkColorScheme = darkColorScheme(
    primary = Colors.Dark.primary,
    onPrimary = Colors.Dark.onPrimary,
    secondary = Colors.Dark.secondary,
    onSecondary = Colors.Dark.onSecondary,
    error = Colors.Dark.error,
    background = Colors.Dark.background,
    surface = Colors.Dark.surface,
    onSurface = Colors.Dark.onSurface,
    onSurfaceVariant = Colors.Dark.onSurfaceVariant
)

/**
 * Get the current color scheme based on system theme
 */
@Composable
fun getColorScheme(darkTheme: Boolean = isSystemInDarkTheme()): ColorScheme {
    return if (darkTheme) DarkColorScheme else LightColorScheme
}

/**
 * Get the current color configuration based on system theme
 */
@Composable
fun getCurrentColors(darkTheme: Boolean = isSystemInDarkTheme()) = if (darkTheme) Colors.Dark else Colors.Light
