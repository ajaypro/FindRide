package com.deepak.mytaxi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.deepak.mytaxi.data.remote.Resource
import com.deepak.mytaxi.data.repository.VehicleRepository
import kotlinx.coroutines.Dispatchers

class SharedViewModel(private val vehicleRepository: VehicleRepository): ViewModel() {

     fun getVehicles() = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
          try {
               emit(Resource.success(vehicleRepository.getVehicles()))
          } catch (exception: Exception) {
               emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
          }
     }
}