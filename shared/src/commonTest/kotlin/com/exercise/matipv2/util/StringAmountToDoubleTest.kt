package com.exercise.matipv2.util

import kotlin.test.Test
import kotlin.test.assertEquals

class StringAmountToDoubleTest {

    @Test
    fun numbersAreParsed() {
        assertEquals(100.0, stringAmountToDouble("100"))
        assertEquals(12.5, stringAmountToDouble("12.5"))
    }

    @Test
    fun textThatIsNotANumberIsZero() {
        assertEquals(0.0, stringAmountToDouble(""))
        assertEquals(0.0, stringAmountToDouble("abc"))
    }
}
