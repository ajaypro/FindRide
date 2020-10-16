package com.deepak.mytaxi.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.deepak.mytaxi.BuildConfig
import com.deepak.mytaxi.data.model.Vehicle
import com.deepak.mytaxi.data.model.Vehicles
import com.deepak.mytaxi.data.remote.Networking
import com.deepak.mytaxi.utils.Status
import kotlinx.android.synthetic.main.progress_layout.*

abstract class BaseFragment: Fragment() {

    lateinit var sharedViewModel: SharedViewModel

    lateinit var poolList: ArrayList<Vehicle>

    lateinit var taxiList: ArrayList<Vehicle>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViewModel()
        setUpObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    private fun setupViewModel() {
        sharedViewModel = ViewModelProvider(this,
            ViewModelFactory (Networking.create(BuildConfig.BASE_URL,
                activity?.application?.cacheDir!!, 10 * 1024 * 1024))
        ).get(SharedViewModel::class.java)
    }

    private fun setUpObservers() {
        sharedViewModel.getVehicles().observe(this, Observer {
            it?.let { resource ->
                when(resource.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        resource.data.let { vehicleList ->
                            navigateToFragments(vehicleList)
                        }
                    }
                    Status.ERROR -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this.activity, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun navigateToFragments(vehicleList: Vehicles?) {

        vehicleList?.let {

            val (taxi, pool) = vehicleList.poiList.partition { it.isTaxiOrPool(it.fleetType) }

            poolList = pool as ArrayList<Vehicle>
            taxiList = taxi as ArrayList<Vehicle>

        }
    }

     fun getPoolList(poolList: ArrayList<Vehicle>): ArrayList<Vehicle>  = poolList
     fun getTaxiList(taxiList: ArrayList<Vehicle>): ArrayList<Vehicle>  = taxiList

    @LayoutRes
    protected abstract fun provideLayoutId(): Int

    protected abstract fun setupView(view: View)
}