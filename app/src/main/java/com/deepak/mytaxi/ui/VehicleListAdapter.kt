package com.deepak.mytaxi.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.deepak.mytaxi.R
import com.deepak.mytaxi.data.model.Vehicle
import com.deepak.mytaxi.utils.getDirection
import kotlinx.android.synthetic.main.item_vehicle.view.*

class VehicleListAdapter(val clickListener: VehicleClickListener) : ListAdapter<Vehicle, VehicleListAdapter.VehicleViewHolder>(VehicleDiffCallback())  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        return VehicleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_vehicle, parent, false))
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindView(item, clickListener)
    }

//    fun setListener(listener: OnVehicleItemClickedListener) {
//        this.listener = listener
//    }

    fun getLocation(address: String?): String = address ?: ""

//    fun updateData(newItems: ArrayList<Vehicle>) {
//        vehicles.clear()
//        vehicles.addAll(newItems)
//        notifyDataSetChanged()
//    }

    inner class VehicleViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindView(vehicle: Vehicle, clickListener: VehicleClickListener) {
            if(vehicle.isTaxiOrPool(vehicle.fleetType)) {
                itemView.fleet_type.setImageResource(R.drawable.taxi)
                itemView.location.text = vehicle.coordinate.addressFromLatLong
            } else {
                itemView.fleet_type.setImageResource(R.drawable.pool)
                itemView.location.text = vehicle.coordinate.addressFromLatLong
            }
               itemView.vehicle_model.text = vehicle.getVehicleModel()
               itemView.heading.text = getDirection(vehicle.heading)
               itemView.setOnClickListener{ clickListener.onClick(vehicle) }

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

class VehicleClickListener(val clickListener: (vehicle: Vehicle) -> Unit) {
    fun onClick(vehicle: Vehicle) = clickListener(vehicle)
}