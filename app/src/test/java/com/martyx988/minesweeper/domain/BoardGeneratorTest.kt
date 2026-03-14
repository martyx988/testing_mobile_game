package com.martyx988.minesweeper.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BoardGeneratorTest {
    @Test
    fun generate_returnsEasyBoardWithExpectedShapeAndMineCount() {
        val board = BoardGenerator.generate(ClassicBoardPresets.easy(seed = 11L))

        assertEquals(8, board.rows)
        assertEquals(8, board.columns)
        assertEquals(10, board.mineCoordinates.size)
        assertEquals(64, board.allCells.size)
    }

    @Test
    fun generate_isDeterministicForTheSameSeed() {
        val first = BoardGenerator.generate(ClassicBoardPresets.easy(seed = 2026L))
        val second = BoardGenerator.generate(ClassicBoardPresets.easy(seed = 2026L))

        assertEquals(first.mineCoordinates, second.mineCoordinates)
        assertEquals(
            first.allCells.map { it.coordinate to it.adjacentMineCount },
            second.allCells.map { it.coordinate to it.adjacentMineCount },
        )
    }

    @Test
    fun generate_changesMineLayoutWhenSeedChanges() {
        val first = BoardGenerator.generate(ClassicBoardPresets.easy(seed = 1L))
        val second = BoardGenerator.generate(ClassicBoardPresets.easy(seed = 2L))

        assertNotEquals(first.mineCoordinates, second.mineCoordinates)
    }

    @Test
    fun generate_assignsCorrectAdjacencyCountsAroundEveryNonMineCell() {
        val board = BoardGenerator.generate(ClassicBoardPresets.easy(seed = 777L))

        board.allCells
            .filter { it.hazard == CellHazard.NONE }
            .forEach { cell ->
                val manualCount = board.neighborsOf(cell.coordinate)
                    .count { neighbor -> board.cellAt(neighbor).hazard == CellHazard.MINE }

                assertEquals(cell.coordinate.toString(), manualCount, cell.adjacentMineCount)
            }
    }

    @Test
    fun generate_marksAllMineCellsExplicitly() {
        val board = BoardGenerator.generate(ClassicBoardPresets.easy(seed = 55L))

        assertTrue(board.mineCoordinates.isNotEmpty())
        board.mineCoordinates.forEach { coordinate ->
            assertEquals(CellHazard.MINE, board.cellAt(coordinate).hazard)
        }
    }
}
