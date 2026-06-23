package com.nishatransport.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class PreferenceManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("nisha_prefs", Context.MODE_PRIVATE)

    companion object {
        const val KEY_DARK_MODE = "dark_mode"
        const val KEY_BACKUP_FREQUENCY = "backup_frequency"
        const val KEY_LAST_BACKUP_TIME = "last_backup_time"
        const val KEY_LAST_BACKUP_STATUS = "last_backup_status"
        const val KEY_GOOGLE_ACCOUNT = "google_account"
        const val KEY_AUTO_BACKUP_ENABLED = "auto_backup_enabled"
        const val KEY_DRIVE_FILE_ID = "drive_file_id"

        const val BACKUP_DAILY = "daily"
        const val BACKUP_WEEKLY = "weekly"
        const val BACKUP_MANUAL = "manual"
    }

    var isDarkMode: Boolean
        get() = prefs.getBoolean(KEY_DARK_MODE, false)
        set(value) {
            prefs.edit().putBoolean(KEY_DARK_MODE, value).apply()
            applyTheme(value)
        }

    var backupFrequency: String
        get() = prefs.getString(KEY_BACKUP_FREQUENCY, BACKUP_DAILY) ?: BACKUP_DAILY
        set(value) = prefs.edit().putString(KEY_BACKUP_FREQUENCY, value).apply()

    var lastBackupTime: Long
        get() = prefs.getLong(KEY_LAST_BACKUP_TIME, 0L)
        set(value) = prefs.edit().putLong(KEY_LAST_BACKUP_TIME, value).apply()

    var lastBackupStatus: String
        get() = prefs.getString(KEY_LAST_BACKUP_STATUS, "Never backed up") ?: "Never backed up"
        set(value) = prefs.edit().putString(KEY_LAST_BACKUP_STATUS, value).apply()

    var googleAccountEmail: String?
        get() = prefs.getString(KEY_GOOGLE_ACCOUNT, null)
        set(value) = prefs.edit().putString(KEY_GOOGLE_ACCOUNT, value).apply()

    var isAutoBackupEnabled: Boolean
        get() = prefs.getBoolean(KEY_AUTO_BACKUP_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_AUTO_BACKUP_ENABLED, value).apply()

    var driveFileId: String?
        get() = prefs.getString(KEY_DRIVE_FILE_ID, null)
        set(value) = prefs.edit().putString(KEY_DRIVE_FILE_ID, value).apply()

    fun applyTheme(darkMode: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    fun applyCurrentTheme() = applyTheme(isDarkMode)
}
