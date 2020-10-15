package com.deepak.mytaxi.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Vehicles(

    val poiList: ArrayList<Vehicle>

): Parcelable

@Parcelize
data class Vehicle(

    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("coordinate")
    val coordinate: Coordinate,

    @SerializedName("fleetType")
    val fleetType: String = "",

    @SerializedName("heading")
    val heading: Double = 0.0,

    val isTaxi: Boolean
): Parcelable {

    fun isTaxiOrPool(fleetType: String) =  fleetType == "TAXI"

}

@Parcelize
data class Coordinate(
    @SerializedName("latitude")
    val latitude: Double = 0.0,

    @SerializedName("longitude")
    val longitude: Double = 0.0
): Parcelable