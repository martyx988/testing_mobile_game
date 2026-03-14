package com.martyx988.minesweeper.data

import android.content.Context
import com.martyx988.minesweeper.domain.AchievementId
import com.martyx988.minesweeper.domain.AppThemeChoice
import com.martyx988.minesweeper.domain.PlayerProfile
import com.martyx988.minesweeper.domain.PlayerSettings
import com.martyx988.minesweeper.domain.PlayerStats

class SharedPreferencesPlayerProfileStore(
    context: Context,
) : PlayerProfileStore {
    private val preferences = context.getSharedPreferences("minesweeper_profile", Context.MODE_PRIVATE)

    override fun load(): PlayerProfile = PlayerProfile(
        stats = PlayerStats(
            wins = preferences.getInt(KEY_WINS, 0),
            losses = preferences.getInt(KEY_LOSSES, 0),
            flagsPlaced = preferences.getInt(KEY_FLAGS_PLACED, 0),
        ),
        achievements = preferences
            .getStringSet(KEY_ACHIEVEMENTS, emptySet())
            .orEmpty()
            .mapNotNull { raw -> AchievementId.entries.find { it.name == raw } }
            .toSet(),
        settings = PlayerSettings(
            themeChoice = AppThemeChoice.entries.find {
                it.name == preferences.getString(KEY_THEME, AppThemeChoice.CLASSIC.name)
            } ?: AppThemeChoice.CLASSIC,
            hapticsEnabled = preferences.getBoolean(KEY_HAPTICS_ENABLED, true),
        ),
    )

    override fun save(profile: PlayerProfile) {
        val saved = preferences.edit()
            .putInt(KEY_WINS, profile.stats.wins)
            .putInt(KEY_LOSSES, profile.stats.losses)
            .putInt(KEY_FLAGS_PLACED, profile.stats.flagsPlaced)
            .putStringSet(KEY_ACHIEVEMENTS, profile.achievements.map { it.name }.toSet())
            .putString(KEY_THEME, profile.settings.themeChoice.name)
            .putBoolean(KEY_HAPTICS_ENABLED, profile.settings.hapticsEnabled)
            .commit()
        check(saved) { "Failed to persist player profile" }
    }

    private companion object {
        const val KEY_WINS = "wins"
        const val KEY_LOSSES = "losses"
        const val KEY_FLAGS_PLACED = "flags_placed"
        const val KEY_ACHIEVEMENTS = "achievements"
        const val KEY_THEME = "theme"
        const val KEY_HAPTICS_ENABLED = "haptics_enabled"
    }
}
