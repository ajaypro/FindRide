package com.deepak.mytaxi.ui.taxi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.deepak.mytaxi.R
import com.deepak.mytaxi.data.model.Vehicle
import com.deepak.mytaxi.ui.VehicleListAdapter
import kotlinx.android.synthetic.main.fragment_taxi.*
import kotlinx.android.synthetic.main.progress_layout.*

class TaxiFragment : Fragment() {

//    companion object {
//
//        const val TAG = "TaxiFragment"
//
//        fun newInstance(args: Bundle) : TaxiFragment {
//                val fragment = TaxiFragment()
//                fragment.arguments = args
//            return fragment
//            }
//
//        }



    lateinit var vehicleListAdapter: VehicleListAdapter

    lateinit var taxiList: ArrayList<Vehicle>


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_taxi, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar.visibility = View.GONE

        taxiList = requireArguments().getParcelableArrayList<Vehicle>("taxi") as ArrayList<Vehicle>

        vehicleListAdapter = VehicleListAdapter()
        vehicleListAdapter.submitList(taxiList)

        taxiView.apply {
            adapter = vehicleListAdapter
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
        }






    }
}