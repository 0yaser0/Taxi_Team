package com.cmc.taxyteam.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmc.taxyteam.data.local.models.Driver
import com.cmc.taxyteam.data.repository.DriverRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val driverRepository: DriverRepository) : ViewModel() {

    fun addDriver(driver: Driver) {
        viewModelScope.launch {
            driverRepository.upsertDriver(driver)
        }
    }

    fun getDriverById(driverId: Int): LiveData<Driver?> {
        val liveDataDriver = MutableLiveData<Driver?>()
        viewModelScope.launch {
            liveDataDriver.postValue(driverRepository.getDriverById(driverId))
        }
        return liveDataDriver
    }
}
