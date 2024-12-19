package com.cmc.taxyteam.data.repository

import com.cmc.taxyteam.data.local.dao.DriverDao
import com.cmc.taxyteam.data.local.models.Driver

class DriverRepository(private val driverDao: DriverDao) {

    suspend fun upsertDriver(driver: Driver) {
        driverDao.upsertDriver(driver)
    }

    suspend fun getDriverById(driverId: Int): Driver? {
        return driverDao.getDriverById(driverId)
    }
}
