package com.exercise.matipv2.fakes

import com.exercise.matipv2.data.repository.BackupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/** [BackupRepository] for tests: every call succeeds and nothing leaves the process. */
class FakeBackupRepository : BackupRepository {

    override suspend fun backupDataToCloud(userId: String): Result<Unit> = Result.success(Unit)

    override suspend fun restoreDataFromCloud(userId: String): Result<Unit> = Result.success(Unit)

    override suspend fun deleteCloudData(userId: String): Result<Unit> = Result.success(Unit)

    override fun getLastBackupDate(userId: String): Flow<String?> = flowOf(null)
}
