package com.martyx988.minesweeper.domain

data class BoardConfig(
    val rows: Int,
    val columns: Int,
    val mineCount: Int,
    val trapCount: Int = 0,
    val seed: Long,
    val mode: GameMode,
) {
    init {
        require(rows > 0) { "rows must be greater than zero" }
        require(columns > 0) { "columns must be greater than zero" }
        require(mineCount >= 1) { "mineCount must be at least 1" }
        require(trapCount >= 0) { "trapCount must not be negative" }
        require(mineCount + trapCount < rows * columns) {
            "total hazards must be less than rows * columns"
        }
    }
}

object ClassicBoardPresets {
    fun easy(seed: Long): BoardConfig = BoardConfig(
        rows = 8,
        columns = 8,
        mineCount = 10,
        seed = seed,
        mode = GameMode.CLASSIC_EASY,
    )
}

object TrapTilesBoardPresets {
    fun standard(seed: Long): BoardConfig = BoardConfig(
        rows = 8,
        columns = 8,
        mineCount = 8,
        trapCount = 4,
        seed = seed,
        mode = GameMode.TRAP_TILES,
    )
}
