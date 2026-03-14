package com.martyx988.minesweeper.ui

import com.martyx988.minesweeper.data.PlayerProfileStore
import com.martyx988.minesweeper.data.SessionSnapshotStore
import com.martyx988.minesweeper.data.SnapshotLoadResult
import com.martyx988.minesweeper.domain.AppThemeChoice
import com.martyx988.minesweeper.domain.ClassicBoardPresets
import com.martyx988.minesweeper.domain.PlayerProfile
import com.martyx988.minesweeper.domain.PlayerSettings
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ResumeGameControllerSettingsTest {
    @Test
    fun init_loadsThemeAndSettingsFromProfileStore() {
        val profileStore = FakePlayerProfileStore(
            profile = PlayerProfile(
                settings = PlayerSettings(
                    themeChoice = AppThemeChoice.SUNSET,
                    hapticsEnabled = false,
                ),
            ),
        )

        val controller = ResumeGameController(
            storage = EmptySessionStore,
            profileStore = profileStore,
            initialConfig = ClassicBoardPresets.easy(seed = 1L),
            nextSeed = { 2L },
        )

        assertEquals(AppThemeChoice.SUNSET, controller.playerProfile.settings.themeChoice)
        assertEquals(false, controller.playerProfile.settings.hapticsEnabled)
    }

    @Test
    fun cycleTheme_persistsUpdatedThemeChoice() {
        val profileStore = FakePlayerProfileStore(PlayerProfile())
        val controller = ResumeGameController(
            storage = EmptySessionStore,
            profileStore = profileStore,
            initialConfig = ClassicBoardPresets.easy(seed = 1L),
            nextSeed = { 2L },
        )

        controller.cycleTheme()

        assertEquals(AppThemeChoice.FOREST, controller.playerProfile.settings.themeChoice)
        assertTrue(profileStore.saveCount > 0)
    }

    @Test
    fun setHapticsEnabled_persistsUpdatedPreference() {
        val profileStore = FakePlayerProfileStore(PlayerProfile())
        val controller = ResumeGameController(
            storage = EmptySessionStore,
            profileStore = profileStore,
            initialConfig = ClassicBoardPresets.easy(seed = 1L),
            nextSeed = { 2L },
        )

        controller.setHapticsEnabled(false)

        assertEquals(false, controller.playerProfile.settings.hapticsEnabled)
        assertTrue(profileStore.saveCount > 0)
    }
}

private object EmptySessionStore : SessionSnapshotStore {
    override fun load(): SnapshotLoadResult = SnapshotLoadResult.Missing
    override fun save(snapshot: com.martyx988.minesweeper.domain.ClassicGameSnapshot) = Unit
    override fun clear() = Unit
}

private class FakePlayerProfileStore(
    private var profile: PlayerProfile,
) : PlayerProfileStore {
    var saveCount: Int = 0

    override fun load(): PlayerProfile = profile

    override fun save(profile: PlayerProfile) {
        this.profile = profile
        saveCount += 1
    }
}
