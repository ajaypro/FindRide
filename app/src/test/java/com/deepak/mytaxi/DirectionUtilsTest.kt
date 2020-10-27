package com.deepak.mytaxi

import android.location.Address
import android.location.Geocoder
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.deepak.mytaxi.data.model.Vehicles
import com.deepak.mytaxi.data.remote.Resource
import com.deepak.mytaxi.utils.TestCoroutineRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.times
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DirectionUtilsTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    lateinit var geocoder: Geocoder

    lateinit var addressList: List<Address>

    val latitude =  53.518699780579965
    val longitude =  10.032005862779812

    @Before
    fun setUp() {
       addressList = listOf(Address(Locale.getDefault()))
    }

    @Test
    fun getLocation_returnSuccess() {

        testCoroutineRule.runBlockingTest {

            doReturn(addressList)
                .`when`(geocoder)
                .getFromLocation(latitude, longitude, 1)

            geocoder.getFromLocation(latitude, longitude, 1)

            verify(geocoder, times(1)).getFromLocation(latitude, longitude, 1)
        }
    }

//    @Test
//    fun getLocation_returnException(){
//
//        testCoroutineRule.runBlockingTest {
//
//            doThrow(IOException())
//                .`when`(geocoder)
//                .getFromLocation(latitude, longitude, 1)
//
//            geocoder.getFromLocation(latitude, longitude, 1)
//
//            verify(geocoder, times(1)).getFromLocation(latitude, longitude, 1).
//        }
//    }

}