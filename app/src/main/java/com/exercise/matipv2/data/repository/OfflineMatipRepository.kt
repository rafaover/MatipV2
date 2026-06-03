package com.exercise.matipv2.data.repository

import com.exercise.matipv2.data.local.dao.ListDao
import com.exercise.matipv2.data.local.dao.TipDao
import com.exercise.matipv2.data.local.model.List
import com.exercise.matipv2.data.local.model.ListWithTips
import com.exercise.matipv2.data.local.model.Tip
import com.exercise.matipv2.util.localDateTimeFormated
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class OfflineMatipRepository(
    private val tipDao: TipDao,
    private val listDao: ListDao,
    private val firestore: FirebaseFirestore
) : MatipRepository {

    private fun userRef(userId: String) = firestore.collection("users").document(userId)
    private fun userListsRef(userId: String) = userRef(userId).collection("lists")
    private fun userTipsRef(userId: String) = userRef(userId).collection("tips")

    // Tip Methods
    override suspend fun insertTip(tip: Tip) {
        tipDao.insertTip(tip)
    }

    override suspend fun deleteTip(tip: Tip) {
        tipDao.deleteTip(tip)
    }

    override suspend fun updateTip(tip: Tip) {
        tipDao.updateTip(tip)
    }

    override fun getAllTips(userId: String?) = tipDao.getAllTips(userId)
    
    override suspend fun getLastTipSaved(userId: String?): Tip {
        return tipDao.getLastTipSaved(userId)
    }

    override fun getAllTipsFromList(listId: Int, userId: String?): Flow<kotlin.collections.List<Tip>> {
        return tipDao.getAllTipsFromList(listId, userId)
    }

    override fun searchTipsInList(listId: Int, query: String, userId: String?) = 
        tipDao.searchTipsInList(listId, query, userId)


    // List Methods
    override suspend fun insertList(list: List) {
        listDao.insertList(list)
    }

    override suspend fun deleteList(list: List) {
        listDao.deleteList(list)
    }

    override suspend fun updateList(list: List) {
        listDao.updateList(list)
    }

    override fun getAllLists(userId: String?) = listDao.getAllLists(userId)
    
    override fun getAllListsWithTips(userId: String?): Flow<kotlin.collections.List<ListWithTips>> {
        return listDao.getAllListsWithTips(userId)
    }

    override fun getListByName(listName: String, userId: String?): Flow<List> {
        return listDao.getListByName(listName, userId)
    }

    override fun getListById(listId: Int, userId: String?): Flow<List> {
        return listDao.getListById(listId, userId)
    }

    override fun searchLists(query: String, userId: String?) = listDao.searchLists(query, userId)

    override suspend fun migrateGuestData(userId: String) {
        listDao.migrateGuestLists(userId)
        tipDao.migrateGuestTips(userId)
    }

    override suspend fun backupDataToCloud(userId: String): Result<Unit> {
        return try {
            val batch = firestore.batch()
            
            // Backup Lists
            val lists = listDao.getAllLists(userId).first()
            for (list in lists) {
                // Use a stable string ID for Firestore document
                val docRef = userListsRef(userId).document(list.id.toString())
                batch.set(docRef, list)
            }
            
            // Backup Tips
            val tips = tipDao.getAllTips(userId).first()
            for (tip in tips) {
                val docRef = userTipsRef(userId).document(tip.id.toString())
                batch.set(docRef, tip)
            }
            
            // Update last backup metadata
            val timestamp = localDateTimeFormated()
            batch.set(userRef(userId), mapOf("last_backup" to timestamp), SetOptions.merge())
            
            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun restoreDataFromCloud(userId: String): Result<Unit> {
        return try {
            // Restore Lists
            val listsSnapshot = userListsRef(userId).get().await()
            for (doc in listsSnapshot.documents) {
                val cloudList = doc.toObject(List::class.java)
                if (cloudList != null) {
                    // Insert as new local (ID 0 lets Room autogenerate)
                    listDao.insertList(cloudList.copy(id = 0)) 
                }
            }

            // Restore Tips
            val tipsSnapshot = userTipsRef(userId).get().await()
            for (doc in tipsSnapshot.documents) {
                val cloudTip = doc.toObject(Tip::class.java)
                if (cloudTip != null) {
                    tipDao.insertTip(cloudTip.copy(id = 0))
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getLastBackupDate(userId: String): Flow<String?> = callbackFlow {
        val listener = userRef(userId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                // Don't close with error to avoid crashing the collector
                // Just log it or send null
                trySend(null)
                return@addSnapshotListener
            }
            val date = snapshot?.getString("last_backup")
            trySend(date)
        }
        awaitClose { listener.remove() }
    }
}
