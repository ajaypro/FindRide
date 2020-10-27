package com.deepak.mytaxi.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.deepak.mytaxi.data.model.Vehicle

class MapViewModel: ViewModel() {

    /**
     * Livedata to get last selected vehcile to display in map
     */

    private var _refreshVehicle: MutableLiveData<Vehicle> = MutableLiveData()

    val refreshVehicle: LiveData<Vehicle> = _refreshVehicle

    /**
     * Handling the click event functionality of selected vehicle in viewmodel that updates livedata
     * @param vehicle - current selected vehicle.
     */

    fun onSelectedVehicle(vehicle: Vehicle){
        _refreshVehicle.value = vehicle
    }
}