package com.exercise.matipv2

import androidx.test.filters.SmallTest
import com.exercise.matipv2.data.local.model.Tip
import com.exercise.matipv2.util.calculateTip
import com.exercise.matipv2.util.localDateTimeFormated
import com.exercise.matipv2.util.stringAmountToDouble
import com.exercise.matipv2.util.tipListToString
import org.junit.Assert.assertEquals
import org.junit.Test

@SmallTest
class TipUtilsTests {

    @Test
    fun testStringAmountToDouble() {
        val amount = "100"
        val expected = 100.0
        val actual = stringAmountToDouble(amount)
        assertEquals(expected, actual, 0.0)
    }

    @Test
    fun testCalculateTip_noSplit() {
        val amount = "100"
        val tipPercent = "15"
        val roundUp = false
        val expected = "$15.00"
        val actual = calculateTip(amount, tipPercent, 1, roundUp)
        assertEquals(expected, actual)
    }

    @Test
    fun testCalculateTip_Split() {
        val amount = "200"
        val tipPercent = "10"
        val roundUp = false
        val expected = "$10.00"
        val actual = calculateTip(amount, tipPercent, 2, roundUp)
        assertEquals(expected, actual)
    }

    @Test
    fun testTipListToString() {
        val tipList = listOf(
            Tip(1, "10", "10", 1, "10/10/2021"),
            Tip(2, "15", "15", 1, "10/10/2021"),
            Tip(3, "20", "20", 1, "10/10/2021")
        )
        val expected = "10\n15\n20"
        val actual = tipListToString(tipList)
        assertEquals(expected, actual)
    }

    /** I want to assert the format of the date, but I can't predict the exact date.
     * So I'll just check if the format is correct
     */
    @Test
    fun testLocalDateTimeFormated() {
        val actual = localDateTimeFormated()
        val regex = Regex("\\d{2}/\\d{2}/\\d{4}")
        assertEquals(true, regex.matches(actual))

    }
}