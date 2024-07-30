package com.hackathon.devlabsuser.ui.main.home

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.hackathon.devlabsuser.R
import com.hackathon.devlabsuser.databinding.*
import com.hackathon.devlabsuser.viewmodel.QuestionnaireViewModel
import kotlinx.coroutines.launch
import com.hackathon.devlabsuser.BR
import java.util.*

class QuestionnaireDialogFragment : DialogFragment(), OnMapReadyCallback {

    private val viewModel: QuestionnaireViewModel by viewModels()
    private lateinit var binding: ViewDataBinding
    private lateinit var map: GoogleMap
    private lateinit var geocoder: Geocoder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false)
        binding.lifecycleOwner = this // Tambahkan ini untuk menghubungkan ViewModel dengan Binding
        binding.setVariable(BR.viewModel, viewModel) // Hubungkan ViewModel dengan Binding
        return binding.root
    }

    private fun getLayoutResId(): Int {
        return when (viewModel.currentQuestion) {
            0 -> R.layout.question_service_type
            1 -> R.layout.question_project_location
            2 -> R.layout.question_project_need_type
            3 -> R.layout.question_building_type
            4 -> R.layout.question_building_area
            5 -> R.layout.question_num_occupants
            6 -> R.layout.question_num_rooms
            7 -> R.layout.question_num_bathrooms
            8 -> R.layout.question_num_floors
            9 -> R.layout.question_theme
            else -> throw IllegalArgumentException("Invalid question index")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        geocoder = Geocoder(requireContext(), Locale.getDefault())

        if (viewModel.currentQuestion == 1) {
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)

            val searchView = view.findViewById<SearchView>(R.id.search_view)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        searchLocation(it)
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
        bindViews()
    }

    private fun searchLocation(query: String) {
        val addressList = geocoder.getFromLocationName(query, 1)
        if (addressList != null && addressList.isNotEmpty()) {
            val address = addressList[0]
            val latLng = LatLng(address.latitude, address.longitude)
            map.clear()
            map.addMarker(MarkerOptions().position(latLng).title(query))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            viewModel.selectedLocation = latLng
        } else {
            Toast.makeText(requireContext(), "Lokasi tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun bindViews() {
        when (viewModel.currentQuestion) {
            0 -> setupQuestion1()
            1 -> setupQuestion2()
            2 -> setupQuestion3()
            3 -> setupQuestion4()
            4 -> setupQuestion5()
            5 -> setupQuestion6()
            6 -> setupQuestion7()
            7 -> setupQuestion8()
            8 -> setupQuestion9()
            9 -> setupQuestion10()
        }
    }

    private fun setupQuestion1() {
        val questionBinding = binding as QuestionServiceTypeBinding
        questionBinding.buttonNext.setOnClickListener {
            val selectedRadioButton = questionBinding.radioGroupServiceType.checkedRadioButtonId
            val answer = when (selectedRadioButton) {
                R.id.radio_design -> "Desain"
                R.id.radio_build -> "Bangun"
                R.id.radio_design_and_build -> "Desain dan Bangun"
                else -> ""
            }
            saveAnswerAndMoveToNext(answer)
        }
    }

    private fun setupQuestion2() {
        val questionBinding = binding as QuestionProjectLocationBinding
        questionBinding.buttonNext.setOnClickListener {
            val location = viewModel.selectedLocation?.let { "${it.latitude},${it.longitude}" } ?: "No Location Selected"
            saveAnswerAndMoveToNext(location)
        }
    }

    private fun setupQuestion3() {
        val questionBinding = binding as QuestionProjectNeedTypeBinding
        questionBinding.buttonNext.setOnClickListener {
            val selectedRadioButton = questionBinding.radioGroupProjectNeedType.checkedRadioButtonId
            val answer = when (selectedRadioButton) {
                R.id.radio_new_build -> "Bangun Baru"
                R.id.radio_renovation -> "Renovasi"
                else -> ""
            }
            saveAnswerAndMoveToNext(answer)
        }
    }

    private fun setupQuestion4() {
        val questionBinding = binding as QuestionBuildingTypeBinding
        questionBinding.buttonNext.setOnClickListener {
            val selectedRadioButton = questionBinding.radioGroupBuildingType.checkedRadioButtonId
            val answer = when (selectedRadioButton) {
                R.id.radio_house -> "Rumah"
                R.id.radio_apartment -> "Apartment"
                R.id.radio_office -> "Kantor"
                R.id.radio_restaurant -> "Restaurant"
                R.id.radio_commercial -> "Komersial/Retail"
                R.id.radio_other -> "Tulis sendiri"
                else -> ""
            }
            saveAnswerAndMoveToNext(answer)
        }
    }

    private fun setupQuestion5() {
        val questionBinding = binding as QuestionBuildingAreaBinding
        questionBinding.buttonNext.setOnClickListener {
            val answer = questionBinding.inputBuildingArea.text.toString()
            saveAnswerAndMoveToNext(answer)
        }
    }

    private fun setupQuestion6() {
        val questionBinding = binding as QuestionNumOccupantsBinding
        questionBinding.buttonNext.setOnClickListener {
            val answer = questionBinding.inputNumOccupants.text.toString()
            saveAnswerAndMoveToNext(answer)
        }
    }

    private fun setupQuestion7() {
        val questionBinding = binding as QuestionNumRoomsBinding
        questionBinding.buttonNext.setOnClickListener {
            val answer = questionBinding.inputNumRooms.text.toString()
            saveAnswerAndMoveToNext(answer)
        }
    }

    private fun setupQuestion8() {
        val questionBinding = binding as QuestionNumBathroomsBinding
        questionBinding.buttonNext.setOnClickListener {
            val answer = questionBinding.inputNumBathrooms.text.toString()
            saveAnswerAndMoveToNext(answer)
        }
    }

    private fun setupQuestion9() {
        val questionBinding = binding as QuestionNumFloorsBinding
        questionBinding.buttonNext.setOnClickListener {
            val answer = questionBinding.inputNumFloors.text.toString()
            saveAnswerAndMoveToNext(answer)
        }
    }

    private fun setupQuestion10() {
        val questionBinding = binding as QuestionThemeBinding
        questionBinding.buttonNext.setOnClickListener {
            val answer = questionBinding.inputTheme.text.toString()
            saveAnswerAndMoveToNext(answer)
        }
    }

    private fun saveAnswerAndMoveToNext(answer: String) {
        viewModel.answers.add(answer)
        viewModel.currentQuestion++
        if (viewModel.currentQuestion < 10) {
            updateLayoutForCurrentQuestion()
        } else {
            submitAnswers()
        }
    }

    private fun updateLayoutForCurrentQuestion() {
        val inflater = layoutInflater
        val container = view?.parent as? ViewGroup
        container?.removeView(view)
        binding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false)
        container?.addView(binding.root)
        bindViews()
    }

    private fun submitAnswers() {
        // Send the answers to the server
        lifecycleScope.launch {
            // Implement the API call here
        }
        dismiss()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        Log.d("QuestionnaireDialogFragment", "Map is ready") // Tambahkan log ini
        map.setOnMapClickListener { latLng ->
            Log.d("QuestionnaireDialogFragment", "Map clicked at: ${latLng.latitude}, ${latLng.longitude}") // Tambahkan log ini
            map.clear()
            map.addMarker(MarkerOptions().position(latLng).title("Selected Location"))
            viewModel.selectedLocation = latLng // Simpan lokasi yang dipilih ke ViewModel
        }
        val defaultLocation = LatLng(-34.0, 151.0)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))
    }
}
