package com.exercise.matipv2.data.model

import androidx.test.filters.SmallTest
import com.exercise.matipv2.data.local.model.List
import com.exercise.matipv2.data.local.model.ListWithTips
import com.exercise.matipv2.data.local.model.Tip
import org.junit.Assert.*
import org.junit.Test

@SmallTest
class ListWithTipsTest {

    @Test
    fun testListWithTips_correctValues() {
        val list = List(1, "List 1")
        val tip1 = Tip(1, "2500", "10", 1, "10/10/2021")
        val tip2 = Tip(2, "3500", "10", 1, "10/10/2021")
        val listWithTips = ListWithTips(list, listOf(tip1, tip2))

        assertEquals(list, listWithTips.list)
        assertEquals(listOf(tip1, tip2), listWithTips.tips)
    }
}