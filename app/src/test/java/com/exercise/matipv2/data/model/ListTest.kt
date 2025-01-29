package com.exercise.matipv2.data.model

import androidx.test.filters.SmallTest
import com.exercise.matipv2.data.local.model.List
import org.junit.Assert.assertEquals
import org.junit.Test

@SmallTest
class ListTest {

    @Test
    fun listModel_correctlyStoresData() {
        // Given
        val expectedId = 1
        val expectedName = "Test List"

        // When
        val list = List(id = expectedId, name = expectedName)

        // Then
        assertEquals(expectedId, list.id)
        assertEquals(expectedName, list.name)
    }
}