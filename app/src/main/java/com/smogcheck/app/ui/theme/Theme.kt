package com.smogcheck.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = TealPrimary,
    onPrimary = Color.White,
    secondary = SkyBlue,
    tertiary = OrangeButton,
    background = BgMint,
    surface = CardWhite,
    onBackground = TextDark,
    onSurface = TextDark
)

@Composable
fun SmogCheckTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}
