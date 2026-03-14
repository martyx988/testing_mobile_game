package com.martyx988.minesweeper.domain

import java.util.Random

object BoardGenerator {
    fun generate(config: BoardConfig): MinefieldBoard {
        val coordinates = buildList {
            repeat(config.rows) { row ->
                repeat(config.columns) { column ->
                    add(Coordinate(row, column))
                }
            }
        }.toMutableList()

        shuffleInPlace(coordinates, config.seed)
        val mineCoordinates = coordinates
            .take(config.mineCount)
            .toSet()
        val trapCoordinates = coordinates
            .drop(config.mineCount)
            .take(config.trapCount)
            .toSet()
        val countedHazards = mineCoordinates + trapCoordinates

        val cells = List(config.rows) { row ->
            List(config.columns) { column ->
                val coordinate = Coordinate(row, column)
                val hazard = when {
                    coordinate in mineCoordinates -> CellHazard.MINE
                    coordinate in trapCoordinates -> CellHazard.TRAP
                    else -> CellHazard.NONE
                }

                val adjacentMineCount = if (hazard != CellHazard.NONE) {
                    0
                } else {
                    coordinate.neighbors(
                        rows = config.rows,
                        columns = config.columns,
                    ).count { neighbor -> neighbor in countedHazards }
                }

                BoardCell(
                    coordinate = coordinate,
                    hazard = hazard,
                    adjacentMineCount = adjacentMineCount,
                )
            }
        }

        return MinefieldBoard(
            config = config,
            cells = cells,
        )
    }

    private fun shuffleInPlace(
        coordinates: MutableList<Coordinate>,
        seed: Long,
    ) {
        val random = Random(seed)
        for (index in coordinates.lastIndex downTo 1) {
            val swapIndex = random.nextInt(index + 1)
            val current = coordinates[index]
            coordinates[index] = coordinates[swapIndex]
            coordinates[swapIndex] = current
        }
    }
}
