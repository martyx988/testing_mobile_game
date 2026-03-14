package com.martyx988.minesweeper.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TrapTilesModeTest {
    @Test
    fun trapTilesPreset_generatesExpectedHazardCounts() {
        val board = BoardGenerator.generate(TrapTilesBoardPresets.standard(seed = 42L))

        assertEquals(GameMode.TRAP_TILES, board.config.mode)
        assertEquals(8, board.mineCoordinates.size)
        assertEquals(4, board.trapCoordinates.size)
    }

    @Test
    fun trapTilesPreset_isDeterministicForTheSameSeed() {
        val first = BoardGenerator.generate(TrapTilesBoardPresets.standard(seed = 42L))
        val second = BoardGenerator.generate(TrapTilesBoardPresets.standard(seed = 42L))

        assertEquals(first.mineCoordinates, second.mineCoordinates)
        assertEquals(first.trapCoordinates, second.trapCoordinates)
    }

    @Test
    fun reveal_trapTile_endsTheGameAndMarksDetonation() {
        val board = MinefieldBoard(
            config = BoardConfig(
                rows = 3,
                columns = 3,
                mineCount = 1,
                trapCount = 1,
                seed = 0L,
                mode = GameMode.TRAP_TILES,
            ),
            cells = listOf(
                listOf(
                    BoardCell(Coordinate(0, 0), CellHazard.TRAP, 0),
                    BoardCell(Coordinate(0, 1), CellHazard.NONE, 2),
                    BoardCell(Coordinate(0, 2), CellHazard.NONE, 1),
                ),
                listOf(
                    BoardCell(Coordinate(1, 0), CellHazard.NONE, 2),
                    BoardCell(Coordinate(1, 1), CellHazard.NONE, 2),
                    BoardCell(Coordinate(1, 2), CellHazard.NONE, 1),
                ),
                listOf(
                    BoardCell(Coordinate(2, 0), CellHazard.NONE, 1),
                    BoardCell(Coordinate(2, 1), CellHazard.NONE, 1),
                    BoardCell(Coordinate(2, 2), CellHazard.MINE, 0),
                ),
            ),
        )
        val state = ClassicGameEngine.fromBoard(board)

        val updated = ClassicGameEngine.reveal(state, Coordinate(0, 0))

        assertEquals(MatchStatus.LOST, updated.status)
        assertTrue(updated.cellStateAt(Coordinate(0, 0)).isDetonated)
        assertEquals(CellVisibility.REVEALED, updated.cellStateAt(Coordinate(2, 2)).visibility)
    }
}
