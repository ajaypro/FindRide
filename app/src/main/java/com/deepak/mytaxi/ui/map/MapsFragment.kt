package com.deepak.mytaxi.ui.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.deepak.mytaxi.MainActivity
import com.deepak.mytaxi.R
import com.deepak.mytaxi.data.model.Vehicle
import com.deepak.mytaxi.utils.KeyConstants
import com.deepak.mytaxi.utils.KeyConstants.MAP
import com.deepak.mytaxi.utils.Toaster
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapsFragment : Fragment(), OnMapReadyCallback {

    lateinit var mMap: GoogleMap

    lateinit var mapViewModel: MapViewModel

    private var vehicleListOnMap: ArrayList<Vehicle>? = null
    private val builder = LatLngBounds.Builder()

    companion object {

        const val TAG = "MapFragment"

        fun newInstance(): MapsFragment {
            val args = Bundle()
            val fragment = MapsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setupViewModel()

        (activity as MainActivity).mainViewModel.selectedVehicle.observe(
            viewLifecycleOwner,
            Observer {
                it.getIfNotHandled().run {
                    this?.let {
                        mapViewModel.onSelectedVehicle(this)
                    }
                }
            })

        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    private fun setupViewModel() {
        mapViewModel = ViewModelProvider(
            this,
            MapViewModelFactory()
        ).get(MapViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_frag_container) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // Set m variable to gmap
        mMap = googleMap

        vehicleListOnMap = arguments?.getParcelableArrayList<Vehicle>(MAP)

        loadVehicleOnMap(vehicleListOnMap)

        mapViewModel.refreshVehicle.observe(viewLifecycleOwner, Observer {
            it?.let {
                focusMapCameraOnLocation(it, 15f)
            }
        })

    }

    private fun loadVehicleOnMap(vehicleListOnMap: ArrayList<Vehicle>?) {

        if (!vehicleListOnMap.isNullOrEmpty()) {
            plotVehicleOnMap(vehicleListOnMap)

        } else {
            Toaster.show(this.requireContext(), getString(R.string.error_no_vehicles))
        }

    }

    private fun plotVehicleOnMap(vehicleListOnMap: ArrayList<Vehicle>) {

        try {
            vehicleListOnMap.forEach { vehicle ->
                focusMapCameraOnLocation(vehicle)
            }

        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }


    private fun focusMapCameraOnLocation(vehicle: Vehicle, zoomDefaultValue: Float = 10f) {

        builder.include(LatLng(vehicle.coordinate.longitude, vehicle.coordinate.latitude))
        mMap.addMarker(
            MarkerOptions()
                .title(vehicle.coordinate.addressFromLatLong)
                .position(LatLng(vehicle.coordinate.longitude, vehicle.coordinate.latitude))
                .icon(
                    BitmapDescriptorFactory.fromResource(
                        if (vehicle.fleetType == KeyConstants.POOL)
                            R.drawable.ic_map_pool
                        else
                            R.drawable.ic_map_taxi
                    )
                )
        )
        mMap.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.builder()
                    .target(LatLng(vehicle.coordinate.longitude, vehicle.coordinate.latitude))
                    .zoom(zoomDefaultValue)
                    .bearing(0f)
                    .build()
            ), 1000, onMapCameraComplete
        )

    }

    /**
     *  Map camera animation complete
     */
    private val onMapCameraComplete = object : GoogleMap.CancelableCallback {
        override fun onFinish() {
            Log.d(TAG, "Camera animation complete")
        }

        override fun onCancel() {
            Log.d(TAG, "Camera animation cancelled")
        }
    }

}