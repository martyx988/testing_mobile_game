package com.martyx988.minesweeper.data

import android.content.Context
import com.martyx988.minesweeper.domain.ClassicGameSnapshot
import com.martyx988.minesweeper.domain.ClassicGameSnapshotCodec
import com.martyx988.minesweeper.domain.SnapshotDecodeResult

class SharedPreferencesSessionSnapshotStore(
    context: Context,
) : SessionSnapshotStore {
    private val preferences = context.getSharedPreferences("minesweeper_session", Context.MODE_PRIVATE)

    override fun load(): SnapshotLoadResult {
        val rawValue = preferences.getString(KEY_ACTIVE_SNAPSHOT, null) ?: return SnapshotLoadResult.Missing
        return when (val decoded = ClassicGameSnapshotCodec.decode(rawValue)) {
            is SnapshotDecodeResult.Success -> SnapshotLoadResult.Available(decoded.snapshot)
            is SnapshotDecodeResult.Corrupted -> SnapshotLoadResult.Corrupted(decoded.rawValue)
        }
    }

    override fun save(snapshot: ClassicGameSnapshot) {
        val saved = preferences.edit()
            .putString(KEY_ACTIVE_SNAPSHOT, ClassicGameSnapshotCodec.encode(snapshot))
            .commit()
        check(saved) { "Failed to persist active game snapshot" }
    }

    override fun clear() {
        val cleared = preferences.edit()
            .remove(KEY_ACTIVE_SNAPSHOT)
            .commit()
        check(cleared) { "Failed to clear active game snapshot" }
    }

    private companion object {
        const val KEY_ACTIVE_SNAPSHOT = "active_snapshot"
    }
}
