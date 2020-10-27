package com.deepak.mytaxi.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.collection.ArrayMap
import com.deepak.mytaxi.data.model.Coordinate
import com.deepak.mytaxi.data.remote.HamburgLocationBounds
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.math.roundToInt

/**
 * Query parmeters for retrofit network call
 */
fun getQueryMap(): ArrayMap<String, Double> {

    val queryMap: ArrayMap<String, Double> = ArrayMap()
    queryMap["p1Lat"] = HamburgLocationBounds.LAT_1.value
    queryMap["p1Lon"] = HamburgLocationBounds.LON_1.value
    queryMap["p2Lat"] = HamburgLocationBounds.LAT_2.value
    queryMap["p2Lon"] = HamburgLocationBounds.LON_2.value

    return queryMap
}

/**
 *  Converting heading degree to directions
 */
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

/**
 * Coroutine exception handler used as this call would be made from lifecyclescope launch{} from mainactivity
 */

val handler = CoroutineExceptionHandler { _, exception ->
    println("CoroutineExceptionHandler got $exception")
}

suspend fun getLocation(
    coordinate: Coordinate,
    context: Context,
    coroutineScope: CoroutineScope
): String {

    var addressList: List<Address>
    val geocoder = Geocoder(context, Locale.getDefault())

    addressList = withContext(coroutineScope.coroutineContext + handler) {
        geocoder.getFromLocation(coordinate.longitude, coordinate.latitude, 1)
    }


    return if (addressList.isNotEmpty()) {

         val returnedAddress = addressList[0]
         val strReturnedAddress = StringBuilder("")

         for (i in 0..returnedAddress.maxAddressLineIndex) {
             strReturnedAddress.append(
                 returnedAddress.getAddressLine(i).run { getfinalAddress(this) }).append("\n")
         }
         strReturnedAddress.toString()

     } else {
         KeyConstants.NO_ADDRESS_FOUND
     }
}


private fun String.getfinalAddress(address: String): String =

    if (!address.contains("Unnamed Road")) {
        address
    } else {
        address.split(",").filter { it != "Unnamed Road" }.joinToString(",")
    }






