package com.theapache64.phokuzed.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Colors.Daintree,
    onPrimary = Color.White,
    primaryVariant = Colors.Daintree_800,
    secondary = Colors.Daintree_1000,
    onSecondary = Color.White,
    surface = Colors.Daintree,
    onSurface = Color.White,
)

@Composable
fun PhokuzedTheme(
    content: @Composable() () -> Unit
) {
    MaterialTheme(
        colors = DarkColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
