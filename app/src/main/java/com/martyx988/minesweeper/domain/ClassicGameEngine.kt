package com.martyx988.minesweeper.domain

import java.util.ArrayDeque

enum class CellVisibility {
    HIDDEN,
    REVEALED,
    FLAGGED,
}

enum class MatchStatus {
    ACTIVE,
    WON,
    LOST,
}

data class GameCellState(
    val cell: BoardCell,
    val visibility: CellVisibility,
    val isDetonated: Boolean = false,
)

data class ClassicGameState(
    val board: MinefieldBoard,
    val status: MatchStatus,
    private val cellsByCoordinate: Map<Coordinate, GameCellState>,
) {
    val allCellStates: List<GameCellState> = board.allCells.map { cell ->
        cellsByCoordinate.getValue(cell.coordinate)
    }

    fun cellStateAt(coordinate: Coordinate): GameCellState = cellsByCoordinate.getValue(coordinate)

    internal fun replaceCells(updatedCells: Map<Coordinate, GameCellState>): ClassicGameState = copy(
        cellsByCoordinate = updatedCells,
    )
}

object ClassicGameEngine {
    fun start(config: BoardConfig): ClassicGameState = fromBoard(BoardGenerator.generate(config))

    fun fromBoard(board: MinefieldBoard): ClassicGameState = ClassicGameState(
        board = board,
        status = MatchStatus.ACTIVE,
        cellsByCoordinate = board.allCells.associate { cell ->
            cell.coordinate to GameCellState(
                cell = cell,
                visibility = CellVisibility.HIDDEN,
            )
        },
    )

    fun reveal(
        state: ClassicGameState,
        coordinate: Coordinate,
    ): ClassicGameState {
        if (state.status != MatchStatus.ACTIVE) {
            return state
        }

        val current = state.cellStateAt(coordinate)
        if (current.visibility != CellVisibility.HIDDEN) {
            return state
        }

        return when (current.cell.hazard) {
            CellHazard.MINE -> loseGame(state, detonatedAt = coordinate)
            CellHazard.NONE -> {
                val updatedCells = state.allCellStates
                    .associateBy { it.cell.coordinate }
                    .toMutableMap()

                revealFloodFill(
                    board = state.board,
                    updatedCells = updatedCells,
                    start = coordinate,
                )

                val nextStatus = if (updatedCells.values
                        .filter { it.cell.hazard != CellHazard.MINE }
                        .all { it.visibility == CellVisibility.REVEALED }
                ) {
                    MatchStatus.WON
                } else {
                    MatchStatus.ACTIVE
                }

                state.copy(
                    status = nextStatus,
                    cellsByCoordinate = updatedCells,
                )
            }
            CellHazard.TRAP -> state
        }
    }

    fun toggleFlag(
        state: ClassicGameState,
        coordinate: Coordinate,
    ): ClassicGameState {
        if (state.status != MatchStatus.ACTIVE) {
            return state
        }

        val current = state.cellStateAt(coordinate)
        if (current.visibility == CellVisibility.REVEALED) {
            return state
        }

        val nextVisibility = when (current.visibility) {
            CellVisibility.HIDDEN -> CellVisibility.FLAGGED
            CellVisibility.FLAGGED -> CellVisibility.HIDDEN
            CellVisibility.REVEALED -> CellVisibility.REVEALED
        }

        val updatedCells = state.allCellStates.associateBy { it.cell.coordinate }.toMutableMap()
        updatedCells[coordinate] = current.copy(visibility = nextVisibility)
        return state.replaceCells(updatedCells)
    }

    fun restart(
        state: ClassicGameState,
        nextSeed: Long,
    ): ClassicGameState = start(
        state.board.config.copy(seed = nextSeed),
    )

    private fun loseGame(
        state: ClassicGameState,
        detonatedAt: Coordinate,
    ): ClassicGameState {
        val updatedCells = state.allCellStates
            .associateBy { it.cell.coordinate }
            .mapValues { (_, gameCell) ->
                when (gameCell.cell.hazard) {
                    CellHazard.MINE -> gameCell.copy(
                        visibility = CellVisibility.REVEALED,
                        isDetonated = gameCell.cell.coordinate == detonatedAt,
                    )
                    else -> gameCell
                }
            }

        return state.copy(
            status = MatchStatus.LOST,
            cellsByCoordinate = updatedCells,
        )
    }

    private fun revealFloodFill(
        board: MinefieldBoard,
        updatedCells: MutableMap<Coordinate, GameCellState>,
        start: Coordinate,
    ) {
        val queue = ArrayDeque<Coordinate>()
        queue.add(start)

        while (queue.isNotEmpty()) {
            val coordinate = queue.removeFirst()
            val current = updatedCells.getValue(coordinate)
            if (current.visibility == CellVisibility.REVEALED || current.visibility == CellVisibility.FLAGGED) {
                continue
            }

            updatedCells[coordinate] = current.copy(visibility = CellVisibility.REVEALED)

            if (current.cell.adjacentMineCount != 0) {
                continue
            }

            board.neighborsOf(coordinate).forEach { neighbor ->
                val neighborState = updatedCells.getValue(neighbor)
                if (neighborState.visibility == CellVisibility.HIDDEN && neighborState.cell.hazard == CellHazard.NONE) {
                    queue.add(neighbor)
                }
            }
        }
    }
}
