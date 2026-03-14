package com.martyx988.minesweeper.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ClassicGameEngineTest {
    @Test
    fun reveal_numberedTile_revealsOnlyThatTile() {
        val state = ClassicGameEngine.fromBoard(testBoard(mineCoordinates = setOf(Coordinate(0, 0))))

        val updated = ClassicGameEngine.reveal(state, Coordinate(0, 1))

        assertEquals(MatchStatus.ACTIVE, updated.status)
        assertEquals(CellVisibility.REVEALED, updated.cellStateAt(Coordinate(0, 1)).visibility)
        assertEquals(CellVisibility.HIDDEN, updated.cellStateAt(Coordinate(1, 1)).visibility)
    }

    @Test
    fun reveal_emptyTile_floodFillsConnectedAreaAndBoundaryNumbers() {
        val state = ClassicGameEngine.fromBoard(testBoard(mineCoordinates = setOf(Coordinate(2, 2))))

        val updated = ClassicGameEngine.reveal(state, Coordinate(0, 0))

        val hiddenSafeCells = updated.allCellStates
            .filter { it.cell.hazard == CellHazard.NONE && it.visibility != CellVisibility.REVEALED }

        assertTrue(hiddenSafeCells.isEmpty())
    }

    @Test
    fun toggleFlag_marksAndUnmarksHiddenTiles() {
        val state = ClassicGameEngine.fromBoard(testBoard(mineCoordinates = setOf(Coordinate(2, 2))))

        val flagged = ClassicGameEngine.toggleFlag(state, Coordinate(0, 0))
        val unflagged = ClassicGameEngine.toggleFlag(flagged, Coordinate(0, 0))

        assertEquals(CellVisibility.FLAGGED, flagged.cellStateAt(Coordinate(0, 0)).visibility)
        assertEquals(CellVisibility.HIDDEN, unflagged.cellStateAt(Coordinate(0, 0)).visibility)
    }

    @Test
    fun reveal_mine_endsTheGameAndRevealsAllMines() {
        val state = ClassicGameEngine.fromBoard(
            testBoard(
                mineCoordinates = setOf(
                    Coordinate(0, 0),
                    Coordinate(2, 2),
                ),
            ),
        )

        val updated = ClassicGameEngine.reveal(state, Coordinate(0, 0))

        assertEquals(MatchStatus.LOST, updated.status)
        assertTrue(updated.cellStateAt(Coordinate(0, 0)).isDetonated)
        assertEquals(CellVisibility.REVEALED, updated.cellStateAt(Coordinate(2, 2)).visibility)
    }

    @Test
    fun completedGame_blocksFurtherFlagChanges() {
        val state = ClassicGameEngine.fromBoard(testBoard(mineCoordinates = setOf(Coordinate(0, 0))))
        val lost = ClassicGameEngine.reveal(state, Coordinate(0, 0))

        val afterFlagAttempt = ClassicGameEngine.toggleFlag(lost, Coordinate(1, 1))

        assertEquals(lost, afterFlagAttempt)
    }

    @Test
    fun reveal_allSafeCells_marksGameWon() {
        val state = ClassicGameEngine.fromBoard(testBoard(mineCoordinates = setOf(Coordinate(2, 2))))

        val updated = ClassicGameEngine.reveal(state, Coordinate(0, 0))

        assertEquals(MatchStatus.WON, updated.status)
    }

    @Test
    fun restart_createsFreshActiveGameWithNewSeed() {
        val initial = ClassicGameEngine.start(ClassicBoardPresets.easy(seed = 10L))
        val restarted = ClassicGameEngine.restart(initial, nextSeed = 11L)

        assertEquals(MatchStatus.ACTIVE, restarted.status)
        assertEquals(11L, restarted.board.config.seed)
        assertTrue(restarted.allCellStates.all { it.visibility == CellVisibility.HIDDEN })
        assertFalse(initial.board.mineCoordinates == restarted.board.mineCoordinates)
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
