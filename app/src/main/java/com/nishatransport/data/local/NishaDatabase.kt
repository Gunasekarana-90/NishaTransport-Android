package com.nishatransport.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nishatransport.data.local.dao.LoadDao
import com.nishatransport.data.local.entity.Load

@Database(
    entities = [Load::class],
    version = 1,
    exportSchema = true
)
abstract class NishaDatabase : RoomDatabase() {

    abstract fun loadDao(): LoadDao

    companion object {
        const val DATABASE_NAME = "nisha_transport.db"

        @Volatile
        private var INSTANCE: NishaDatabase? = null

        fun getInstance(context: Context): NishaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NishaDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
