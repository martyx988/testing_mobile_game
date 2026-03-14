package com.martyx988.minesweeper.domain

enum class AchievementId {
    FIRST_WIN,
    FIRST_LOSS,
    FIRST_FLAG,
}

enum class AppThemeChoice {
    CLASSIC,
    FOREST,
    SUNSET,
}

data class PlayerStats(
    val wins: Int = 0,
    val losses: Int = 0,
    val flagsPlaced: Int = 0,
)

data class PlayerSettings(
    val themeChoice: AppThemeChoice = AppThemeChoice.CLASSIC,
    val hapticsEnabled: Boolean = true,
)

data class PlayerProfile(
    val stats: PlayerStats = PlayerStats(),
    val achievements: Set<AchievementId> = emptySet(),
    val settings: PlayerSettings = PlayerSettings(),
)

object PlayerProfileManager {
    fun recordGameFinished(
        current: PlayerProfile,
        result: MatchStatus,
    ): PlayerProfile = when (result) {
        MatchStatus.WON -> current.copy(
            stats = current.stats.copy(wins = current.stats.wins + 1),
            achievements = current.achievements + AchievementId.FIRST_WIN,
        )
        MatchStatus.LOST -> current.copy(
            stats = current.stats.copy(losses = current.stats.losses + 1),
            achievements = current.achievements + AchievementId.FIRST_LOSS,
        )
        MatchStatus.ACTIVE -> current
    }

    fun recordFlagPlaced(current: PlayerProfile): PlayerProfile = current.copy(
        stats = current.stats.copy(flagsPlaced = current.stats.flagsPlaced + 1),
        achievements = current.achievements + AchievementId.FIRST_FLAG,
    )

    fun cycleTheme(current: PlayerProfile): PlayerProfile {
        val nextTheme = when (current.settings.themeChoice) {
            AppThemeChoice.CLASSIC -> AppThemeChoice.FOREST
            AppThemeChoice.FOREST -> AppThemeChoice.SUNSET
            AppThemeChoice.SUNSET -> AppThemeChoice.CLASSIC
        }
        return current.copy(
            settings = current.settings.copy(themeChoice = nextTheme),
        )
    }

    fun setHapticsEnabled(
        current: PlayerProfile,
        enabled: Boolean,
    ): PlayerProfile = current.copy(
        settings = current.settings.copy(hapticsEnabled = enabled),
    )
}
