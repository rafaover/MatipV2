package com.exercise.matipv2.util

import com.exercise.matipv2.data.local.model.Tip
import kotlin.test.Test
import kotlin.test.assertEquals

class TipListToStringTest {

    @Test
    fun everyTipAmountIsOnItsOwnLine() {
        val tips = listOf(
            Tip(id = 1, tipAmount = "10", dateCreated = "10/10/2021"),
            Tip(id = 2, tipAmount = "15", dateCreated = "10/10/2021"),
            Tip(id = 3, tipAmount = "20", dateCreated = "10/10/2021"),
        )

        assertEquals("10\n15\n20", tipListToString(tips))
    }

    @Test
    fun noTipsIsAnEmptyText() {
        assertEquals("", tipListToString(emptyList()))
    }
}
