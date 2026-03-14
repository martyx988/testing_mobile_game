package com.martyx988.minesweeper.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.martyx988.minesweeper.data.SessionSnapshotStore
import com.martyx988.minesweeper.data.SnapshotLoadResult
import com.martyx988.minesweeper.domain.BoardConfig
import com.martyx988.minesweeper.domain.ClassicBoardPresets
import com.martyx988.minesweeper.domain.ClassicGameEngine
import com.martyx988.minesweeper.domain.ClassicGameSnapshot
import com.martyx988.minesweeper.domain.ClassicGameState
import com.martyx988.minesweeper.domain.Coordinate
import com.martyx988.minesweeper.domain.MatchStatus
import kotlin.random.Random

class ResumeGameController(
    private val storage: SessionSnapshotStore,
    initialConfig: BoardConfig = ClassicBoardPresets.easy(seed = Random.nextLong()),
    private val nextSeed: () -> Long = { Random.nextLong() },
) {
    private val baseConfig = initialConfig

    var gameState by mutableStateOf(ClassicGameEngine.start(initialConfig))
        private set

    var pendingResumeSnapshot by mutableStateOf<ClassicGameSnapshot?>(null)
        private set

    var resumeNotice by mutableStateOf<String?>(null)
        private set

    init {
        when (val result = storage.load()) {
            is SnapshotLoadResult.Available -> pendingResumeSnapshot = result.snapshot
            is SnapshotLoadResult.Corrupted -> {
                resumeNotice = "Saved game data was invalid and has been cleared."
                storage.clear()
            }
            SnapshotLoadResult.Missing -> Unit
        }
    }

    fun reveal(coordinate: Coordinate) {
        gameState = ClassicGameEngine.reveal(gameState, coordinate)
        syncSnapshotAfterInteraction()
    }

    fun toggleFlag(coordinate: Coordinate) {
        gameState = ClassicGameEngine.toggleFlag(gameState, coordinate)
        syncSnapshotAfterInteraction()
    }

    fun restart() {
        gameState = ClassicGameEngine.restart(gameState, nextSeed())
        syncSnapshotAfterInteraction()
    }

    fun restartWithCurrentSeed() {
        gameState = ClassicGameEngine.start(baseConfig.copy(seed = gameState.board.config.seed))
        syncSnapshotAfterInteraction()
    }

    fun confirmResume() {
        val snapshot = pendingResumeSnapshot ?: return
        gameState = ClassicGameEngine.restore(snapshot)
        pendingResumeSnapshot = null
        syncSnapshotAfterInteraction()
    }

    fun dismissResume() {
        pendingResumeSnapshot = null
        storage.clear()
    }

    fun dismissResumeNotice() {
        resumeNotice = null
    }

    private fun syncSnapshotAfterInteraction() {
        when (gameState.status) {
            MatchStatus.ACTIVE -> storage.save(ClassicGameEngine.snapshot(gameState))
            MatchStatus.WON,
            MatchStatus.LOST,
            -> storage.clear()
        }
    }
}
