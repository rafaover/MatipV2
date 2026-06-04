package com.exercise.matipv2.data.repository

import com.exercise.matipv2.data.local.dao.ListDao
import com.exercise.matipv2.data.local.dao.TipDao
import com.exercise.matipv2.data.local.model.List
import com.exercise.matipv2.data.local.model.Tip
import com.exercise.matipv2.util.localDateTimeFormated
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

interface BackupRepository {
    suspend fun backupDataToCloud(userId: String): Result<Unit>
    suspend fun restoreDataFromCloud(userId: String): Result<Unit>
    suspend fun deleteCloudData(userId: String): Result<Unit>
    fun getLastBackupDate(userId: String): Flow<String?>
}

class FirestoreBackupRepository(
    private val tipDao: TipDao,
    private val listDao: ListDao,
    private val firestore: FirebaseFirestore
) : BackupRepository {

    private fun userRef(userId: String) = firestore.collection("users").document(userId)
    private fun userListsRef(userId: String) = userRef(userId).collection("lists")
    private fun userTipsRef(userId: String) = userRef(userId).collection("tips")

    override suspend fun backupDataToCloud(userId: String): Result<Unit> {
        return try {
            val batch = firestore.batch()
            
            // 1. Fetch current local data
            val localLists = listDao.getAllLists(userId).first()
            val localTips = tipDao.getAllTips(userId).first()
            
            val localListIds = localLists.map { it.id.toString() }.toSet()
            val localTipIds = localTips.map { it.id.toString() }.toSet()

            // 2. Clear removed items from Cloud
            val cloudLists = userListsRef(userId).get().await()
            for (doc in cloudLists.documents) {
                if (doc.id !in localListIds) {
                    batch.delete(doc.reference)
                }
            }
            
            val cloudTips = userTipsRef(userId).get().await()
            for (doc in cloudTips.documents) {
                if (doc.id !in localTipIds) {
                    batch.delete(doc.reference)
                }
            }

            // 3. Upload/Update current items
            for (list in localLists) {
                val docRef = userListsRef(userId).document(list.id.toString())
                batch.set(docRef, list)
            }
            for (tip in localTips) {
                val docRef = userTipsRef(userId).document(tip.id.toString())
                batch.set(docRef, tip)
            }
            
            // 4. Update last backup timestamp
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
            val cloudLists = userListsRef(userId).get().await()
            val cloudTips = userTipsRef(userId).get().await()

            if (cloudLists.isEmpty && cloudTips.isEmpty) {
                return Result.failure(Exception("No backup found"))
            }

            // 1. Clean current local user state
            tipDao.deleteAllTipsForUser(userId)
            listDao.deleteAllListsForUser(userId)

            // 2. Insert cloud snapshot
            for (doc in cloudLists.documents) {
                val list = doc.toObject(List::class.java)
                if (list != null) {
                    listDao.insertList(list) 
                }
            }
            for (doc in cloudTips.documents) {
                val tip = doc.toObject(Tip::class.java)
                if (tip != null) {
                    tipDao.insertTip(tip)
                }
            }
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteCloudData(userId: String): Result<Unit> {
        return try {
            val batch = firestore.batch()
            
            // Delete sub-collections
            val lists = userListsRef(userId).get().await()
            for (doc in lists.documents) batch.delete(doc.reference)
            
            val tips = userTipsRef(userId).get().await()
            for (doc in tips.documents) batch.delete(doc.reference)
            
            // Delete user document
            batch.delete(userRef(userId))
            
            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getLastBackupDate(userId: String): Flow<String?> = callbackFlow {
        val listener = userRef(userId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(null)
                return@addSnapshotListener
            }
            val date = snapshot?.getString("last_backup")
            trySend(date)
        }
        awaitClose { listener.remove() }
    }
}
