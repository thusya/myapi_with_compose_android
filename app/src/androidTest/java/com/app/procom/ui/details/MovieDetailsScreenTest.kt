package com.app.procom.ui.details

import androidx.compose.ui.test.junit4.createComposeRule
import com.app.procom.ui.home.details.DetailsScreen
import org.junit.Rule
import org.junit.Test

class MovieDetailsScreenTest {
    @get: Rule
    val composeRule = createComposeRule()

    @Test
    fun test() {
        composeRule.setContent { DetailsScreen() }
    }
}