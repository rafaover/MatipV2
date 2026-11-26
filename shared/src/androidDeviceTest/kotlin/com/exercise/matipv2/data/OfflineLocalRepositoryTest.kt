package com.exercise.matipv2.data

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.exercise.matipv2.data.local.MatipDatabase
import com.exercise.matipv2.data.local.model.Tip
import com.exercise.matipv2.data.local.model.TipList
import com.exercise.matipv2.data.repository.OfflineLocalRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/** Runs against a real in-memory database, because what matters here is the SQL behind the repository. */
@RunWith(AndroidJUnit4::class)
class OfflineLocalRepositoryTest {

    private lateinit var database: MatipDatabase
    private lateinit var repository: OfflineLocalRepository

    @Before
    fun setUp() {
        database = Room
            .inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().targetContext, MatipDatabase::class.java)
            .build()
        repository = OfflineLocalRepository(database.tipDao(), database.listDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun migrateGuestData_givesGuestListsAndTipsToTheUserAndLeavesOthersAlone() = runTest {
        repository.insertList(TipList(id = 1, name = "Guest list"))
        repository.insertList(TipList(id = 2, name = "Other user's list", userId = "someone-else"))
        repository.insertTip(Tip(id = 1, tipAmount = "$5.00", listId = 1))
        repository.insertTip(Tip(id = 2, tipAmount = "$9.00", listId = 2, userId = "someone-else"))

        repository.migrateGuestData(userId = "user-1")

        assertEquals(listOf("Guest list"), repository.getAllLists("user-1").first().map { it.name })
        assertEquals(listOf("$5.00"), repository.getAllTips("user-1").first().map { it.tipAmount })
        assertEquals(emptyList<TipList>(), repository.getAllLists(userId = null).first())
        assertEquals(listOf("Other user's list"), repository.getAllLists("someone-else").first().map { it.name })
    }

    @Test
    fun deleteAllUserData_removesOnlyThatUsersListsAndTips() = runTest {
        repository.insertList(TipList(id = 1, name = "Mine", userId = "user-1"))
        repository.insertList(TipList(id = 2, name = "Guest list"))
        repository.insertTip(Tip(id = 1, tipAmount = "$5.00", listId = 1, userId = "user-1"))
        repository.insertTip(Tip(id = 2, tipAmount = "$9.00", listId = 2))

        repository.deleteAllUserData(userId = "user-1")

        assertEquals(emptyList<TipList>(), repository.getAllLists("user-1").first())
        assertEquals(emptyList<Tip>(), repository.getAllTips("user-1").first())
        assertEquals(listOf("Guest list"), repository.getAllLists(userId = null).first().map { it.name })
        assertEquals(listOf("$9.00"), repository.getAllTips(userId = null).first().map { it.tipAmount })
    }
}
