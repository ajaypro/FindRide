package com.deepak.mytaxi.ui.taxi

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
import com.deepak.mytaxi.utils.KeyConstants.TAXI
import kotlinx.android.synthetic.main.fragment_vehicle.*
import kotlinx.android.synthetic.main.progress_layout.*

class TaxiFragment : Fragment() {

    lateinit var vehicleListAdapter: VehicleListAdapter

    var taxiList: ArrayList<Vehicle>? = null

    var taxiLocation: String? = null

    companion object {

        const val TAG = "TaxiFragment"

        fun newInstance(): TaxiFragment {
            val args = Bundle()
            val fragment = TaxiFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        (activity as MainActivity).setActionBar(getString(R.string.taxi))

        vehicleListAdapter = VehicleListAdapter( VehicleClickListener { vehicle ->

            (activity as MainActivity).mainViewModel.apply {
                selectedVehicle.postValue(Event(vehicle))
                onNavigateToMap()
            }
        })

        taxiList = arguments?.getParcelableArrayList<Vehicle>(TAXI)

        vehicleListAdapter.submitList(taxiList)

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
//    }
}