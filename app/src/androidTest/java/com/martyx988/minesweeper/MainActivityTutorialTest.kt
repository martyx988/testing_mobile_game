package com.martyx988.minesweeper

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import org.junit.Rule
import org.junit.Test

class MainActivityTutorialTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun howToPlayButton_opensTutorialDialog() {
        if (composeRule.onAllNodesWithText("Discard").fetchSemanticsNodes().isNotEmpty()) {
            composeRule.onNodeWithText("Discard").performClick()
        }

        composeRule.onNodeWithText("How to Play").performScrollTo().performClick()

        composeRule.onNodeWithText("How to play").assertIsDisplayed()
    }
}
