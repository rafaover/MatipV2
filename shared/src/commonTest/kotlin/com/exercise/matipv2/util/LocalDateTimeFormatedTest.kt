package com.exercise.matipv2.util

import kotlin.test.Test
import kotlin.test.assertTrue

class LocalDateTimeFormatedTest {

    /** Today's date cannot be predicted, so this checks the shape: day/month/year. */
    @Test
    fun dateIsDayMonthYear() {
        val date = localDateTimeFormated()

        assertTrue(Regex("\\d{1,2}/\\d{1,2}/\\d{4}").matches(date), "Unexpected date format: $date")
    }
}
