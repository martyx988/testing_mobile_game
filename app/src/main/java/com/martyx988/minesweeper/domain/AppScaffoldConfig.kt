package com.martyx988.minesweeper.domain

internal object AppScaffoldConfig {
    const val appTitle = "Minesweeper"
    val supportedModes: List<GameMode> = listOf(
        GameMode.CLASSIC_EASY,
        GameMode.TRAP_TILES,
    )
}

