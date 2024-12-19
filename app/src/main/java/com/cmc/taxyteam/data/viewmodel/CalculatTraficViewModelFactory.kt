package com.cmc.taxyteam.data.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CalculatTraficViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalculatTraficViewModel::class.java)) {
            return CalculatTraficViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}