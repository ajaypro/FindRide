package com.deepak.mytaxi

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.deepak.mytaxi.data.model.Coordinate
import com.deepak.mytaxi.data.model.Vehicle
import com.deepak.mytaxi.data.model.Vehicles
import com.deepak.mytaxi.data.remote.NetworkService
import com.deepak.mytaxi.data.remote.Resource
import com.deepak.mytaxi.data.repository.VehicleRepository
import com.deepak.mytaxi.ui.MainViewModel
import com.deepak.mytaxi.utils.TestCoroutineRule
import com.deepak.mytaxi.utils.getQueryMap
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {


    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()


    @Mock
    lateinit var vehicleRepository: VehicleRepository

    lateinit var viewModel: MainViewModel

    @Mock
    private lateinit var vehcilesObserver: Observer<Resource<Vehicles>>

    lateinit var vehicles: Vehicles

    @Before
    fun setUp() {
        viewModel = MainViewModel(vehicleRepository)

        vehicles = Vehicles(listOf(Vehicle(1935, Coordinate(53.45, 9.90), "TAXI", 145.77)))
    }

    @Test
    fun getVehicles_returnSuccess() {

        testCoroutineRule.runBlockingTest {

            doReturn(Vehicles(emptyList()))
                .`when`(vehicleRepository)
                .getVehicles()
            viewModel.getVehicles().observeForever(vehcilesObserver)

            verify(vehcilesObserver).onChanged(Resource.loading(null))

            whenever(vehicleRepository.getVehicles()) .thenReturn(vehicles)
            verify(vehcilesObserver).onChanged(Resource.success(Vehicles(emptyList())))

            viewModel.getVehicles().removeObserver(vehcilesObserver)

        }
    }

    @Test
    fun getVehicles_returnError() {

        val errorMessage = " Error occured no value"
        testCoroutineRule.runBlockingTest {

            doThrow(RuntimeException(errorMessage))
                .`when`(vehicleRepository)
                .getVehicles()
            viewModel.getVehicles().observeForever(vehcilesObserver)

            verify(vehcilesObserver, times(1)).onChanged(Resource.loading(null))

            verify(vehcilesObserver, times(1)).onChanged(Resource.error(null, errorMessage))

            viewModel.getVehicles().removeObserver(vehcilesObserver)

        }
    }

}