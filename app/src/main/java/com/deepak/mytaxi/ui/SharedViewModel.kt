package com.deepak.mytaxi.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.deepak.mytaxi.data.model.Vehicle
import com.deepak.mytaxi.data.remote.Resource
import com.deepak.mytaxi.data.repository.VehicleRepository
import com.deepak.mytaxi.utils.Event
import com.deepak.mytaxi.utils.getLocation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SharedViewModel(private val vehicleRepository: VehicleRepository, application: Application): AndroidViewModel(application) {

    //private val context = getApplication<Application>().applicationContext

    /**
     * Livedata for Navigation
     */
    val taxiNavigation =  MutableLiveData<Event<Boolean>>()
    val poolNavigation = MutableLiveData<Event<Boolean>>()
    val mapNavigation = MutableLiveData<Event<Boolean>>()



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