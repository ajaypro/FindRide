package com.deepak.mytaxi.data.remote

import com.deepak.mytaxi.data.model.Vehicles
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface NetworkService {

    @GET(".")
    suspend fun getCoordinates(
        @QueryMap map: HashMap<String, Double>
    ): Vehicles
}