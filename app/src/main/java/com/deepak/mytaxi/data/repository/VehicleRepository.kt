package com.deepak.mytaxi.data.repository

import com.deepak.mytaxi.data.model.Vehicles
import com.deepak.mytaxi.data.remote.HamburgLocationBounds
import com.deepak.mytaxi.data.remote.NetworkService

class VehicleRepository(private val networkService: NetworkService) {

    suspend fun getVehicles(): Vehicles {

        val queryMap : HashMap <String, Double> = hashMapOf()
        queryMap["p1Lat"] = HamburgLocationBounds.LAT_1.value
        queryMap["p1Lon"] = HamburgLocationBounds.LON_1.value
        queryMap["p2Lat"] = HamburgLocationBounds.LAT_2.value
        queryMap["p2Lon"] = HamburgLocationBounds.LON_2.value

       return  networkService.getCoordinates(queryMap)
    }

}