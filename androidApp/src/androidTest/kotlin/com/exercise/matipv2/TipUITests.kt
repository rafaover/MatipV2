package com.exercise.matipv2

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.koin.androidx.compose.koinViewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.exercise.matipv2.ui.MainScreen
import com.exercise.matipv2.ui.theme.MaTIPv2Theme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.text.NumberFormat

// See [testing documentation](http://d.android.com/tools/testing).

@RunWith(AndroidJUnit4::class)
@MediumTest
class TipUITests {
    @get: Rule
    val composeTestRule = createComposeRule()

    @Test
    fun calculate_20_percent_tip() {
        composeTestRule.setContent {
            MaTIPv2Theme {
                MainScreen(koinViewModel())
            }
        }
        composeTestRule.onNodeWithText("Bill Amount").performTextInput("10")
        composeTestRule.onNodeWithText("Tip Percentage").performTextInput("20")
        val expectedTip = NumberFormat.getCurrencyInstance().format(2)
        composeTestRule.onNodeWithTag("TipAmount").assert(hasText(expectedTip))
    }
    @Test
    fun calculate_20_percent_tip_NoRound() {
        composeTestRule.setContent {
            MaTIPv2Theme {
                MainScreen(koinViewModel())
            }
        }
        composeTestRule.onNodeWithText("Bill Amount").performTextInput("13")
        composeTestRule.onNodeWithText("Tip Percentage").performTextInput("20")
        val expectedTip = NumberFormat.getCurrencyInstance().format(2.60)
        composeTestRule.onNodeWithText(expectedTip).assertExists()
    }
    @Test
    fun calculate_20_percent_tip_Rounded() {
        composeTestRule.setContent {
            MaTIPv2Theme {
                MainScreen(koinViewModel())
            }
        }
        composeTestRule.onNodeWithText("Bill Amount").performTextInput("13")
        composeTestRule.onNodeWithText("Tip Percentage").performTextInput("20")
        composeTestRule.onNodeWithTag("RoundTheTipSwitch").performClick()
        val expectedTip = NumberFormat.getCurrencyInstance().format(3)
        composeTestRule.onNodeWithText(expectedTip).assertExists()
    }

    @Test
    fun splitCounter_afterClick_plusAndMinus() {
        composeTestRule.setContent {
            MaTIPv2Theme {
                MainScreen(koinViewModel())
            }
        }
        composeTestRule.onNodeWithText("+").performClick().performClick()
        composeTestRule.onNodeWithTag("SplitCounter").assert(hasText("3"))
        composeTestRule.onNodeWithText("-").performClick()
        composeTestRule.onNodeWithTag("SplitCounter").assert(hasText("2"))
    }
    @Test
    fun splitCounter_nonNegative() {
        composeTestRule.setContent {
            MaTIPv2Theme {
                MainScreen(koinViewModel())
            }
        }
        composeTestRule.onNodeWithText("-").performClick()
        composeTestRule.onNodeWithTag("SplitCounter").assert(hasText("0"))
    }
}

