package com.exercise.matipv2.data.model

import androidx.test.filters.SmallTest
import com.exercise.matipv2.data.local.model.Tip
import org.junit.Assert.*
import org.junit.Test

@SmallTest
class TipTest {

    @Test
    fun tipModel_correctlyStoresData() {
        // Given
        val expectedId = 1
        val expectedTipAmount = "10"
        val expectedTipPercent = "10"
        val expectedListId = 1
        val expectedDateCreated = "10/10/2021"

        // When
        val tip = Tip(
            id = expectedId,
            tipAmount = expectedTipAmount,
            tipPercent = expectedTipPercent,
            listId = expectedListId,
            dateCreated = expectedDateCreated)

        // Then
        assertEquals(expectedId, tip.id)
        assertEquals(expectedTipAmount, tip.tipAmount)
        assertEquals(expectedTipPercent, tip.tipPercent)
        assertEquals(expectedListId, tip.listId)
        assertEquals(expectedDateCreated, tip.dateCreated)
    }

}