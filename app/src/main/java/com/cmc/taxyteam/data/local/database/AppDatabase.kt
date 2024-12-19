package com.cmc.taxyteam.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cmc.taxyteam.data.local.dao.DriverDao
import com.cmc.taxyteam.data.local.models.Driver


@Database(entities = [Driver::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun driverDao(): DriverDao
}