package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CaptioColorScheme = darkColorScheme(
    primary = CaptioPurplePrimary,
    onPrimary = Color.White,
    primaryContainer = CaptioPurpleDark,
    onPrimaryContainer = Color.White,
    secondary = CaptioNeonGreen,
    onSecondary = Color.Black,
    secondaryContainer = CaptioNeonGreenAlpha,
    onSecondaryContainer = CaptioNeonGreen,
    background = CaptioBgDeep,
    onBackground = CaptioTextWhite,
    surface = CaptioCardBg,
    onSurface = CaptioTextWhite,
    surfaceVariant = CaptioCardBgElevated,
    onSurfaceVariant = CaptioTextGray,
    outline = CaptioCardBorder
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CaptioColorScheme,
        typography = Typography,
        content = content
    )
}
