package com.exercise.matipv2.data.remote

import com.exercise.matipv2.data.local.model.Tip
import com.exercise.matipv2.data.local.model.TipList
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Backups already stored in Firestore were written by the reflection mapper,
 * which uses the Kotlin property names as field names. These tests pin that
 * shape so new builds can restore old backups and the other way around.
 */
class BackupMappingTest {

    @Test
    fun tipList_isWrittenWithPropertyNames() {
        val list = TipList(id = 3, name = "Dinner", userId = "user-1", cloudId = null)

        assertEquals(
            mapOf("id" to 3, "name" to "Dinner", "userId" to "user-1", "cloudId" to null),
            list.toBackupMap()
        )
    }

    @Test
    fun tip_isWrittenWithPropertyNames() {
        val tip = Tip(
            id = 9,
            tipAmount = "$5.00",
            tipPercent = "15",
            listId = 3,
            dateCreated = "1/2/2026",
            userId = "user-1",
            cloudId = "cloud-9"
        )

        assertEquals(
            mapOf(
                "id" to 9,
                "tipAmount" to "$5.00",
                "tipPercent" to "15",
                "listId" to 3,
                "dateCreated" to "1/2/2026",
                "userId" to "user-1",
                "cloudId" to "cloud-9"
            ),
            tip.toBackupMap()
        )
    }

    @Test
    fun tipList_isReadFromFirestoreDocument() {
        // Firestore returns every integer as a Long.
        val document = mapOf("id" to 3L, "name" to "Dinner", "userId" to "user-1")

        assertEquals(TipList(id = 3, name = "Dinner", userId = "user-1"), document.toTipList())
    }

    @Test
    fun tip_isReadFromFirestoreDocument() {
        val document = mapOf(
            "id" to 9L,
            "tipAmount" to "$5.00",
            "tipPercent" to "15",
            "listId" to 3L,
            "dateCreated" to "1/2/2026",
            "userId" to "user-1"
        )

        assertEquals(
            Tip(
                id = 9,
                tipAmount = "$5.00",
                tipPercent = "15",
                listId = 3,
                dateCreated = "1/2/2026",
                userId = "user-1"
            ),
            document.toTip()
        )
    }

    @Test
    fun missingFields_fallBackToEntityDefaults() {
        assertEquals(TipList(), emptyMap<String, Any?>().toTipList())
        assertEquals(Tip(), emptyMap<String, Any?>().toTip())
    }

    @Test
    fun roundTrip_keepsEveryField() {
        val tip = Tip(1, "$2.50", "10", 4, "5/6/2026", "user-2", "cloud-1")
        val list = TipList(4, "Lunch", "user-2", "cloud-2")

        assertEquals(tip, tip.toBackupMap().toLongBackedDocument().toTip())
        assertEquals(list, list.toBackupMap().toLongBackedDocument().toTipList())
    }

    /** Mimics Firestore, which hands back every stored integer as a Long. */
    private fun Map<String, Any?>.toLongBackedDocument(): Map<String, Any?> =
        mapValues { (_, value) -> if (value is Int) value.toLong() else value }
}
