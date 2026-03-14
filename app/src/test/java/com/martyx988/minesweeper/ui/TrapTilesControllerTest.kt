package com.martyx988.minesweeper.ui

import com.martyx988.minesweeper.data.PlayerProfileStore
import com.martyx988.minesweeper.data.SessionSnapshotStore
import com.martyx988.minesweeper.data.SnapshotLoadResult
import com.martyx988.minesweeper.domain.ClassicGameSnapshot
import com.martyx988.minesweeper.domain.GameMode
import com.martyx988.minesweeper.domain.PlayerProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class TrapTilesControllerTest {
    @Test
    fun switchMode_movesBetweenClassicAndTrapBoards() {
        val controller = ResumeGameController(
            storage = EmptySnapshotStore,
            profileStore = EmptyProfileStore,
            nextSeed = { 33L },
        )
        val classicMode = controller.gameState.board.config.mode

        controller.switchMode(GameMode.TRAP_TILES)

        assertEquals(GameMode.TRAP_TILES, controller.gameState.board.config.mode)
        assertNotEquals(classicMode, controller.gameState.board.config.mode)
        assertEquals(4, controller.gameState.board.config.trapCount)
    }
}

private object EmptySnapshotStore : SessionSnapshotStore {
    override fun load(): SnapshotLoadResult = SnapshotLoadResult.Missing
    override fun save(snapshot: ClassicGameSnapshot) = Unit
    override fun clear() = Unit
}

private object EmptyProfileStore : PlayerProfileStore {
    override fun load(): PlayerProfile = PlayerProfile()
    override fun save(profile: PlayerProfile) = Unit
}
