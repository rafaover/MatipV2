package com.exercise.matipv2.data.remote

import com.exercise.matipv2.data.local.model.Tip
import com.exercise.matipv2.data.local.model.TipList
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Backups already stored in Firestore use the Kotlin property names as field names.
 * These tests pin that shape so new builds can restore old backups and the other way around.
 * JSON is only a stand-in here: it uses the same serial names Firestore receives.
 */
class BackupMappingTest {

    private val json = Json {
        encodeDefaults = true
        explicitNulls = true
        ignoreUnknownKeys = true
    }

    @Test
    fun tipList_isWrittenWithPropertyNames() {
        val list = TipList(id = 3, name = "Dinner", userId = "user-1", cloudId = null)

        assertEquals(
            """{"id":3,"name":"Dinner","userId":"user-1","cloudId":null}""",
            json.encodeToString(list.toBackupDto())
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
            """{"id":9,"tipAmount":"$5.00","tipPercent":"15","listId":3,""" +
                """"dateCreated":"1/2/2026","userId":"user-1","cloudId":"cloud-9"}""",
            json.encodeToString(tip.toBackupDto())
        )
    }

    @Test
    fun tipList_isReadFromFirestoreDocument() {
        val document = """{"id":3,"name":"Dinner","userId":"user-1"}"""

        assertEquals(
            TipList(id = 3, name = "Dinner", userId = "user-1"),
            json.decodeFromString<ListBackupDto>(document).toTipList()
        )
    }

    @Test
    fun tip_isReadFromFirestoreDocument() {
        val document = """{"id":9,"tipAmount":"$5.00","tipPercent":"15","listId":3,""" +
            """"dateCreated":"1/2/2026","userId":"user-1"}"""

        assertEquals(
            Tip(
                id = 9,
                tipAmount = "$5.00",
                tipPercent = "15",
                listId = 3,
                dateCreated = "1/2/2026",
                userId = "user-1"
            ),
            json.decodeFromString<TipBackupDto>(document).toTip()
        )
    }

    @Test
    fun missingFields_fallBackToEntityDefaults() {
        assertEquals(TipList(), json.decodeFromString<ListBackupDto>("{}").toTipList())
        assertEquals(Tip(), json.decodeFromString<TipBackupDto>("{}").toTip())
    }

    @Test
    fun unknownFields_areIgnored() {
        val document = """{"id":3,"name":"Dinner","addedByALaterVersion":true}"""

        assertEquals(TipList(id = 3, name = "Dinner"), json.decodeFromString<ListBackupDto>(document).toTipList())
    }

    @Test
    fun roundTrip_keepsEveryField() {
        val tip = Tip(1, "$2.50", "10", 4, "5/6/2026", "user-2", "cloud-1")
        val list = TipList(4, "Lunch", "user-2", "cloud-2")

        assertEquals(tip, json.decodeFromString<TipBackupDto>(json.encodeToString(tip.toBackupDto())).toTip())
        assertEquals(list, json.decodeFromString<ListBackupDto>(json.encodeToString(list.toBackupDto())).toTipList())
    }

    @Test
    fun lastBackupDate_isWrittenAsLastBackup() {
        assertEquals("""{"last_backup":"26/11/2026"}""", json.encodeToString(BackupMetadataDto("26/11/2026")))
    }
}
