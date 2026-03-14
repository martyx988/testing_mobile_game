package com.martyx988.minesweeper.data

import com.martyx988.minesweeper.domain.PlayerProfile

interface PlayerProfileStore {
    fun load(): PlayerProfile
    fun save(profile: PlayerProfile)
}
