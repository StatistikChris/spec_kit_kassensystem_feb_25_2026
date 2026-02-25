package de.barpos.kassensystem.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import de.barpos.kassensystem.MainActivity
import org.junit.Rule
import org.junit.Test

class KassenHappyPathTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun `US-1 full transaction less than 30s`() {
        // TODO: Implement test
    }
}
