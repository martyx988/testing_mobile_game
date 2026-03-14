package com.martyx988.minesweeper.ui

import com.martyx988.minesweeper.domain.BoardConfig
import com.martyx988.minesweeper.domain.ClassicGameEngine
import com.martyx988.minesweeper.domain.ClassicGameState
import com.martyx988.minesweeper.domain.ClassicBoardPresets
import com.martyx988.minesweeper.domain.Coordinate
import com.martyx988.minesweeper.domain.GameMode
import com.martyx988.minesweeper.domain.MatchStatus
import com.martyx988.minesweeper.domain.MinefieldBoard
import com.martyx988.minesweeper.domain.BoardCell
import com.martyx988.minesweeper.domain.CellHazard
import com.martyx988.minesweeper.domain.CellVisibility
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ClassicGameControllerTest {
    @Test
    fun revealCell_updatesVisibleGameState() {
        val controller = ClassicGameController(
            initialState = ClassicGameEngine.fromBoard(testBoard(mineCoordinates = setOf(Coordinate(2, 2)))),
            nextSeed = { 99L },
        )

        controller.reveal(Coordinate(0, 0))

        assertEquals(MatchStatus.WON, controller.gameState.status)
        assertEquals(CellVisibility.REVEALED, controller.gameState.cellStateAt(Coordinate(0, 0)).visibility)
    }

    @Test
    fun toggleFlag_updatesVisibleGameState() {
        val controller = ClassicGameController(
            initialState = ClassicGameEngine.fromBoard(testBoard(mineCoordinates = setOf(Coordinate(2, 2)))),
            nextSeed = { 99L },
        )

        controller.toggleFlag(Coordinate(0, 0))

        assertEquals(CellVisibility.FLAGGED, controller.gameState.cellStateAt(Coordinate(0, 0)).visibility)
    }

    @Test
    fun restart_usesNextSeedToCreateFreshBoard() {
        val controller = ClassicGameController(
            initialConfig = ClassicBoardPresets.easy(seed = 40L),
            nextSeed = { 41L },
        )
        val firstMineLayout = controller.gameState.board.mineCoordinates

        controller.restart()

        assertEquals(41L, controller.gameState.board.config.seed)
        assertTrue(controller.gameState.allCellStates.all { it.visibility == CellVisibility.HIDDEN })
        assertFalse(firstMineLayout == controller.gameState.board.mineCoordinates)
    }

    private fun testBoard(mineCoordinates: Set<Coordinate>): MinefieldBoard {
        val config = BoardConfig(
            rows = 3,
            columns = 3,
            mineCount = mineCoordinates.size,
            seed = 0L,
            mode = GameMode.CLASSIC_EASY,
        )

        val cells = List(config.rows) { row ->
            List(config.columns) { column ->
                val coordinate = Coordinate(row, column)
                val isMine = coordinate in mineCoordinates
                val adjacentMineCount = if (isMine) {
                    0
                } else {
                    coordinate.neighbors(config.rows, config.columns)
                        .count { neighbor -> neighbor in mineCoordinates }
                }

                BoardCell(
                    coordinate = coordinate,
                    hazard = if (isMine) CellHazard.MINE else CellHazard.NONE,
                    adjacentMineCount = adjacentMineCount,
                )
            }
        }

        return MinefieldBoard(config = config, cells = cells)
    }
}
