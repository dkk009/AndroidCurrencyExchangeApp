package com.deepak.currencyexchangeapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class HomeScreenTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp(){
        hiltRule.inject()
    }

    @Test
    fun homeScreenDisplayTest() {
        composeTestRule.onNodeWithTag("input_field").assertIsDisplayed()
    }

    @Test
    fun homeScreenCurrencyDialogTest() {
        composeTestRule.onNodeWithTag("select_currency_dialog").assertIsDisplayed()
        composeTestRule.onNodeWithTag("select_currency_dialog").performClick()
        composeTestRule.onNodeWithTag("currency_selection_dialog").assertIsDisplayed()
    }
}