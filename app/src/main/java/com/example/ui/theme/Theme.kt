package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val FixArtDarkColorScheme = darkColorScheme(
    primary = VividSky,
    onPrimary = NavyDark,
    primaryContainer = NavySurfaceElevated,
    onPrimaryContainer = DarkTextPrimary,
    secondary = NeonCyan,
    onSecondary = NavyDark,
    secondaryContainer = NavyCard,
    onSecondaryContainer = DarkTextPrimary,
    tertiary = AccentGold,
    onTertiary = NavyDark,
    background = NavyDark,
    onBackground = DarkTextPrimary,
    surface = NavySurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = NavyCard,
    onSurfaceVariant = DarkTextSecondary,
    outline = NavyBorder,
    outlineVariant = Color(0xFF223561),
    error = ErrorRed,
    onError = Color.White
)

private val FixArtLightColorScheme = lightColorScheme(
    primary = LightNavyPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDBEAFE),
    onPrimaryContainer = Color(0xFF1E3A8A),
    secondary = LightBlueSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0F2FE),
    onSecondaryContainer = Color(0xFF075985),
    tertiary = AccentGold,
    onTertiary = Color.White,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = Color(0xFFF8FAFC),
    onSurfaceVariant = LightTextSecondary,
    outline = LightBorder,
    outlineVariant = Color(0xFFCBD5E1),
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to FixArt signature dark navy styling requested by user
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) FixArtDarkColorScheme else FixArtLightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
