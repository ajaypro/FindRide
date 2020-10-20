package com.deepak.mytaxi.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.deepak.mytaxi.data.model.Coordinate
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.math.roundToInt


fun getDirection(angle: Double): String {
    val directions = arrayOf(
        "North",
        "North-West",
        "West",
        "South-West",
        "South",
        "South-East",
        "East",
        "North-East"
    )

    val data = if (angle % 360 < 0) angle.plus(360) else angle

    return directions[((data / 45) % 7).roundToInt()]
}

val handler = CoroutineExceptionHandler { _, exception ->
    println("CoroutineExceptionHandler got $exception")
}

suspend fun getLocation(
    coordinate: Coordinate,
    context: Context,
    coroutineScope: CoroutineScope
): String {
    var strAddress = ""
    var addressList: List<Address>
    val geocoder = Geocoder(context, Locale.getDefault())

    addressList = withContext(coroutineScope.coroutineContext + handler) {
        geocoder.getFromLocation(coordinate.longitude, coordinate.latitude, 1)
    }


    if (addressList.isNotEmpty()) {

        val returnedAddress = addressList[0]
        val strReturnedAddress = StringBuilder("")

        for (i in 0..returnedAddress.maxAddressLineIndex) {
            strReturnedAddress.append(
                returnedAddress.getAddressLine(i).run { getfinalAddress(this) }).append("\n")
        }
        strAddress = strReturnedAddress.toString()

//                addressList.forEach {
//                    location = it.apply {
//                        strReturnedAddress
//                            .append(getValue(subAdminArea)).append(" ,")
//                            .append(getValue(subLocality)).append(" ,")
//                            .append(locality)
//                            .append(adminArea)
//                    }
//                    strAddress = location.toString()
//                }

    } else {
        strAddress = KeyConstants.NO_ADDRESS_FOUND
    }
    return strAddress
}


private fun String.getfinalAddress(address: String): String =

    if (!address.contains("Unnamed Road")) {
        address
    } else {
        address.split(",").filter { it != "Unnamed Road" }.joinToString(",")
    }






