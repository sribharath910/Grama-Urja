package com.gramaUrja.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Green800,
    onPrimary = White,
    primaryContainer = Green50,
    onPrimaryContainer = Green800,
    secondary = Green600,
    onSecondary = White,
    secondaryContainer = Green50,
    onSecondaryContainer = Green800,
    background = GreenBg,
    onBackground = Color(0xFF1B2E1C),
    surface = White,
    onSurface = Color(0xFF1B2E1C),
    surfaceVariant = Green50,
    onSurfaceVariant = Color(0xFF5D7A5F),
    outline = Color(0xFFC8E6C9),
    error = Red800,
    onError = White
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkSurface,
    onPrimaryContainer = Green200,
    secondary = Green400,
    onSecondary = DarkOnPrimary,
    secondaryContainer = DarkSurface,
    onSecondaryContainer = Green200,
    background = DarkBg,
    onBackground = Color(0xFFE8F5E9),
    surface = DarkCard,
    onSurface = Color(0xFFE8F5E9),
    surfaceVariant = DarkSurface,
    onSurfaceVariant = Color(0xFF81A882),
    outline = DarkBorder,
    error = DarkPowerOff,
    onError = White
)

@Composable
fun GramaUrjaTheme(
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
