package com.deepak.mytaxi.ui

import com.deepak.mytaxi.data.model.Vehicle

interface OnVehicleItemClickedListener {

    fun onVehicleItemClicked(vehicle: Vehicle, vehicles: ArrayList<Vehicle>)
}