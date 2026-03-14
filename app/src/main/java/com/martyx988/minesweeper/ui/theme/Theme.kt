package com.martyx988.minesweeper.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = MineGreen,
    onPrimary = MineFog,
    secondary = MineClay,
    background = MineSand,
    onBackground = MineCharcoal,
    surface = MineFog,
    onSurface = MineCharcoal,
    surfaceVariant = ColorTokens.surfaceVariant,
    onSurfaceVariant = ColorTokens.onSurfaceVariant,
)

private val DarkColors = darkColorScheme(
    primary = MineSteel,
    onPrimary = MineCharcoal,
    secondary = MineClay,
    background = MineCharcoal,
    onBackground = MineFog,
    surface = ColorTokens.darkSurface,
    onSurface = MineFog,
    surfaceVariant = ColorTokens.darkSurfaceVariant,
    onSurfaceVariant = MineSteel,
)

@Composable
internal fun MinesweeperTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = MinesweeperTypography,
        content = content,
    )
}

private object ColorTokens {
    val surfaceVariant = MineFog.copy(alpha = 0.88f)
    val onSurfaceVariant = MineCharcoal.copy(alpha = 0.72f)
    val darkSurface = Color(0xFF273438)
    val darkSurfaceVariant = Color(0xFF314247)
}
