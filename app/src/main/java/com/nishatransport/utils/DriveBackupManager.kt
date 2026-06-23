package com.nishatransport.utils

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import com.nishatransport.data.local.NishaDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object DriveBackupManager {

    private const val TAG = "DriveBackupManager"
    private const val BACKUP_FOLDER_NAME = "NishaTransportBackup"
    private const val BACKUP_FILE_PREFIX = "nisha_backup_"
    private const val ENCRYPTION_PASSWORD = "NishaTransport@2024#Secure"
    private const val MIME_TYPE_BIN = "application/octet-stream"
    private const val MIME_TYPE_FOLDER = "application/vnd.google-apps.folder"

    // ─── GET DRIVE SERVICE ───

    private fun getDriveService(context: Context): Drive? {
        val account = GoogleSignIn.getLastSignedInAccount(context) ?: return null
        val credential = GoogleAccountCredential.usingOAuth2(
            context, listOf(DriveScopes.DRIVE_APPDATA, DriveScopes.DRIVE_FILE)
        )
        credential.selectedAccount = account.account

        return Drive.Builder(
            AndroidHttp.newCompatibleTransport(),
            GsonFactory.getDefaultInstance(),
            credential
        ).setApplicationName("NishaTransport").build()
    }

    // ─── BACKUP ───

    suspend fun backupToGoogleDrive(context: Context): BackupResult = withContext(Dispatchers.IO) {
        try {
            val drive = getDriveService(context)
                ?: return@withContext BackupResult.Failure("Not signed in to Google. Please sign in first.")

            // Get DB file
            val dbFile = context.getDatabasePath(NishaDatabase.DATABASE_NAME)
            if (!dbFile.exists()) {
                return@withContext BackupResult.Failure("Database file not found")
            }

            // Encrypt
            val encryptedBytes = encryptFile(dbFile.readBytes())

            // Get or create backup folder
            val folderId = getOrCreateFolder(drive)

            // Create backup file
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val backupFileName = "${BACKUP_FILE_PREFIX}${timestamp}.enc"

            val fileMetadata = File().apply {
                name = backupFileName
                parents = listOf(folderId)
            }

            val mediaContent = com.google.api.client.http.ByteArrayContent(
                MIME_TYPE_BIN, encryptedBytes
            )

            val uploadedFile = drive.files().create(fileMetadata, mediaContent)
                .setFields("id, name, size, createdTime")
                .execute()

            // Keep only last 5 backups
            cleanupOldBackups(drive, folderId)

            BackupResult.Success(
                fileId = uploadedFile.id,
                fileName = uploadedFile.name,
                sizeBytes = uploadedFile.getSize() ?: 0L
            )
        } catch (e: Exception) {
            Log.e(TAG, "Backup failed", e)
            BackupResult.Failure("Backup failed: ${e.message}")
        }
    }

    // ─── RESTORE ───

    suspend fun restoreFromGoogleDrive(
        context: Context,
        fileId: String
    ): RestoreResult = withContext(Dispatchers.IO) {
        try {
            val drive = getDriveService(context)
                ?: return@withContext RestoreResult.Failure("Not signed in to Google.")

            // Download encrypted backup
            val outputStream = ByteArrayOutputStream()
            drive.files().get(fileId).executeMediaAndDownloadTo(outputStream)
            val encryptedBytes = outputStream.toByteArray()

            // Decrypt
            val decryptedBytes = decryptFile(encryptedBytes)

            // Close current DB connection
            NishaDatabase.getInstance(context).close()

            // Write to DB file
            val dbFile = context.getDatabasePath(NishaDatabase.DATABASE_NAME)
            dbFile.parentFile?.mkdirs()
            dbFile.writeBytes(decryptedBytes)

            RestoreResult.Success
        } catch (e: Exception) {
            Log.e(TAG, "Restore failed", e)
            RestoreResult.Failure("Restore failed: ${e.message}")
        }
    }

    // ─── LIST BACKUPS ───

    suspend fun listBackups(context: Context): List<BackupFile> = withContext(Dispatchers.IO) {
        try {
            val drive = getDriveService(context) ?: return@withContext emptyList()
            val folderId = getOrCreateFolder(drive)

            val result = drive.files().list()
                .setQ("'$folderId' in parents and trashed = false")
                .setOrderBy("createdTime desc")
                .setFields("files(id, name, size, createdTime)")
                .execute()

            result.files.map { file ->
                BackupFile(
                    id = file.id,
                    name = file.name,
                    sizeBytes = file.getSize() ?: 0L,
                    createdTime = file.createdTime?.value ?: 0L
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "List backups failed", e)
            emptyList()
        }
    }

    // ─── HELPERS ───

    private fun getOrCreateFolder(drive: Drive): String {
        val result = drive.files().list()
            .setQ("name = '$BACKUP_FOLDER_NAME' and mimeType = '$MIME_TYPE_FOLDER' and trashed = false")
            .setFields("files(id)")
            .execute()

        return if (result.files.isNotEmpty()) {
            result.files[0].id
        } else {
            val folderMetadata = File().apply {
                name = BACKUP_FOLDER_NAME
                mimeType = MIME_TYPE_FOLDER
            }
            drive.files().create(folderMetadata).setFields("id").execute().id
        }
    }

    private fun cleanupOldBackups(drive: Drive, folderId: String, keepCount: Int = 5) {
        try {
            val result = drive.files().list()
                .setQ("'$folderId' in parents and trashed = false and name contains '$BACKUP_FILE_PREFIX'")
                .setOrderBy("createdTime desc")
                .setFields("files(id, name)")
                .execute()

            if (result.files.size > keepCount) {
                result.files.drop(keepCount).forEach { file ->
                    drive.files().delete(file.id).execute()
                    Log.d(TAG, "Deleted old backup: ${file.name}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Cleanup failed", e)
        }
    }

    // ─── ENCRYPTION (AES-256) ───

    private fun encryptFile(data: ByteArray): ByteArray {
        val salt = ByteArray(16).also { java.security.SecureRandom().nextBytes(it) }
        val iv = ByteArray(16).also { java.security.SecureRandom().nextBytes(it) }
        val key = deriveKey(ENCRYPTION_PASSWORD, salt)

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(iv))
        val encrypted = cipher.doFinal(data)

        // Format: salt(16) + iv(16) + encrypted data
        return salt + iv + encrypted
    }

    private fun decryptFile(data: ByteArray): ByteArray {
        val salt = data.copyOfRange(0, 16)
        val iv = data.copyOfRange(16, 32)
        val encrypted = data.copyOfRange(32, data.size)
        val key = deriveKey(ENCRYPTION_PASSWORD, salt)

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv))
        return cipher.doFinal(encrypted)
    }

    private fun deriveKey(password: String, salt: ByteArray): SecretKeySpec {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(password.toCharArray(), salt, 65536, 256)
        val tmp = factory.generateSecret(spec)
        return SecretKeySpec(tmp.encoded, "AES")
    }
}

// ─── RESULT TYPES ───

sealed class BackupResult {
    data class Success(val fileId: String, val fileName: String, val sizeBytes: Long) : BackupResult()
    data class Failure(val message: String) : BackupResult()
}

sealed class RestoreResult {
    object Success : RestoreResult()
    data class Failure(val message: String) : RestoreResult()
}

data class BackupFile(
    val id: String,
    val name: String,
    val sizeBytes: Long,
    val createdTime: Long
)
