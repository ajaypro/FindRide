package com.deepak.mytaxi.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Vehicles(

    val poiList: List<Vehicle>

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
    val heading: Double = 0.0

): Parcelable {

    fun getVehicleModel(): String = arrayOf("Tesla Model 3", "VW polo", "VW Beetle",
        "Lamborghini", "Benz C Class", "Toyota Cyris", "Honda Civic"," Honda Accord", "Bentley", "Porche", "Audi A4", "BMW", "Opel Mass", "Rolls-Royce","Aston Martin", "Mclaren"
    ,"Land Rover", "Jaguar", "Mini-cooper", "Ferrari", "Volvo", "Range-Rover").random()


    fun isTaxiOrPool(fleetType: String) =  fleetType == "TAXI"

}

@Parcelize
data class Coordinate(
    @SerializedName("latitude")
    val latitude: Double = 0.0,

    @SerializedName("longitude")
    val longitude: Double = 0.0,

    /// user defined added data variable for showing address wherever we need to show
    var addressFromLatLong: String = ""
): Parcelable