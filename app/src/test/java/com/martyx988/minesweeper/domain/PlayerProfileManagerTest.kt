package com.martyx988.minesweeper.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PlayerProfileManagerTest {
    @Test
    fun recordCompletedGame_updatesStatsAndUnlocksFirstWin() {
        val profile = PlayerProfileManager.recordGameFinished(
            current = PlayerProfile(),
            result = MatchStatus.WON,
        )

        assertEquals(1, profile.stats.wins)
        assertTrue(profile.achievements.contains(AchievementId.FIRST_WIN))
    }

    @Test
    fun recordCompletedGame_updatesStatsAndUnlocksFirstLoss() {
        val profile = PlayerProfileManager.recordGameFinished(
            current = PlayerProfile(),
            result = MatchStatus.LOST,
        )

        assertEquals(1, profile.stats.losses)
        assertTrue(profile.achievements.contains(AchievementId.FIRST_LOSS))
    }

    @Test
    fun recordFlagPlacement_incrementsFlagsAndUnlocksFlagAchievement() {
        val profile = PlayerProfileManager.recordFlagPlaced(PlayerProfile())

        assertEquals(1, profile.stats.flagsPlaced)
        assertTrue(profile.achievements.contains(AchievementId.FIRST_FLAG))
    }
}

