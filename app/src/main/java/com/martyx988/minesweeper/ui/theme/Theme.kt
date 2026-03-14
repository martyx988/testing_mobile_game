package com.martyx988.minesweeper.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.martyx988.minesweeper.domain.AppThemeChoice

private val ClassicColors = lightColorScheme(
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

private val ForestColors = lightColorScheme(
    primary = Color(0xFF305946),
    onPrimary = MineFog,
    secondary = Color(0xFF8C7A45),
    background = Color(0xFFEAF1E1),
    onBackground = MineCharcoal,
    surface = Color(0xFFF7FBF3),
    onSurface = MineCharcoal,
    surfaceVariant = Color(0xFFDCE7D7),
    onSurfaceVariant = Color(0xFF456251),
)

private val SunsetColors = lightColorScheme(
    primary = Color(0xFF7F3D2E),
    onPrimary = MineFog,
    secondary = Color(0xFFD28A4C),
    background = Color(0xFFFFF1E1),
    onBackground = MineCharcoal,
    surface = Color(0xFFFFF9F2),
    onSurface = MineCharcoal,
    surfaceVariant = Color(0xFFF8E0CC),
    onSurfaceVariant = Color(0xFF6F5548),
)

@Composable
internal fun MinesweeperTheme(
    themeChoice: AppThemeChoice = AppThemeChoice.CLASSIC,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = when (themeChoice) {
            AppThemeChoice.CLASSIC -> ClassicColors
            AppThemeChoice.FOREST -> ForestColors
            AppThemeChoice.SUNSET -> SunsetColors
        },
        typography = MinesweeperTypography,
        content = content,
    )
}

private object ColorTokens {
    val surfaceVariant = MineFog.copy(alpha = 0.88f)
    val onSurfaceVariant = MineCharcoal.copy(alpha = 0.72f)
}
