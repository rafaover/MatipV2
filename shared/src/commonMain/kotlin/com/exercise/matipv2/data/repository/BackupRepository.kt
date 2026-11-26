package com.exercise.matipv2.data.repository

import kotlinx.coroutines.flow.Flow

interface BackupRepository {
    suspend fun backupDataToCloud(userId: String): Result<Unit>
    suspend fun restoreDataFromCloud(userId: String): Result<Unit>
    suspend fun deleteCloudData(userId: String): Result<Unit>
    fun getLastBackupDate(userId: String): Flow<String?>
}
