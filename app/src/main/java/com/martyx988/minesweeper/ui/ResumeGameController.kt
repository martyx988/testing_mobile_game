package com.martyx988.minesweeper.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.martyx988.minesweeper.data.PlayerProfileStore
import com.martyx988.minesweeper.data.SessionSnapshotStore
import com.martyx988.minesweeper.data.SnapshotLoadResult
import com.martyx988.minesweeper.domain.BoardConfig
import com.martyx988.minesweeper.domain.CellVisibility
import com.martyx988.minesweeper.domain.ClassicBoardPresets
import com.martyx988.minesweeper.domain.ClassicGameEngine
import com.martyx988.minesweeper.domain.ClassicGameSnapshot
import com.martyx988.minesweeper.domain.Coordinate
import com.martyx988.minesweeper.domain.GameMode
import com.martyx988.minesweeper.domain.MatchStatus
import com.martyx988.minesweeper.domain.PlayerProfile
import com.martyx988.minesweeper.domain.PlayerProfileManager
import com.martyx988.minesweeper.domain.TrapTilesBoardPresets
import kotlin.random.Random

class ResumeGameController(
    private val storage: SessionSnapshotStore,
    private val profileStore: PlayerProfileStore,
    initialConfig: BoardConfig = ClassicBoardPresets.easy(seed = Random.nextLong()),
    private val nextSeed: () -> Long = { Random.nextLong() },
    private val configFactory: (GameMode, Long) -> BoardConfig = ::defaultConfigForMode,
) {
    private var currentMode: GameMode = initialConfig.mode

    var gameState by mutableStateOf(ClassicGameEngine.start(initialConfig))
        private set

    var pendingResumeSnapshot by mutableStateOf<ClassicGameSnapshot?>(null)
        private set

    var resumeNotice by mutableStateOf<String?>(null)
        private set

    var isHelpOpen by mutableStateOf(false)
        private set

    var displayedHazardCount by mutableStateOf(hazardCountFor(gameState))
        private set

    var playerProfile by mutableStateOf(profileStore.load())
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
        val lastActiveHazardCount = displayedHazardCount
        val previousStatus = gameState.status
        gameState = ClassicGameEngine.reveal(gameState, coordinate)
        if (gameState.status == MatchStatus.ACTIVE) {
            syncDisplayedCountsFromState()
        } else {
            displayedHazardCount = lastActiveHazardCount
        }
        recordCompletionIfNeeded(previousStatus)
        syncSnapshotAfterInteraction()
    }

    fun toggleFlag(coordinate: Coordinate) {
        val previousVisibility = gameState.cellStateAt(coordinate).visibility
        gameState = ClassicGameEngine.toggleFlag(gameState, coordinate)
        if (previousVisibility != gameState.cellStateAt(coordinate).visibility &&
            gameState.cellStateAt(coordinate).visibility == CellVisibility.FLAGGED
        ) {
            playerProfile = PlayerProfileManager.recordFlagPlaced(playerProfile)
            persistProfile()
        }
        syncDisplayedCountsFromState()
        syncSnapshotAfterInteraction()
    }

    fun restart() {
        gameState = ClassicGameEngine.start(configFactory(currentMode, nextSeed()))
        syncDisplayedCountsFromState()
        syncSnapshotAfterInteraction()
    }

    fun restartWithCurrentSeed() {
        gameState = ClassicGameEngine.start(configFactory(currentMode, gameState.board.config.seed))
        syncDisplayedCountsFromState()
        syncSnapshotAfterInteraction()
    }

    fun confirmResume() {
        val snapshot = pendingResumeSnapshot ?: return
        gameState = ClassicGameEngine.restore(snapshot)
        currentMode = snapshot.config.mode
        pendingResumeSnapshot = null
        syncDisplayedCountsFromState()
        syncSnapshotAfterInteraction()
    }

    fun dismissResume() {
        pendingResumeSnapshot = null
        storage.clear()
    }

    fun dismissResumeNotice() {
        resumeNotice = null
    }

    fun openHelp() {
        isHelpOpen = true
    }

    fun closeHelp() {
        isHelpOpen = false
    }

    fun cycleTheme() {
        playerProfile = PlayerProfileManager.cycleTheme(playerProfile)
        persistProfile()
    }

    fun setHapticsEnabled(enabled: Boolean) {
        playerProfile = PlayerProfileManager.setHapticsEnabled(playerProfile, enabled)
        persistProfile()
    }

    fun switchMode(mode: GameMode) {
        currentMode = mode
        gameState = ClassicGameEngine.start(configFactory(mode, nextSeed()))
        syncDisplayedCountsFromState()
        syncSnapshotAfterInteraction()
    }

    private fun syncDisplayedCountsFromState() {
        displayedHazardCount = hazardCountFor(gameState)
    }

    private fun syncSnapshotAfterInteraction() {
        when (gameState.status) {
            MatchStatus.ACTIVE -> storage.save(ClassicGameEngine.snapshot(gameState))
            MatchStatus.WON,
            MatchStatus.LOST,
            -> storage.clear()
        }
    }

    private fun recordCompletionIfNeeded(previousStatus: MatchStatus) {
        if (previousStatus == MatchStatus.ACTIVE && gameState.status != MatchStatus.ACTIVE) {
            playerProfile = PlayerProfileManager.recordGameFinished(
                current = playerProfile,
                result = gameState.status,
            )
            persistProfile()
        }
    }

    private fun persistProfile() {
        profileStore.save(playerProfile)
    }

    private companion object {
        fun hazardCountFor(state: com.martyx988.minesweeper.domain.ClassicGameState): Int =
            state.board.config.mineCount + state.board.config.trapCount -
                state.allCellStates.count { it.visibility == CellVisibility.FLAGGED }

        fun defaultConfigForMode(
            mode: GameMode,
            seed: Long,
        ): BoardConfig = when (mode) {
            GameMode.CLASSIC_EASY -> ClassicBoardPresets.easy(seed)
            GameMode.TRAP_TILES -> TrapTilesBoardPresets.standard(seed)
        }
    }
}
