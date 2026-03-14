package com.martyx988.minesweeper.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ClassicGameSnapshotCodecTest {
    @Test
    fun snapshot_roundTripsThroughCodecAndRestore() {
        var state = ClassicGameEngine.start(ClassicBoardPresets.easy(seed = 2026L))
        state = ClassicGameEngine.toggleFlag(state, Coordinate(0, 0))
        state = ClassicGameEngine.reveal(state, Coordinate(7, 7))

        val snapshot = ClassicGameEngine.snapshot(state)
        val encoded = ClassicGameSnapshotCodec.encode(snapshot)
        val decoded = ClassicGameSnapshotCodec.decode(encoded)

        assertTrue(decoded is SnapshotDecodeResult.Success)
        val restored = ClassicGameEngine.restore((decoded as SnapshotDecodeResult.Success).snapshot)

        assertEquals(state.status, restored.status)
        assertEquals(
            state.allCellStates.map { Triple(it.cell.coordinate, it.visibility, it.isDetonated) },
            restored.allCellStates.map { Triple(it.cell.coordinate, it.visibility, it.isDetonated) },
        )
    }

    @Test
    fun decode_reportsCorruptedSnapshotData() {
        val decoded = ClassicGameSnapshotCodec.decode("not-a-valid-snapshot")

        assertTrue(decoded is SnapshotDecodeResult.Corrupted)
    }
}
