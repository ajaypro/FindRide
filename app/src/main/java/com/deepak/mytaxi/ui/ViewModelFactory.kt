package com.deepak.mytaxi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.deepak.mytaxi.data.remote.NetworkService
import com.deepak.mytaxi.data.repository.VehicleRepository

class ViewModelFactory(private val networkService: NetworkService) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            return SharedViewModel(VehicleRepository(networkService)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}