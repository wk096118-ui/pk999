package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val PK999ColorScheme =
  darkColorScheme(
    primary = CasinoGold,
    onPrimary = CasinoBackground,
    primaryContainer = CasinoGoldDark,
    onPrimaryContainer = CasinoGoldLight,
    secondary = CasinoGreen,
    onSecondary = CasinoBackground,
    tertiary = CasinoRed,
    onTertiary = CasinoTextPrimary,
    background = CasinoBackground,
    onBackground = CasinoTextPrimary,
    surface = CasinoSurface,
    onSurface = CasinoTextPrimary,
    surfaceVariant = CasinoSurfaceElevated,
    onSurfaceVariant = CasinoTextSecondary,
    outline = CasinoBorder
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = PK999ColorScheme,
    typography = Typography,
    content = content
  )
}
