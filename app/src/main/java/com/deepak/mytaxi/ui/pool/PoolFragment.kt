package com.deepak.mytaxi.ui.pool

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.deepak.mytaxi.MainActivity
import com.deepak.mytaxi.POOL
import com.deepak.mytaxi.R
import com.deepak.mytaxi.data.model.Vehicle
import com.deepak.mytaxi.ui.VehicleListAdapter
import kotlinx.android.synthetic.main.fragment_pool.*
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

        return inflater.inflate(R.layout.fragment_pool, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        progressBar.visibility = View.GONE

        poolList = arguments?.getParcelableArrayList<Vehicle>(POOL)
        vehicleListAdapter = VehicleListAdapter()

        vehicleListAdapter.submitList(poolList)

        poolView.apply {
            adapter = vehicleListAdapter
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
        }

    }
}