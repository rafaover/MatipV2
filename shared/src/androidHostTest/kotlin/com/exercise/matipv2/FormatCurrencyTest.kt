package com.exercise.matipv2

import com.exercise.matipv2.util.formatCurrency
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Locale
import kotlin.test.assertEquals

class FormatCurrencyTest {

    private val originalLocale: Locale = Locale.getDefault()

    @Before
    fun setUp() {
        Locale.setDefault(Locale.US)
    }

    @After
    fun tearDown() {
        Locale.setDefault(originalLocale)
    }

    @Test
    fun formatCurrency_usesTwoDecimalsAndSymbol() {
        assertEquals("$1.50", formatCurrency(1.5))
    }

    @Test
    fun formatCurrency_zeroIsFormatted() {
        assertEquals("$0.00", formatCurrency(0.0))
    }
}
