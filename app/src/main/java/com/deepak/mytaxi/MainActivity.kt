package com.deepak.mytaxi

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.deepak.mytaxi.data.model.Vehicle
import com.deepak.mytaxi.data.model.Vehicles
import com.deepak.mytaxi.data.remote.Networking
import com.deepak.mytaxi.ui.MainViewModel
import com.deepak.mytaxi.ui.MainViewModelFactory
import com.deepak.mytaxi.ui.map.MapsFragment
import com.deepak.mytaxi.ui.pool.PoolFragment
import com.deepak.mytaxi.ui.taxi.TaxiFragment
import com.deepak.mytaxi.utils.KeyConstants.MAP
import com.deepak.mytaxi.utils.KeyConstants.POOL
import com.deepak.mytaxi.utils.KeyConstants.TAXI
import com.deepak.mytaxi.utils.Status
import com.deepak.mytaxi.utils.getLocation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.progress_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity() {

    lateinit var mainViewModel: MainViewModel

    lateinit var vehicleAddress: ArrayList<Vehicle>

    var activeFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewModel()
        setUpObservers()
        setupView(savedInstanceState)

    }

    private fun setupView(savedInstanceState: Bundle?) {
        bottomnavigationview.run {
            //itemIconTintList = null
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.taxi_nav -> {
                        mainViewModel.onTaxiSelected()
                        true
                    }
                    R.id.pool_nav -> {
                        mainViewModel.onPoolSelected()
                        true
                    }
                    R.id.map_nav -> {
                        mainViewModel.onMapSelected()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(this,
            MainViewModelFactory (Networking.create(BuildConfig.BASE_URL, application.cacheDir, 10 * 1024 * 1024))
        ).get(MainViewModel::class.java)
    }

    private fun setUpObservers() {
        mainViewModel.getVehicles().observe(this, Observer {
            it?.let { resource ->
                when(resource.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        resource.data?.let { vehicleList ->
                            navigateToFragments(vehicleList)
                        }
                    }
                    Status.ERROR -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                         progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun navigateToFragments(vehicleList: Vehicles) {
        var finalVehicleList =  ArrayList<Vehicle>()

        lifecycleScope.launch {
             finalVehicleList =
                getAddress(vehicleList.poiList as ArrayList<Vehicle>, this.coroutineContext)
        }

            val (taxi, pool) = finalVehicleList.partition { it.isTaxiOrPool(it.fleetType) }

            mainViewModel.taxiNavigation.observe(this, Observer {
                it.getIfNotHandled()?.run { showTaxi(taxi as ArrayList<Vehicle>) }
            })

            mainViewModel.poolNavigation.observe(this, Observer {
                it.getIfNotHandled()?.run { showPool(pool as ArrayList<Vehicle>) }
            })

            mainViewModel.mapNavigation.observe(this, Observer {
                it.getIfNotHandled()?.run { showMap(finalVehicleList) }
            })

           mainViewModel.navigateToMapFragment.observe( this, Observer {
               it.getIfNotHandled()?.run { bottomnavigationview.selectedItemId = R.id.map_nav }
               })

    }


    private fun showTaxi(taxi: ArrayList<Vehicle>) {
        if (activeFragment is TaxiFragment) return

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        var fragment = supportFragmentManager.findFragmentByTag(TaxiFragment.TAG) as TaxiFragment?

        if (fragment == null) {
            fragment = TaxiFragment.newInstance()

                val taxiBundle = Bundle()
                taxiBundle.putParcelableArrayList(TAXI, taxi)
                fragment.arguments = taxiBundle


            fragmentTransaction.add(R.id.fragmentcontainer, fragment, TaxiFragment.TAG)
        } else {
            fragmentTransaction.show(fragment)
        }

        if (activeFragment != null) fragmentTransaction.hide(activeFragment as Fragment)

        fragmentTransaction.commit()

        activeFragment = fragment
    }

    private fun showPool(pool: ArrayList<Vehicle>) {
        if (activeFragment is PoolFragment) return

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        var fragment = supportFragmentManager.findFragmentByTag(PoolFragment.TAG) as PoolFragment?


        if (fragment == null) {
            fragment = PoolFragment.newInstance()

                val poolBundle = Bundle()
                poolBundle.putParcelableArrayList(POOL, pool)
                fragment.arguments = poolBundle


            fragmentTransaction.add(R.id.fragmentcontainer, fragment, PoolFragment.TAG)
        } else {
            fragmentTransaction.show(fragment)
        }

        if (activeFragment != null) fragmentTransaction.hide(activeFragment as Fragment)

        fragmentTransaction.commit()

        activeFragment = fragment
    }

    private fun showMap(vehicleList: ArrayList<Vehicle>) {
        if (activeFragment is MapsFragment) return

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        var fragment = supportFragmentManager.findFragmentByTag(MapsFragment.TAG) as MapsFragment?


        if (fragment == null) {
            fragment = MapsFragment.newInstance()

            val mapBundle = Bundle()
            mapBundle.putParcelableArrayList (MAP, vehicleList as ArrayList<out Parcelable>)
            fragment.arguments = mapBundle

            fragmentTransaction.add(R.id.fragmentcontainer, fragment, MapsFragment.TAG)
        } else {
            fragmentTransaction.show(fragment)
        }

        if (activeFragment != null) fragmentTransaction.hide(activeFragment as Fragment)

        fragmentTransaction.commit()

        activeFragment = fragment
    }

    private suspend fun getAddress(vehicleList: ArrayList<Vehicle>, coroutineContext: CoroutineContext): ArrayList<Vehicle> {

        return withContext(coroutineContext) {
            vehicleListWithLocation(
                vehicleList,
                this
            )
        }
    }

    private suspend  fun vehicleListWithLocation(vehicleList: ArrayList<Vehicle>, coroutineScope: CoroutineScope): ArrayList<Vehicle> {

        vehicleAddress = vehicleList

        for (i in 0 until vehicleList.size) {

            vehicleAddress[i].coordinate.addressFromLatLong = getLocation(vehicleList[i].coordinate, this, coroutineScope)
        }
        return vehicleAddress
    }


}
