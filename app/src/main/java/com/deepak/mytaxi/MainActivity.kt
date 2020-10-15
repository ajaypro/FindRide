package com.deepak.mytaxi

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.deepak.mytaxi.data.model.Vehicles
import com.deepak.mytaxi.data.remote.Networking
import com.deepak.mytaxi.ui.SharedViewModel
import com.deepak.mytaxi.ui.ViewModelFactory
import com.deepak.mytaxi.utils.Status
import com.deepak.mytaxi.utils.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.progress_layout.*

const val TAXI = "taxi"
const val POOL = "pool"

class MainActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null

    lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
        setupViewModel()
        setUpObservers()


        //val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_taxi,
            R.id.navigation_pool,
            R.id.navigation_map
        ))
       // setupActionBarWithNavController(navController, appBarConfiguration)
        //bottomNavigationView.setupWithNavController(navController)
    }

    private fun setupViewModel() {
        sharedViewModel = ViewModelProvider(this,
            ViewModelFactory (Networking.create(BuildConfig.BASE_URL, application.cacheDir, 10 * 1024 * 1024))
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

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {


        val navGraphIds = listOf(R.navigation.taxi_navigation, R.navigation.pool_navigation, R.navigation.map_navigation)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottom_nav_view.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment,
            intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
        })
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

//    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()

    private fun navigateToFragments(vehicleList: Vehicles) {
//            val(taxi, pool) = vehicleList.poiList.partition { it.isTaxi }
//        vehicleList.poiList
//            .filter { it.isTaxi }

       val(taxi, pool) = vehicleList.poiList.partition { it.isTaxiOrPool(it.fleetType) }

//        val taxiList = ArrayList<Vehicle>()
//        val poolList = ArrayList<Vehicle>()
//        vehicleList.forEach {
//            if (it.fleetType == "TAXI"){
//                taxiList.add(it)
//            } else {
//                poolList.add(it)
//            }
//        }


          val taxiBundle = Bundle()
          val poolBundle = Bundle()
        poolBundle.putParcelableArrayList (POOL, pool as ArrayList<out Parcelable>)
        taxiBundle.putParcelableArrayList (TAXI, taxi as ArrayList<out Parcelable>)

              findNavController(R.id.nav_host_fragment).setGraph(R.navigation.taxi_navigation, taxiBundle)
              findNavController(R.id.nav_host_fragment).setGraph(R.navigation.pool_navigation, poolBundle)
    }
}
