package com.deepak.mytaxi.data.repository

import com.deepak.mytaxi.data.model.Vehicles
import com.deepak.mytaxi.data.remote.NetworkService
import com.deepak.mytaxi.utils.getQueryMap

class VehicleRepository(private val networkService: NetworkService) {

    suspend fun getVehicles(): Vehicles {

       return  networkService.getVehicles(getQueryMap())
    }

}