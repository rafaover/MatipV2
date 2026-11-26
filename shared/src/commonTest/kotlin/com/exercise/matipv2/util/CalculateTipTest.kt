package com.exercise.matipv2.util

import kotlin.test.Test
import kotlin.test.assertEquals

class CalculateTipTest {

    @Test
    fun tipIsThePercentageOfTheBill() {
        assertEquals(2.0, calculateTipAmount(amount = "10", tipPercent = "20", roundUp = false))
    }

    @Test
    fun tipIsSharedBetweenEveryonePaying() {
        assertEquals(10.0, calculateTipAmount(amount = "200", tipPercent = "10", tipSplit = 2, roundUp = false))
    }

    @Test
    fun roundUpRoundsEachShareUpToTheNextWholeNumber() {
        assertEquals(4.0, calculateTipAmount(amount = "16", tipPercent = "20", roundUp = true))
        assertEquals(1.0, calculateTipAmount(amount = "10", tipPercent = "15", tipSplit = 2, roundUp = true))
    }

    @Test
    fun withoutRoundUpTheTipKeepsItsCents() {
        assertEquals(3.2, calculateTipAmount(amount = "16", tipPercent = "20", roundUp = false), absoluteTolerance = 0.0001)
    }

    @Test
    fun splitOfOneOrLessDoesNotChangeTheTip() {
        assertEquals(2.0, calculateTipAmount(amount = "10", tipPercent = "20", tipSplit = 1, roundUp = false))
        assertEquals(2.0, calculateTipAmount(amount = "10", tipPercent = "20", tipSplit = 0, roundUp = false))
    }

    @Test
    fun missingOrInvalidInputIsATipOfZero() {
        assertEquals(0.0, calculateTipAmount(amount = "", tipPercent = "20", roundUp = false))
        assertEquals(0.0, calculateTipAmount(amount = "abc", tipPercent = "20", roundUp = true))
        assertEquals(0.0, calculateTipAmount(amount = "10", tipPercent = "", roundUp = false))
    }
}
