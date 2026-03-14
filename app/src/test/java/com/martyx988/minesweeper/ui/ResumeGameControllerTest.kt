package com.martyx988.minesweeper.ui

import com.martyx988.minesweeper.data.SessionSnapshotStore
import com.martyx988.minesweeper.data.SnapshotLoadResult
import com.martyx988.minesweeper.domain.ClassicBoardPresets
import com.martyx988.minesweeper.domain.ClassicGameEngine
import com.martyx988.minesweeper.domain.Coordinate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ResumeGameControllerTest {
    @Test
    fun init_withSavedSnapshot_showsResumePrompt() {
        val savedState = ClassicGameEngine.start(ClassicBoardPresets.easy(seed = 12L))
        val store = FakeSessionSnapshotStore(
            loadResult = SnapshotLoadResult.Available(ClassicGameEngine.snapshot(savedState)),
        )

        val controller = ResumeGameController(
            storage = store,
            initialConfig = ClassicBoardPresets.easy(seed = 20L),
            nextSeed = { 21L },
        )

        assertNotNull(controller.pendingResumeSnapshot)
    }

    @Test
    fun dismissResume_clearsStoredSnapshotAndPrompt() {
        val savedState = ClassicGameEngine.start(ClassicBoardPresets.easy(seed = 12L))
        val store = FakeSessionSnapshotStore(
            loadResult = SnapshotLoadResult.Available(ClassicGameEngine.snapshot(savedState)),
        )
        val controller = ResumeGameController(
            storage = store,
            initialConfig = ClassicBoardPresets.easy(seed = 20L),
            nextSeed = { 21L },
        )

        controller.dismissResume()

        assertNull(controller.pendingResumeSnapshot)
        assertTrue(store.wasCleared)
    }

    @Test
    fun confirmResume_restoresSavedState() {
        var savedState = ClassicGameEngine.start(ClassicBoardPresets.easy(seed = 12L))
        savedState = ClassicGameEngine.toggleFlag(savedState, Coordinate(0, 0))
        val store = FakeSessionSnapshotStore(
            loadResult = SnapshotLoadResult.Available(ClassicGameEngine.snapshot(savedState)),
        )
        val controller = ResumeGameController(
            storage = store,
            initialConfig = ClassicBoardPresets.easy(seed = 20L),
            nextSeed = { 21L },
        )

        controller.confirmResume()

        assertNull(controller.pendingResumeSnapshot)
        assertEquals(savedState.board.config.seed, controller.gameState.board.config.seed)
        assertEquals(
            savedState.cellStateAt(Coordinate(0, 0)).visibility,
            controller.gameState.cellStateAt(Coordinate(0, 0)).visibility,
        )
    }

    @Test
    fun init_withCorruptedSnapshot_setsRecoverableNoticeAndClearsStorage() {
        val store = FakeSessionSnapshotStore(
            loadResult = SnapshotLoadResult.Corrupted("broken"),
        )

        val controller = ResumeGameController(
            storage = store,
            initialConfig = ClassicBoardPresets.easy(seed = 20L),
            nextSeed = { 21L },
        )

        assertNotNull(controller.resumeNotice)
        assertTrue(store.wasCleared)
    }
}

private class FakeSessionSnapshotStore(
    private val loadResult: SnapshotLoadResult,
) : SessionSnapshotStore {
    var savedSnapshotCount: Int = 0
    var wasCleared: Boolean = false

    override fun load(): SnapshotLoadResult = loadResult

    override fun save(snapshot: com.martyx988.minesweeper.domain.ClassicGameSnapshot) {
        savedSnapshotCount += 1
    }

    override fun clear() {
        wasCleared = true
    }
}
