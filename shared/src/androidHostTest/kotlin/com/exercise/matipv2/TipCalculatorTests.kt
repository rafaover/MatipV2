package com.exercise.matipv2

import androidx.test.filters.SmallTest
import com.exercise.matipv2.util.calculateTip
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.NumberFormat

@SmallTest
class TipCalculatorTests {
    @Test
    fun calculateTip_20Percent() {
        val amount = "10"
        val tipPercent = "20"
        val expectedTip = NumberFormat.getCurrencyInstance().format(2)
        val actualTip = calculateTip(amount, tipPercent, 1, false)
        assertEquals(expectedTip, actualTip)
    }
    @Test
    fun calculateTip_20PercentRoundUp() {
        val amount = "16"
        val tipPercent = "20"
        val expectedTip = NumberFormat.getCurrencyInstance().format(4)
        val actualTip = calculateTip(amount, tipPercent, 1, true)
        assertEquals(expectedTip, actualTip)
    }
    @Test
    fun calculateTip_20PercentNoRoundUp() {
        val amount = "16"
        val tipPercent = "20"
        val expectedTip = NumberFormat.getCurrencyInstance().format(3.2)
        val actualTip = calculateTip(amount, tipPercent, 1, false)
        assertEquals(expectedTip, actualTip)
    }
}