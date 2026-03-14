package com.martyx988.minesweeper.domain

internal enum class SupportedMode {
    CLASSIC_EASY,
    TRAP_TILES,
}

internal object AppScaffoldConfig {
    const val appTitle = "Minesweeper"
    val supportedModes: List<SupportedMode> = listOf(
        SupportedMode.CLASSIC_EASY,
        SupportedMode.TRAP_TILES,
    )
}

