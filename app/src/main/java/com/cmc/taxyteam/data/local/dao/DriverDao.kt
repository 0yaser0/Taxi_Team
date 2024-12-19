package com.cmc.taxyteam.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cmc.taxyteam.data.local.models.Driver

@Dao
interface DriverDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDriver(driver : Driver)

    @Query("SELECT * FROM drivers WHERE driverId = :driverId")
    suspend fun getDriverById(driverId: Int): Driver?

}