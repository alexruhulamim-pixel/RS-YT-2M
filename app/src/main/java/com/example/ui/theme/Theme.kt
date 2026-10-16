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

private val DarkColorScheme = darkColorScheme(
    primary = Sky400,
    onPrimary = Slate900,
    primaryContainer = Slate800,
    onPrimaryContainer = Sky400,
    secondary = Amber500,
    onSecondary = Slate900,
    secondaryContainer = Slate750,
    onSecondaryContainer = Amber400,
    tertiary = Blue600,
    onTertiary = Slate50,
    background = Slate900,
    onBackground = Slate50,
    surface = Slate800,
    onSurface = Slate200,
    surfaceVariant = Slate700,
    onSurfaceVariant = Slate400,
    outline = Slate700,
    outlineVariant = Slate600
)

private val LightColorScheme = darkColorScheme(
    // Defaulting to the dark gamer theme requested in the HTML prompt
    primary = Sky400,
    onPrimary = Slate900,
    primaryContainer = Slate800,
    onPrimaryContainer = Sky400,
    secondary = Amber500,
    onSecondary = Slate900,
    secondaryContainer = Slate750,
    onSecondaryContainer = Amber400,
    tertiary = Blue600,
    onTertiary = Slate50,
    background = Slate900,
    onBackground = Slate50,
    surface = Slate800,
    onSurface = Slate200,
    surfaceVariant = Slate700,
    onSurfaceVariant = Slate400,
    outline = Slate700,
    outlineVariant = Slate600
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Gaming redeem codes theme is dark by default
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else DarkColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
