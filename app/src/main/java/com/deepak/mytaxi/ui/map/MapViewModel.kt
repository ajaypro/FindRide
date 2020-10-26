package com.deepak.mytaxi.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.deepak.mytaxi.data.model.Vehicle

class MapViewModel: ViewModel() {

    var _refreshVehicle: MutableLiveData<Vehicle> = MutableLiveData()

    val refreshVehicle: LiveData<Vehicle> = _refreshVehicle


    fun onSelectedVehicle(vehicle: Vehicle){
        _refreshVehicle.value = vehicle
        vehicle.coordinate
    }
}