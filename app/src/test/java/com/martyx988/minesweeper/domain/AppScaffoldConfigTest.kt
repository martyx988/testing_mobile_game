package com.martyx988.minesweeper.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AppScaffoldConfigTest {
    @Test
    fun supportedModes_containsClassicAndVariantModes() {
        assertEquals(2, AppScaffoldConfig.supportedModes.size)
        assertTrue(AppScaffoldConfig.supportedModes.contains(GameMode.CLASSIC_EASY))
        assertTrue(AppScaffoldConfig.supportedModes.contains(GameMode.TRAP_TILES))
    }
}
