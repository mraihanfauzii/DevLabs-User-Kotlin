package com.hackathon.devlabsuser.ui.main.construction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.hackathon.devlabsuser.R
import com.hackathon.devlabsuser.databinding.ActivityDetailProjectBinding
import com.hackathon.devlabsuser.ui.authentication.AuthenticationManager
import com.hackathon.devlabsuser.viewmodel.ProgressViewModel

class DetailProjectActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityDetailProjectBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var authManager: AuthenticationManager
    private lateinit var progressViewModel: ProgressViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val projectId = intent.getStringExtra(ID)

        authManager = AuthenticationManager(this)
        val getToken = authManager.getAccess(AuthenticationManager.TOKEN).toString()
        val token = "Bearer $getToken"

        progressViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[ProgressViewModel::class.java]

        progressViewModel.isLoading.observe(this) { loading ->
            showLoading(loading)
        }

        progressViewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        progressViewModel.getProjectsDataById(token, projectId!!)

        progressViewModel.projectDataById.observe(this) { project ->
            // Update UI with the project details
            binding.apply {
                tvProjectName.text = project.firstOrNull()!!.name ?: "Project Name Unavailable"
                tvBudget.text = project.firstOrNull()!!.price ?: "N/A"
                tvLocation.text = project.firstOrNull()!!.city ?: "N/A"
            }

            val lat = project.firstOrNull()!!.lat?.toDoubleOrNull()
            val long = project.firstOrNull()!!.long?.toDoubleOrNull()

            if (lat != null && long != null && ::googleMap.isInitialized) {
                val location = LatLng(lat, long)
                googleMap.addMarker(MarkerOptions().position(location).title("Project Location"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
            } else {
                Toast.makeText(applicationContext, "Invalid location coordinates", Toast.LENGTH_SHORT).show()
            }
        }

        // Initialize the map
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        // Set up marker placement when the map is ready
        progressViewModel.projectDataById.observe(this) { project ->
            val lat = project.firstOrNull()!!.lat?.toDoubleOrNull()
            val long = project.firstOrNull()!!.long?.toDoubleOrNull()

            if (lat != null && long != null) {
                val location = LatLng(lat, long)
                googleMap.addMarker(MarkerOptions().position(location).title("Project Location"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
            } else {
                Toast.makeText(applicationContext, "Invalid location coordinates", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val ID = "id"
    }
}
