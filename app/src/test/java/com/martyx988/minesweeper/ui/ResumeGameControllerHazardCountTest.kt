package com.martyx988.minesweeper.ui

import com.martyx988.minesweeper.data.PlayerProfileStore
import com.martyx988.minesweeper.data.SessionSnapshotStore
import com.martyx988.minesweeper.data.SnapshotLoadResult
import com.martyx988.minesweeper.domain.BoardConfig
import com.martyx988.minesweeper.domain.CellHazard
import com.martyx988.minesweeper.domain.ClassicGameSnapshot
import com.martyx988.minesweeper.domain.GameMode
import com.martyx988.minesweeper.domain.MatchStatus
import com.martyx988.minesweeper.domain.PlayerProfile
import org.junit.Assert.assertEquals
import org.junit.Test

class ResumeGameControllerHazardCountTest {
    @Test
    fun toggleFlag_updatesDisplayedHazardCountWhileMatchIsActive() {
        val controller = controllerForSingleMineBoard()

        controller.toggleFlag(controller.gameState.board.allCells.first().coordinate)

        assertEquals(0, controller.displayedHazardCount)
    }

    @Test
    fun losingMatch_keepsLastActiveDisplayedHazardCount() {
        val controller = controllerForSingleMineBoard()
        val mineCoordinate = controller.gameState.board.allCells.first { it.hazard == CellHazard.MINE }.coordinate
        val safeCoordinate = controller.gameState.board.allCells.first { it.hazard == CellHazard.NONE }.coordinate

        controller.toggleFlag(safeCoordinate)
        val lastActiveHazardCount = controller.displayedHazardCount

        controller.reveal(mineCoordinate)

        assertEquals(MatchStatus.LOST, controller.gameState.status)
        assertEquals(lastActiveHazardCount, controller.displayedHazardCount)
    }

    @Test
    fun winningMatch_keepsLastActiveDisplayedHazardCount() {
        val controller = controllerForSingleMineBoard()
        val mineCoordinate = controller.gameState.board.allCells.first { it.hazard == CellHazard.MINE }.coordinate
        val safeCoordinate = controller.gameState.board.allCells.first { it.hazard == CellHazard.NONE }.coordinate

        controller.toggleFlag(mineCoordinate)
        val lastActiveHazardCount = controller.displayedHazardCount

        controller.reveal(safeCoordinate)

        assertEquals(MatchStatus.WON, controller.gameState.status)
        assertEquals(lastActiveHazardCount, controller.displayedHazardCount)
    }

    private fun controllerForSingleMineBoard(): ResumeGameController = ResumeGameController(
        storage = EmptyHazardSnapshotStore,
        profileStore = EmptyHazardProfileStore,
        initialConfig = BoardConfig(
            rows = 1,
            columns = 2,
            mineCount = 1,
            seed = 7L,
            mode = GameMode.CLASSIC_EASY,
        ),
        nextSeed = { 8L },
    )
}

private object EmptyHazardSnapshotStore : SessionSnapshotStore {
    override fun load(): SnapshotLoadResult = SnapshotLoadResult.Missing

    override fun save(snapshot: ClassicGameSnapshot) = Unit

    override fun clear() = Unit
}

private object EmptyHazardProfileStore : PlayerProfileStore {
    override fun load(): PlayerProfile = PlayerProfile()

    override fun save(profile: PlayerProfile) = Unit
}
