package com.deepak.mytaxi.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.deepak.mytaxi.data.model.Vehicle
import com.deepak.mytaxi.data.remote.Resource
import com.deepak.mytaxi.data.repository.VehicleRepository
import com.deepak.mytaxi.utils.Event
import kotlinx.coroutines.Dispatchers

class MainViewModel(private val vehicleRepository: VehicleRepository): ViewModel() {

    /**
     * Livedata for Navigation based on bottom navigation selection
     */

    val taxiNavigation =  MutableLiveData<Event<Boolean>>()
    val poolNavigation = MutableLiveData<Event<Boolean>>()
    val mapNavigation = MutableLiveData<Event<Boolean>>()

    val navigateToMapFragment: MutableLiveData<Event<Boolean>> = MutableLiveData()

    /**
     * livedata that gets updated from pool and taxi fragment when a vehicle is selected from RV to
     * display on map
     */
    val selectedVehicle: MutableLiveData<Event<Vehicle>> = MutableLiveData()

    fun onNavigateToMap() {
        navigateToMapFragment.postValue(Event(true))
    }

    init {
        taxiNavigation.postValue(Event(true))
    }

    fun onTaxiSelected(){
        taxiNavigation.postValue(Event(true))
    }

    fun onPoolSelected(){
        poolNavigation.postValue(Event(true))
    }

    fun onMapSelected(){
        mapNavigation.postValue(Event(true))
    }

     fun getVehicles() = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
          try {
               emit(Resource.success(vehicleRepository.getVehicles()))
          } catch (exception: Exception) {
               emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
          }
     }


}