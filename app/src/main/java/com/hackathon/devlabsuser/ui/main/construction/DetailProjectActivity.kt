package com.hackathon.devlabsuser.ui.main.construction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.hackathon.devlabsuser.R
import com.hackathon.devlabsuser.databinding.ActivityDetailProjectBinding
import com.hackathon.devlabsuser.ui.main.home.DetailArticleActivity

class DetailProjectActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityDetailProjectBinding
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val city = intent.getStringExtra(CITY)
        val createdAt = intent.getStringExtra(CREATED_AT)
        val notes = intent.getStringExtra(NOTES)
        val buildingTime = intent.getStringExtra(BUILDING_TIME)
        val budget = intent.getStringExtra(BUDGET)
        val theme = intent.getStringExtra(THEME)
        val numFloor = intent.getStringExtra(NUM_FLOOR)
        val numBathroom = intent.getStringExtra(NUM_BATHROOM)
        val numRoom = intent.getStringExtra(NUM_ROOM)
        val numPerson = intent.getStringExtra(NUM_PERSON)
        val area = intent.getStringExtra(AREA)
        val buildingType = intent.getStringExtra(BUILDING_TYPE)
        val type = intent.getStringExtra(TYPE)
        val long = intent.getStringExtra(LONG)?.toDoubleOrNull()
        val lat = intent.getStringExtra(LAT)?.toDoubleOrNull()
        val amount = intent.getStringExtra(AMOUNT)
        val tax = intent.getStringExtra(TAX)
        val price = intent.getStringExtra(PRICE)
        val statusTransaction = intent.getStringExtra(STATUS_TRANSACTION)
        val transactionId = intent.getStringExtra(TRANSACTION_ID)
        val status = intent.getStringExtra(STATUS)
        val name = intent.getStringExtra(NAME)

        // Initialize the map
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Add a marker at the specified location
        val lat = intent.getStringExtra(LAT)?.toDoubleOrNull()
        val long = intent.getStringExtra(LONG)?.toDoubleOrNull()

        if (lat != null && long != null) {
            val location = LatLng(lat, long)
            googleMap.addMarker(MarkerOptions().position(location).title("Project Location"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        }
    }

    companion object {
        const val CITY = "city"
        const val CREATED_AT = "created_at"
        const val NOTES = "notes"
        const val BUILDING_TIME = "building_time"
        const val BUDGET = "budget"
        const val THEME = "theme"
        const val NUM_FLOOR = "num_floor"
        const val NUM_BATHROOM = "num_bathroom"
        const val NUM_ROOM = "num_room"
        const val NUM_PERSON = "description"
        const val AREA = "area"
        const val BUILDING_TYPE = "building_type"
        const val TYPE = "type"
        const val LONG = "long"
        const val LAT = "lat"
        const val AMOUNT = "amount"
        const val TAX = "tax"
        const val PRICE = "price"
        const val STATUS_TRANSACTION = "status_transaction"
        const val TRANSACTION_ID = "transaction_id"
        const val STATUS = "status"
        const val NAME = "name"
    }
}