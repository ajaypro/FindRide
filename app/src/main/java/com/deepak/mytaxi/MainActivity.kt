package com.deepak.mytaxi

import android.os.Bundle
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
import com.deepak.mytaxi.ui.SharedViewModel
import com.deepak.mytaxi.ui.ViewModelFactory
import com.deepak.mytaxi.ui.map.MapFragment
import com.deepak.mytaxi.ui.pool.PoolFragment
import com.deepak.mytaxi.ui.taxi.TaxiFragment
import com.deepak.mytaxi.utils.Status
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.progress_layout.*
import com.deepak.mytaxi.utils.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

const val TAXI = "taxi"
const val POOL = "pool"

class MainActivity : AppCompatActivity() {

    lateinit var sharedViewModel: SharedViewModel

    lateinit var vehicleAddress: ArrayList<Vehicle>

    var activeFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        setupViewModel()
        setUpObservers()
        setupView(savedInstanceState)

    }

     fun setActionBar(title: String = "") {
        actionBar?.title = title
    }

    fun setupView(savedInstanceState: Bundle?) {
        bottomnavigationview.run {
            //itemIconTintList = null
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.taxi_nav -> {
                        sharedViewModel.onTaxiSelected()
                        true
                    }
                    R.id.pool_nav -> {
                        sharedViewModel.onPoolSelected()
                        true
                    }
                    R.id.map_nav -> {
                        sharedViewModel.onMapSelected()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun setupViewModel() {
        sharedViewModel = ViewModelProvider(this,
            ViewModelFactory (Networking.create(BuildConfig.BASE_URL, application.cacheDir, 10 * 1024 * 1024), this.application)
        ).get(SharedViewModel::class.java)
    }

    private fun setUpObservers() {
        sharedViewModel.getVehicles().observe(this, Observer {
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

            sharedViewModel.taxiNavigation.observe(this@MainActivity, Observer {
                it.getIfNotHandled()?.run { showTaxi(taxi as ArrayList<Vehicle>) }
            })

            sharedViewModel.poolNavigation.observe(this@MainActivity, Observer {
                it.getIfNotHandled()?.run { showPool(pool as ArrayList<Vehicle>) }
            })

            sharedViewModel.mapNavigation.observe(this@MainActivity, Observer {
                it.getIfNotHandled()?.run { showMap(any = "Maps") }
            })


//        val taxiList = ArrayList<Vehicle>()
//        val poolList = ArrayList<Vehicle>()
//        vehicleList.forEach {
//            if (it.fleetType == "TAXI"){
//                taxiList.add(it)
//            } else {
//                poolList.add(it)
//            }
//        }


//          val taxiBundle = Bundle()
//          val poolBundle = Bundle()
//        poolBundle.putParcelableArrayList (POOL, pool as ArrayList<out Parcelable>)
//        taxiBundle.putParcelableArrayList (TAXI, taxi as ArrayList<out Parcelable>)

             //findNavController(R.id.fragmentcontainer).setGraph(R.navigation.taxi_navigation, taxiBundle)
              //findNavController(R.id.fragmentcontainer).setGraph(R.navigation.pool_navigation, poolBundle)
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

    private fun showMap(any: Any) {
        if (activeFragment is MapFragment) return

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        var fragment = supportFragmentManager.findFragmentByTag(MapFragment.TAG) as MapFragment?


        if (fragment == null) {
            fragment = MapFragment.newInstance()

//            val poolBundle = Bundle()
//            poolBundle.putParcelableArrayList (POOL, pool as ArrayList<out Parcelable>)
//            fragment.arguments = poolBundle

            fragmentTransaction.add(R.id.fragmentcontainer, fragment, MapFragment.TAG)
        } else {
            fragmentTransaction.show(fragment)
        }

        if (activeFragment != null) fragmentTransaction.hide(activeFragment as Fragment)

        fragmentTransaction.commit()

        activeFragment = fragment
    }

    suspend fun getAddress(vehicleList: ArrayList<Vehicle>, coroutineContext: CoroutineContext): ArrayList<Vehicle> {

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
