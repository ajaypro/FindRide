package com.deepak.mytaxi.ui.pool

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.deepak.mytaxi.MainActivity
import com.deepak.mytaxi.R
import com.deepak.mytaxi.data.model.Vehicle
import com.deepak.mytaxi.ui.VehicleClickListener
import com.deepak.mytaxi.ui.VehicleListAdapter
import com.deepak.mytaxi.utils.Event
import com.deepak.mytaxi.utils.KeyConstants.POOL
import kotlinx.android.synthetic.main.fragment_vehicle.*
import kotlinx.android.synthetic.main.progress_layout.*

class PoolFragment : Fragment() {

    lateinit var vehicleListAdapter: VehicleListAdapter

    var poolList: ArrayList<Vehicle>? = null

    companion object {

        const val TAG = "PoolFragment"

        fun newInstance(): PoolFragment {
            val args = Bundle()
            val fragment = PoolFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        (activity as MainActivity).setActionBar(getString(R.string.pool))

        vehicleListAdapter = VehicleListAdapter( VehicleClickListener { vehicle ->

            (activity as MainActivity).mainViewModel.apply {
                selectedVehicle.postValue(Event(vehicle))
                onNavigateToMap()
            }
        })

        poolList = arguments?.getParcelableArrayList<Vehicle>(POOL)

        vehicleListAdapter.submitList(poolList)

        return inflater.inflate(R.layout.fragment_vehicle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar.visibility = View.GONE

        vehicleView.apply {
            adapter = vehicleListAdapter
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
        }
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//
//
//
//    }

//    fun navigateToMap(vehicle: Vehicle) {
//         val bundle = Bundle()
//        val mapFragment = MapsFragment()
//        bundle.putParcelable(VEHICLEDATA, vehicle)
//        mapFragment.arguments = bundle
//    }
}