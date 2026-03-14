package com.martyx988.minesweeper.domain

data class Coordinate(
    val row: Int,
    val column: Int,
) {
    fun neighbors(
        rows: Int,
        columns: Int,
    ): List<Coordinate> = buildList {
        for (rowOffset in -1..1) {
            for (columnOffset in -1..1) {
                if (rowOffset == 0 && columnOffset == 0) {
                    continue
                }

                val nextRow = row + rowOffset
                val nextColumn = column + columnOffset
                if (nextRow in 0 until rows && nextColumn in 0 until columns) {
                    add(Coordinate(nextRow, nextColumn))
                }
            }
        }
    }
}

enum class GameMode {
    CLASSIC_EASY,
    TRAP_TILES,
}

enum class CellHazard {
    NONE,
    MINE,
    TRAP,
}

data class BoardCell(
    val coordinate: Coordinate,
    val hazard: CellHazard,
    val adjacentMineCount: Int,
)

data class MinefieldBoard(
    val config: BoardConfig,
    val cells: List<List<BoardCell>>,
) {
    val rows: Int = cells.size
    val columns: Int = cells.firstOrNull()?.size ?: 0
    val allCells: List<BoardCell> = cells.flatten()
    val mineCoordinates: Set<Coordinate> = allCells
        .filter { it.hazard == CellHazard.MINE }
        .map { it.coordinate }
        .toSet()

    fun cellAt(coordinate: Coordinate): BoardCell {
        require(coordinate.row in 0 until rows) { "row out of bounds: ${coordinate.row}" }
        require(coordinate.column in 0 until columns) { "column out of bounds: ${coordinate.column}" }
        return cells[coordinate.row][coordinate.column]
    }

    fun neighborsOf(coordinate: Coordinate): List<Coordinate> = coordinate.neighbors(
        rows = rows,
        columns = columns,
    )
}
