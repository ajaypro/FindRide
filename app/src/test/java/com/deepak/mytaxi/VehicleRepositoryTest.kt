package com.deepak.mytaxi

import android.accounts.NetworkErrorException
import android.os.NetworkOnMainThreadException
import com.deepak.mytaxi.data.model.Coordinate
import com.deepak.mytaxi.data.model.Vehicle
import com.deepak.mytaxi.data.model.Vehicles
import com.deepak.mytaxi.data.remote.NetworkService
import com.deepak.mytaxi.data.repository.VehicleRepository
import com.deepak.mytaxi.utils.getQueryMap
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class VehicleRepositoryTest {

   @Mock
   lateinit var networkService: NetworkService


    lateinit var vehicleRepository: VehicleRepository

    @Before
    fun setUp() {
        vehicleRepository = VehicleRepository(networkService)
    }

    @Test
    fun getVehicles_returnSuccess(){

        val vehicles = Vehicles(listOf(Vehicle(1935, Coordinate(53.45, 9.90), "TAXI", 145.77)))

        runBlocking {
            doReturn(vehicles)
                .`when`(networkService)
                .getVehicles(getQueryMap())

            vehicleRepository.getVehicles()

            verify(networkService, times(1)).getVehicles(getQueryMap())
        }
    }

    @Test
    fun getVehciles_returnError(){

        val errorMessage = "Network error"

        runBlocking {
            doReturn(NetworkOnMainThreadException())
                .`when`(networkService)
                .getVehicles(getQueryMap())

            vehicleRepository.getVehicles()

            verify(networkService, times(1)).getVehicles(getQueryMap())

        }

    }
}