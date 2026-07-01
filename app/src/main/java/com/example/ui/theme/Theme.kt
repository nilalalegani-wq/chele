package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF9ECAFF),
    onPrimary = Color(0xFF003258),
    primaryContainer = Color(0xFF00497D),
    onPrimaryContainer = Color(0xFFD1E4FF),
    secondary = HighDensityAccentDark,
    onSecondary = Color.White,
    tertiary = HighDensitySuccess,
    onTertiary = Color.White,
    background = Color(0xFF1A1C1E),
    onBackground = Color(0xFFE3E2E6),
    surface = Color(0xFF21242A),
    onSurface = Color(0xFFE3E2E6),
    surfaceVariant = Color(0xFF2C313B),
    onSurfaceVariant = Color(0xFFC4C6D0),
    outline = Color(0xFF8D919F)
)

private val LightColorScheme = lightColorScheme(
    primary = HighDensityPrimary,
    onPrimary = Color.White,
    primaryContainer = HighDensityPrimaryContainer,
    onPrimaryContainer = HighDensityOnPrimaryContainer,
    secondary = HighDensityAccentDark,
    onSecondary = Color.White,
    tertiary = HighDensitySuccess,
    onTertiary = Color.White,
    background = HighDensityBg,
    onBackground = HighDensityText,
    surface = Color.White,
    onSurface = HighDensityText,
    surfaceVariant = HighDensitySecondaryContainer,
    onSurfaceVariant = HighDensitySecondaryText,
    outline = HighDensityOutline
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
