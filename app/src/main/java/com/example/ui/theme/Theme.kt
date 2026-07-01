package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ================= GOLD PALETTE =================
private val GoldLightColorScheme = lightColorScheme(
    primary = SpiritualGoldLight,
    onPrimary = Color.White,
    primaryContainer = SpiritualLightSurfaceVariant,
    onPrimaryContainer = Color(0xFF2E1C00),
    secondary = SpiritualCrimsonLight,
    onSecondary = Color.White,
    tertiary = SpiritualIslamicGreenLight,
    onTertiary = Color.White,
    background = SpiritualLightBg,
    onBackground = Color(0xFF1C1A17),
    surface = SpiritualLightSurface,
    onSurface = Color(0xFF1C1A17),
    surfaceVariant = SpiritualLightSurfaceVariant,
    onSurfaceVariant = Color(0xFF4A463E),
    outline = Color(0xFF807664)
)

private val GoldDarkColorScheme = darkColorScheme(
    primary = SpiritualGold,
    onPrimary = Color(0xFF261800),
    primaryContainer = SpiritualDarkSurfaceVariant,
    onPrimaryContainer = Color.White,
    secondary = SpiritualCrimson,
    onSecondary = Color.White,
    tertiary = SpiritualIslamicGreen,
    onTertiary = Color.White,
    background = SpiritualDarkBg,
    onBackground = Color(0xFFEBF1F5),
    surface = SpiritualDarkSurface,
    onSurface = Color(0xFFEBF1F5),
    surfaceVariant = SpiritualDarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFC5C6D0),
    outline = Color(0xFF8D919F)
)

// ================= BLUE PALETTE =================
private val BlueLightColorScheme = lightColorScheme(
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

private val BlueDarkColorScheme = darkColorScheme(
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

// ================= GREEN PALETTE =================
private val GreenLightColorScheme = lightColorScheme(
    primary = SpiritualIslamicGreenLight,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE8F5E9),
    onPrimaryContainer = Color(0xFF0F3811),
    secondary = SpiritualGoldLight,
    onSecondary = Color.White,
    tertiary = Color(0xFF1E88E5),
    onTertiary = Color.White,
    background = Color(0xFFF9FBF9),
    onBackground = Color(0xFF111411),
    surface = Color.White,
    onSurface = Color(0xFF111411),
    surfaceVariant = Color(0xFFE1E8E1),
    onSurfaceVariant = Color(0xFF3F453F),
    outline = Color(0xFFBEC5BE)
)

private val GreenDarkColorScheme = darkColorScheme(
    primary = Color(0xFF81C784),
    onPrimary = Color(0xFF003300),
    primaryContainer = Color(0xFF1B5E20),
    onPrimaryContainer = Color(0xFFE8F5E9),
    secondary = SpiritualGold,
    onSecondary = Color.Black,
    tertiary = Color(0xFF64B5F6),
    onTertiary = Color.Black,
    background = Color(0xFF0B140E),
    onBackground = Color(0xFFE1E8E1),
    surface = Color(0xFF122117),
    onSurface = Color(0xFFE1E8E1),
    surfaceVariant = Color(0xFF1A2B20),
    onSurfaceVariant = Color(0xFFBEC5BE),
    outline = Color(0xFF889388)
)

// ================= CRIMSON PALETTE =================
private val CrimsonLightColorScheme = lightColorScheme(
    primary = SpiritualCrimsonLight,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFDF0EF),
    onPrimaryContainer = Color(0xFF4A100A),
    secondary = SpiritualGoldLight,
    onSecondary = Color.White,
    tertiary = SpiritualIslamicGreenLight,
    onTertiary = Color.White,
    background = Color(0xFFFDFBFB),
    onBackground = Color(0xFF1F1211),
    surface = Color.White,
    onSurface = Color(0xFF1F1211),
    surfaceVariant = Color(0xFFEAE0DF),
    onSurfaceVariant = Color(0xFF534341),
    outline = Color(0xFFD0C3C1)
)

private val CrimsonDarkColorScheme = darkColorScheme(
    primary = Color(0xFFEF9A9A),
    onPrimary = Color(0xFF5C0000),
    primaryContainer = Color(0xFF5B0000),
    onPrimaryContainer = Color(0xFFFDF0EF),
    secondary = SpiritualGold,
    onSecondary = Color.Black,
    tertiary = SpiritualIslamicGreen,
    onTertiary = Color.White,
    background = Color(0xFF140B0B),
    onBackground = Color(0xFFEAE0DF),
    surface = Color(0xFF221212),
    onSurface = Color(0xFFEAE0DF),
    surfaceVariant = Color(0xFF331D1D),
    onSurfaceVariant = Color(0xFFD0C3C1),
    outline = Color(0xFF9E8E8C)
)

@Composable
fun MyApplicationTheme(
    themeMode: String = "SYSTEM",
    colorTheme: String = "GOLD",
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        "LIGHT" -> false
        "DARK" -> true
        else -> isSystemInDarkTheme()
    }

    val colorScheme = when (colorTheme) {
        "BLUE" -> if (darkTheme) BlueDarkColorScheme else BlueLightColorScheme
        "GREEN" -> if (darkTheme) GreenDarkColorScheme else GreenLightColorScheme
        "CRIMSON" -> if (darkTheme) CrimsonDarkColorScheme else CrimsonLightColorScheme
        else -> if (darkTheme) GoldDarkColorScheme else GoldLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

