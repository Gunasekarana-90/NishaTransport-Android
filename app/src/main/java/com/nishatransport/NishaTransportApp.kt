package com.nishatransport

import android.app.Application
import com.nishatransport.data.local.NishaDatabase
import com.nishatransport.data.repository.LoadRepository
import com.nishatransport.utils.PreferenceManager

class NishaTransportApp : Application() {

    val database by lazy { NishaDatabase.getInstance(this) }
    val repository by lazy { LoadRepository(database.loadDao()) }
    val preferenceManager by lazy { PreferenceManager(this) }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: NishaTransportApp
            private set
    }
}
