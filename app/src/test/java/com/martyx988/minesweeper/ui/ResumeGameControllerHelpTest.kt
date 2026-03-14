package com.martyx988.minesweeper.ui

import com.martyx988.minesweeper.data.PlayerProfileStore
import com.martyx988.minesweeper.data.SessionSnapshotStore
import com.martyx988.minesweeper.data.SnapshotLoadResult
import com.martyx988.minesweeper.domain.ClassicBoardPresets
import com.martyx988.minesweeper.domain.ClassicGameSnapshot
import com.martyx988.minesweeper.domain.PlayerProfile
import org.junit.Assert.assertEquals
import org.junit.Test

class ResumeGameControllerHelpTest {
    @Test
    fun helpDialog_canBeOpenedAndClosed() {
        val controller = ResumeGameController(
            storage = EmptyHelpSnapshotStore,
            profileStore = EmptyHelpProfileStore,
            initialConfig = ClassicBoardPresets.easy(seed = 1L),
            nextSeed = { 2L },
        )

        controller.openHelp()
        assertEquals(true, controller.isHelpOpen)

        controller.closeHelp()
        assertEquals(false, controller.isHelpOpen)
    }
}

private object EmptyHelpSnapshotStore : SessionSnapshotStore {
    override fun load(): SnapshotLoadResult = SnapshotLoadResult.Missing

    override fun save(snapshot: ClassicGameSnapshot) = Unit

    override fun clear() = Unit
}

private object EmptyHelpProfileStore : PlayerProfileStore {
    override fun load(): PlayerProfile = PlayerProfile()

    override fun save(profile: PlayerProfile) = Unit
}
