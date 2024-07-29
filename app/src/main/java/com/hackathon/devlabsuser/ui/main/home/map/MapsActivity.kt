package com.hackathon.devlabsuser.ui.main.home.map

import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.hackathon.devlabsuser.R
import com.hackathon.devlabsuser.databinding.ActivityMapsBinding
import com.hackathon.devlabsuser.ui.authentication.AuthenticationManager

class MapsActivity : AppCompatActivity() {

    private lateinit var mMap: GoogleMap
    private lateinit var authentication: AuthenticationManager
    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var fusedLocation: FusedLocationProviderClient
    private lateinit var binding: ActivityMapsBinding

    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        authentication = AuthenticationManager(this)
//        mapsViewModel = ViewModelProvider(this)[MapsViewModel::class.java]
//        mapsViewModel.getStoryLocation(authentication.getToken(AuthenticationManager.TOKEN).toString())
//        fusedLocation = LocationServices.getFusedLocationProviderClient(this)
//
//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
    }

//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//
//        mMap.uiSettings.isZoomControlsEnabled = true
//        mMap.uiSettings.isIndoorLevelPickerEnabled = true
//        mMap.uiSettings.isCompassEnabled = true
//        mMap.uiSettings.isMapToolbarEnabled = true
//
//        mapsViewModel.getSomeLocation().observe(this) {
//            if (it != null) {
//                addLocationMark(it)
//            }
//        }
//
//        setMapStyle()
//    }
//
//    private fun addLocationMark(stories: List<Story>) {
//
//        stories.forEach { user ->
//            val latLng = LatLng(user.lat, user.lon)
//            mMap.addMarker(
//                MarkerOptions().position(latLng).title(user.name).snippet(user.description)
//                .icon(BitmapDescriptorFactory.defaultMarker()))
//            boundsBuilder.include(latLng)
//        }
//        val bounds: LatLngBounds = boundsBuilder.build()
//
//        mMap.animateCamera(
//            CameraUpdateFactory.newLatLngBounds(
//                bounds,
//                resources.displayMetrics.widthPixels,
//                resources.displayMetrics.heightPixels,
//                30
//            )
//        )
//    }
//
//    private val requestPermissionLauncher =
//        registerForActivityResult(
//            ActivityResultContracts.RequestPermission()
//        ) { isGranted: Boolean ->
//            if (isGranted) {
//                getMyLocation()
//            }
//        }
//
//    private fun getMyLocation() {
//        if (ContextCompat.checkSelfPermission(
//                this.applicationContext,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            mMap.isMyLocationEnabled = true
//        } else {
//            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
//        }
//    }
//
//    // Apply style
//    private fun setMapStyle() {
//        try {
//            val success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
//            if (!success) {
//                Log.e(TAG, "Style parsing failed.")
//            }
//        } catch (exception: Resources.NotFoundException) {
//            Log.e(TAG, "Can't find style. Error: ", exception)
//        }
//    }
//
//    companion object {
//        private const val TAG = "MapsActivity"
//    }
}