package com.deepak.mytaxi.data.remote

import androidx.collection.ArrayMap
import com.deepak.mytaxi.data.model.Vehicles
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface NetworkService {

    @GET(".")
    suspend fun getVehicles(
        @QueryMap map: ArrayMap<String, Double>
    ): Vehicles
}