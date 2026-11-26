package com.exercise.matipv2.data.repository

import com.exercise.matipv2.data.local.dao.ListDao
import com.exercise.matipv2.data.local.dao.TipDao
import com.exercise.matipv2.data.remote.BackupMetadataDto
import com.exercise.matipv2.data.remote.LAST_BACKUP_FIELD
import com.exercise.matipv2.data.remote.ListBackupDto
import com.exercise.matipv2.data.remote.TipBackupDto
import com.exercise.matipv2.data.remote.toBackupDto
import com.exercise.matipv2.data.remote.toTip
import com.exercise.matipv2.data.remote.toTipList
import com.exercise.matipv2.util.localDateTimeFormated
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

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
            for (doc in userListsRef(userId).get().documents) {
                if (doc.id !in localListIds) batch.delete(doc.reference)
            }
            for (doc in userTipsRef(userId).get().documents) {
                if (doc.id !in localTipIds) batch.delete(doc.reference)
            }

            // 3. Upload/Update current items
            for (list in localLists) {
                val docRef = userListsRef(userId).document(list.id.toString())
                batch.set(docRef, ListBackupDto.serializer(), list.toBackupDto())
            }
            for (tip in localTips) {
                val docRef = userTipsRef(userId).document(tip.id.toString())
                batch.set(docRef, TipBackupDto.serializer(), tip.toBackupDto())
            }

            // 4. Update last backup timestamp
            batch.set(
                userRef(userId),
                BackupMetadataDto.serializer(),
                BackupMetadataDto(localDateTimeFormated()),
                merge = true
            )

            batch.commit()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun restoreDataFromCloud(userId: String): Result<Unit> {
        return try {
            val cloudLists = userListsRef(userId).get().documents
            val cloudTips = userTipsRef(userId).get().documents

            if (cloudLists.isEmpty() && cloudTips.isEmpty()) {
                return Result.failure(Exception("No backup found"))
            }

            // 1. Read everything first: a bad document must fail before local data is touched.
            val listsToRestore = cloudLists.map { it.data(ListBackupDto.serializer()).toTipList() }
            val tipsToRestore = cloudTips.map { it.data(TipBackupDto.serializer()).toTip() }

            // 2. Replace the current local user state with the cloud snapshot
            tipDao.deleteAllTipsForUser(userId)
            listDao.deleteAllListsForUser(userId)
            listsToRestore.forEach { listDao.insertList(it) }
            tipsToRestore.forEach { tipDao.insertTip(it) }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteCloudData(userId: String): Result<Unit> {
        return try {
            val batch = firestore.batch()

            // Delete sub-collections
            for (doc in userListsRef(userId).get().documents) batch.delete(doc.reference)
            for (doc in userTipsRef(userId).get().documents) batch.delete(doc.reference)

            // Delete user document
            batch.delete(userRef(userId))

            batch.commit()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getLastBackupDate(userId: String): Flow<String?> =
        userRef(userId).snapshots
            .map { snapshot -> if (snapshot.exists) snapshot.get<String?>(LAST_BACKUP_FIELD) else null }
            .catch { emit(null) }
}
