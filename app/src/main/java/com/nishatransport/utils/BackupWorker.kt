package com.nishatransport.utils

import android.content.Context
import androidx.work.*
import com.nishatransport.NishaTransportApp
import java.util.concurrent.TimeUnit

class BackupWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val result = DriveBackupManager.backupToGoogleDrive(applicationContext)
            val prefManager = (applicationContext as NishaTransportApp).preferenceManager

            when (result) {
                is BackupResult.Success -> {
                    prefManager.lastBackupTime = System.currentTimeMillis()
                    prefManager.lastBackupStatus = "✅ Backed up: ${result.fileName}"
                    prefManager.driveFileId = result.fileId
                    Result.success()
                }
                is BackupResult.Failure -> {
                    prefManager.lastBackupStatus = "❌ Failed: ${result.message}"
                    Result.retry()
                }
            }
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "NishaTransportBackup"

        fun scheduleBackup(context: Context, frequency: String) {
            val workManager = WorkManager.getInstance(context)

            workManager.cancelUniqueWork(WORK_NAME)

            val intervalHours = when (frequency) {
                PreferenceManager.BACKUP_DAILY -> 24L
                PreferenceManager.BACKUP_WEEKLY -> 168L
                else -> return // Manual only, don't schedule
            }

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()

            val request = PeriodicWorkRequestBuilder<BackupWorker>(
                intervalHours, TimeUnit.HOURS,
                intervalHours / 4, TimeUnit.HOURS  // flex interval
            )
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.MINUTES)
                .build()

            workManager.enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                request
            )
        }

        fun cancelBackup(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }
}
