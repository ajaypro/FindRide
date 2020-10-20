package com.deepak.mytaxi.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.deepak.mytaxi.R
import com.deepak.mytaxi.TAXI
import com.deepak.mytaxi.data.model.Vehicle
import com.deepak.mytaxi.utils.getDirection
import kotlinx.android.synthetic.main.item_vehicle.view.*

class VehicleListAdapter : ListAdapter<Vehicle, VehicleListAdapter.VehicleViewHolder>(VehicleDiffCallback())  {

    private lateinit var listener: OnVehicleItemClickedListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        return VehicleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_vehicle, parent, false))
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindView(item)
    }

    fun setListener(listener: OnVehicleItemClickedListener) {
        this.listener = listener
    }

    fun getLocation(address: String?): String = address ?: ""

//    fun updateData(newItems: ArrayList<Vehicle>) {
//        vehicles.clear()
//        vehicles.addAll(newItems)
//        notifyDataSetChanged()
//    }

    inner class VehicleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        private val id = view.tv_vehicle_id!!
//        private val fleetType = view.tv_vehicle_fleet_type!!
//        private val heading = view.tv_vehicle_heading!!
//        private val location = view.tv_vehicle_location!!
//        private val avatar = view.iv_fleet_type!!
//        private val container = view.item_vehicle_container!!

        fun bindView(vehicle: Vehicle) {
            if(vehicle.isTaxiOrPool(vehicle.fleetType)) {
                itemView.fleet_type.setImageResource(R.drawable.taxi)
                itemView.location.text = vehicle.coordinate.addressFromLatLong
            } else {
                itemView.fleet_type.setImageResource(R.drawable.pool)
                itemView.location.text = vehicle.coordinate.addressFromLatLong
            }
               itemView.heading.text = getDirection(vehicle.heading)

//            id.text = vehicle.id.toString()
//            fleetType.text = vehicle.fleetType?.toUpperCase()
//            heading.text = vehicle.heading.toString()
//            location.text = "(${vehicle.latitude} , ${vehicle.longitude})"
//            when (vehicle.fleetType) {
//                FleetType.POOLING.value -> {
//                    fleetType.setBackgroundResource(R.drawable.bg_rounded_accent)
//                    avatar.setImageResource(R.drawable.ic_svg_car_pooling)
//                }
//                else -> {
//                    fleetType.setBackgroundResource(R.drawable.bg_rounded_gray)
//                    avatar.setImageResource(R.drawable.ic_svg_car_taxi)
//                }
//            }
//            container.setOnClickListener {
//                if (::listener.isInitialized) {
//                    listener.onVehicleItemClicked(vehicle, vehicles)
//                }
//            }
        }
    }


}

class VehicleDiffCallback: DiffUtil.ItemCallback<Vehicle> () {

    override fun areItemsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean {
        return oldItem == newItem
    }
}