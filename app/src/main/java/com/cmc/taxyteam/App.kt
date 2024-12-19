package com.cmc.taxyteam

import android.app.Application
import androidx.room.Room
import com.cmc.taxyteam.data.local.database.AppDatabase

class App : Application() {

    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "my_taxi_database"
        ).fallbackToDestructiveMigration().build()
    }

}