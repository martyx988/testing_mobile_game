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

data class ClassicGameSnapshot(
    val config: BoardConfig,
    val status: MatchStatus,
    val cells: List<GameCellSnapshot>,
)

data class GameCellSnapshot(
    val coordinate: Coordinate,
    val visibility: CellVisibility,
    val isDetonated: Boolean,
)

sealed interface SnapshotDecodeResult {
    data class Success(val snapshot: ClassicGameSnapshot) : SnapshotDecodeResult
    data class Corrupted(val rawValue: String?) : SnapshotDecodeResult
}

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

    fun snapshot(state: ClassicGameState): ClassicGameSnapshot = ClassicGameSnapshot(
        config = state.board.config,
        status = state.status,
        cells = state.allCellStates.map { gameCell ->
            GameCellSnapshot(
                coordinate = gameCell.cell.coordinate,
                visibility = gameCell.visibility,
                isDetonated = gameCell.isDetonated,
            )
        },
    )

    fun restore(snapshot: ClassicGameSnapshot): ClassicGameState {
        val board = BoardGenerator.generate(snapshot.config)
        val snapshotCells = snapshot.cells.associateBy { it.coordinate }
        require(snapshotCells.size == board.rows * board.columns) {
            "snapshot cell count does not match board dimensions"
        }

        val cellsByCoordinate = board.allCells.associate { boardCell ->
            val snapshotCell = requireNotNull(snapshotCells[boardCell.coordinate]) {
                "missing snapshot cell for ${boardCell.coordinate}"
            }

            boardCell.coordinate to GameCellState(
                cell = boardCell,
                visibility = snapshotCell.visibility,
                isDetonated = snapshotCell.isDetonated,
            )
        }

        return ClassicGameState(
            board = board,
            status = snapshot.status,
            cellsByCoordinate = cellsByCoordinate,
        )
    }

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

object ClassicGameSnapshotCodec {
    private const val headerPrefix = "MS1"
    private const val headerSeparator = "|"
    private const val cellSeparator = ";"
    private const val valueSeparator = ","

    fun encode(snapshot: ClassicGameSnapshot): String {
        val header = listOf(
            headerPrefix,
            snapshot.config.rows,
            snapshot.config.columns,
            snapshot.config.mineCount,
            snapshot.config.seed,
            snapshot.config.mode.name,
            snapshot.status.name,
        ).joinToString(separator = headerSeparator)

        val cells = snapshot.cells.joinToString(separator = cellSeparator) { cell ->
            listOf(
                cell.coordinate.row,
                cell.coordinate.column,
                cell.visibility.name,
                cell.isDetonated,
            ).joinToString(separator = valueSeparator)
        }

        return "$header\n$cells"
    }

    fun decode(rawValue: String?): SnapshotDecodeResult {
        if (rawValue.isNullOrBlank()) {
            return SnapshotDecodeResult.Corrupted(rawValue)
        }

        return try {
            val lines = rawValue.lines()
            val headerValues = lines.first().split(headerSeparator)
            require(headerValues.size == 7)
            require(headerValues.first() == headerPrefix)

            val config = BoardConfig(
                rows = headerValues[1].toInt(),
                columns = headerValues[2].toInt(),
                mineCount = headerValues[3].toInt(),
                seed = headerValues[4].toLong(),
                mode = GameMode.valueOf(headerValues[5]),
            )

            val cells = lines.drop(1)
                .joinToString(separator = "")
                .split(cellSeparator)
                .filter { it.isNotBlank() }
                .map { token ->
                    val values = token.split(valueSeparator)
                    require(values.size == 4)
                    GameCellSnapshot(
                        coordinate = Coordinate(
                            row = values[0].toInt(),
                            column = values[1].toInt(),
                        ),
                        visibility = CellVisibility.valueOf(values[2]),
                        isDetonated = values[3].toBooleanStrict(),
                    )
                }

            SnapshotDecodeResult.Success(
                snapshot = ClassicGameSnapshot(
                    config = config,
                    status = MatchStatus.valueOf(headerValues[6]),
                    cells = cells,
                ),
            )
        } catch (_: IllegalArgumentException) {
            SnapshotDecodeResult.Corrupted(rawValue)
        } catch (_: IllegalStateException) {
            SnapshotDecodeResult.Corrupted(rawValue)
        }
    }
}
