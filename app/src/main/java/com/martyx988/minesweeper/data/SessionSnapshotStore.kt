package com.martyx988.minesweeper.data

import com.martyx988.minesweeper.domain.ClassicGameSnapshot

sealed interface SnapshotLoadResult {
    data object Missing : SnapshotLoadResult
    data class Available(val snapshot: ClassicGameSnapshot) : SnapshotLoadResult
    data class Corrupted(val rawValue: String?) : SnapshotLoadResult
}

interface SessionSnapshotStore {
    fun load(): SnapshotLoadResult
    fun save(snapshot: ClassicGameSnapshot)
    fun clear()
}
