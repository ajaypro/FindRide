package com.deepak.mytaxi.ui.taxi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.deepak.mytaxi.MainActivity
import com.deepak.mytaxi.R
import com.deepak.mytaxi.TAXI
import com.deepak.mytaxi.data.model.Vehicle
import com.deepak.mytaxi.ui.VehicleListAdapter
import kotlinx.android.synthetic.main.fragment_taxi.*
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

        return inflater.inflate(R.layout.fragment_taxi, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        progressBar.visibility = View.GONE

       taxiList = arguments?.getParcelableArrayList<Vehicle>(TAXI)
        taxiLocation = arguments?.getString("address")

        vehicleListAdapter = VehicleListAdapter()
        vehicleListAdapter.submitList(taxiList)

        taxiView.apply {
            adapter = vehicleListAdapter
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
        }
    }
}