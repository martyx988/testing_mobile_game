package com.martyx988.minesweeper.domain

data class BoardConfig(
    val rows: Int,
    val columns: Int,
    val mineCount: Int,
    val seed: Long,
    val mode: GameMode,
) {
    init {
        require(rows > 0) { "rows must be greater than zero" }
        require(columns > 0) { "columns must be greater than zero" }
        require(mineCount in 1 until rows * columns) {
            "mineCount must be between 1 and rows * columns - 1"
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

