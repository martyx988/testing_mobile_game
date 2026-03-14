package com.martyx988.minesweeper.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.martyx988.minesweeper.domain.BoardConfig
import com.martyx988.minesweeper.domain.ClassicBoardPresets
import com.martyx988.minesweeper.domain.ClassicGameEngine
import com.martyx988.minesweeper.domain.ClassicGameState
import com.martyx988.minesweeper.domain.Coordinate
import kotlin.random.Random

class ClassicGameController(
    initialConfig: BoardConfig = ClassicBoardPresets.easy(seed = Random.nextLong()),
    initialState: ClassicGameState? = null,
    private val nextSeed: () -> Long = { Random.nextLong() },
) {
    private val baseConfig: BoardConfig = initialState?.board?.config ?: initialConfig

    var gameState by mutableStateOf(
        initialState ?: ClassicGameEngine.start(initialConfig),
    )
        private set

    fun reveal(coordinate: Coordinate) {
        gameState = ClassicGameEngine.reveal(gameState, coordinate)
    }

    fun toggleFlag(coordinate: Coordinate) {
        gameState = ClassicGameEngine.toggleFlag(gameState, coordinate)
    }

    fun restart() {
        gameState = ClassicGameEngine.restart(
            state = gameState,
            nextSeed = nextSeed(),
        )
    }

    fun restartWithCurrentSeed() {
        gameState = ClassicGameEngine.start(baseConfig.copy(seed = gameState.board.config.seed))
    }
}

