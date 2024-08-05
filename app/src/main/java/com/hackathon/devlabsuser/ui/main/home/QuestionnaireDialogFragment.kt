package com.hackathon.devlabsuser.ui.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.hackathon.devlabsuser.R
import com.hackathon.devlabsuser.databinding.*
import com.hackathon.devlabsuser.viewmodel.QuestionnaireViewModel
import kotlinx.coroutines.launch
import com.hackathon.devlabsuser.BR
import com.hackathon.devlabsuser.model.ProjectRequest
import com.hackathon.devlabsuser.ui.authentication.AuthenticationManager
import com.hackathon.devlabsuser.viewmodel.HomeViewModel

class QuestionnaireDialogFragment : DialogFragment(), OnMapReadyCallback {

    private val viewModel: QuestionnaireViewModel by viewModels({ requireParentFragment() })
    private val homeViewModel: HomeViewModel by viewModels({ requireParentFragment() })
    private lateinit var authManager: AuthenticationManager
    private lateinit var binding: ViewDataBinding
    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false)
        binding.lifecycleOwner = this
        binding.setVariable(BR.viewModel, viewModel)
        return binding.root
    }

    private fun getLayoutResId(): Int {
        return when (viewModel.currentQuestion) {
            0 -> R.layout.question_service_type
            1 -> R.layout.question_project_location
            2 -> R.layout.question_project_need_type
            3 -> R.layout.question_building_type
            4 -> R.layout.question_building_area_and_num_floors
            5 -> R.layout.question_combined
            6 -> R.layout.question_theme
            7 -> R.layout.question_summary
            else -> throw IllegalArgumentException("Invalid question index")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Places.
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), "AIzaSyA82FIXRqsMilx4QkwhAOyobGC5H8TtjeA")
        }

        authManager = AuthenticationManager(requireContext())

        homeViewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        bindViews()
    }

    private fun bindViews() {
        Log.d("Questionnaire", "bindViews called for question ${viewModel.currentQuestion}")
        when (viewModel.currentQuestion) {
            0 -> setupQuestion1()
            1 -> setupQuestion2()
            2 -> setupQuestion3()
            3 -> setupQuestion4()
            4 -> setupQuestion5and6()
            5 -> setupQuestion7to9()
            6 -> setupQuestion10()
            7 -> setupSummary()
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
            if (answer.isEmpty()) {
                Toast.makeText(requireContext(), "Pilih salah satu layanan", Toast.LENGTH_SHORT).show()
            } else {
                saveAnswerAndMoveToNext(answer)
            }
        }
    }

    private fun setupQuestion2() {
        val questionBinding = binding as QuestionProjectLocationBinding

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        // Setup Place Autocomplete
        val autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?
        autocompleteFragment?.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
        autocompleteFragment?.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                place.latLng?.let {
                    updateMapLocation(it, place.name ?: "Selected Location")
                }
            }

            override fun onError(status: com.google.android.gms.common.api.Status) {
                Toast.makeText(requireContext(), "An error occurred: ${status.statusMessage}", Toast.LENGTH_LONG).show()
            }
        })

        questionBinding.buttonNext.setOnClickListener {
            val location = viewModel.selectedLocation
            val placeName = viewModel.selectedLocationName ?: "No Location Selected"
            if (location == null) {
                Toast.makeText(requireContext(), "Pilih lokasi proyek", Toast.LENGTH_SHORT).show()
            } else {
                saveAnswerAndMoveToNext("$placeName (Lat: ${location.latitude}, Long: ${location.longitude})", reopen = true)
            }
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
            if (answer.isEmpty()) {
                Toast.makeText(requireContext(), "Pilih salah satu jenis proyek", Toast.LENGTH_SHORT).show()
            } else {
                saveAnswerAndMoveToNext(answer)
            }
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
            if (answer.isEmpty()) {
                Toast.makeText(requireContext(), "Pilih salah satu tipe bangunan", Toast.LENGTH_SHORT).show()
            } else {
                saveAnswerAndMoveToNext(answer)
            }
        }
    }

    private fun setupQuestion5and6() {
        val questionBinding = binding as QuestionBuildingAreaAndNumFloorsBinding
        questionBinding.buttonNext.setOnClickListener {
            val buildingArea = questionBinding.inputBuildingArea.text.toString()
            val numFloors = questionBinding.inputNumFloors.text.toString()

            if (buildingArea.isEmpty() || numFloors.isEmpty()) {
                Toast.makeText(requireContext(), "Isi luas bangunan dan jumlah lantai", Toast.LENGTH_SHORT).show()
            } else {
                saveAnswerAndMoveToNext("$buildingArea, Jumlah Lantai: $numFloors")
            }
        }
    }

    private fun setupQuestion7to9() {
        val questionBinding = binding as QuestionCombinedBinding
        questionBinding.buttonNext.setOnClickListener {
            val numOccupants = questionBinding.inputNumOccupants.text.toString()
            val numRooms = questionBinding.inputNumRooms.text.toString()
            val numBathrooms = questionBinding.inputNumBathrooms.text.toString()

            if (numOccupants.isEmpty() || numRooms.isEmpty() || numBathrooms.isEmpty()) {
                Toast.makeText(requireContext(), "Isi semua pertanyaan", Toast.LENGTH_SHORT).show()
            } else {
                saveAnswerAndMoveToNext("$numOccupants, Jumlah Kamar: $numRooms, Jumlah Kamar Mandi: $numBathrooms")
            }
        }
    }

    private fun setupQuestion10() {
        val questionBinding = binding as QuestionThemeBinding
        val themeSpinner = questionBinding.themeSpinner

        questionBinding.buttonNext.setOnClickListener {
            val selectedTheme = themeSpinner.selectedItem.toString()
            if (selectedTheme.isEmpty()) {
                Toast.makeText(requireContext(), "Pilih salah satu tema", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.selectedTheme = selectedTheme
                saveAnswerAndMoveToNext("Tema: $selectedTheme")
            }
        }
    }

    private fun setupSummary() {
        val questionBinding = binding as QuestionSummaryBinding
        val summaryText = questionBinding.summaryText
        val buttonReset = questionBinding.buttonReset
        val buttonSubmit = questionBinding.buttonSubmit

        // Display the summary of answers with questions
        val summaryBuilder = StringBuilder()
        val questions = listOf(
            "1. Jenis Layanan",
            "2. Lokasi Proyek",
            "3. Jenis Proyek",
            "4. Tipe Bangunan",
            "5. Luas Bangunan",
            "6. Jumlah Penghuni",
            "7. Tema",
            "8. Jumlah Kamar",
            "9. Jumlah Kamar Mandi",
            "10. Tema"
        )

        viewModel.answers.forEachIndexed { index, answer ->
            val question = if (index < questions.size) questions[index] else "Pertanyaan Tidak Diketahui"
            summaryBuilder.append("<b>$question:</b> $answer<br><br>")
        }

        summaryText.text = HtmlCompat.fromHtml(summaryBuilder.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)

        buttonReset.setOnClickListener {
            // Restart the questionnaire
            viewModel.currentQuestion = 0
            viewModel.answers.clear()
            dismiss()
            dismissAndReopen()
        }

        buttonSubmit.setOnClickListener {
            // Submit the answers to the server
            submitAnswers()
        }
    }

    private fun saveAnswerAndMoveToNext(answer: String, reopen: Boolean = true) {
        Log.d("Questionnaire", "Saving answer: $answer")
        viewModel.answers.add(answer)
        viewModel.currentQuestion++
        Log.d("Questionnaire", "Current question: ${viewModel.currentQuestion}")

        if (viewModel.currentQuestion < 8) {
            if (reopen) {
                dismissAndReopen()
            } else {
                updateLayoutForCurrentQuestion()
            }
        } else {
            submitAnswers()
        }
    }

    private fun updateLayoutForCurrentQuestion() {
        Log.d("Questionnaire", "Updating layout for question ${viewModel.currentQuestion}")
        val container = view?.parent as? ViewGroup
        container?.removeView(binding.root)
        binding = DataBindingUtil.inflate(layoutInflater, getLayoutResId(), container, false)
        binding.lifecycleOwner = this
        binding.setVariable(BR.viewModel, viewModel)
        container?.addView(binding.root)
        bindViews()
    }

    private fun dismissAndReopen() {
        dismiss()
        parentFragment?.let {
            if (it is HomeFragment) {
                it.openQuestionnaire(viewModel.currentQuestion)
            }
        }
    }

    private fun submitAnswers() {
        // Pastikan bahwa `viewModel.answers` memiliki semua jawaban
        val projectRequest = ProjectRequest(
            projectName = "Rumah kedua",
            name = viewModel.answers[0], // Jasa
            lat = viewModel.selectedLocation?.latitude ?: 0.0,
            long = viewModel.selectedLocation?.longitude ?: 0.0,
            type = viewModel.answers[2], // Kebutuhan
            buildingType = viewModel.answers[3], // Tipe Bangunan
            area = viewModel.answers[4].substringAfter("Luas Bangunan: ").toIntOrNull() ?: 0,
            numFloor = viewModel.answers[5].substringAfter("Jumlah Lantai: ").toIntOrNull() ?: 0,
            numPerson = viewModel.answers[6].substringAfter("Jumlah Penghuni: ").toIntOrNull() ?: 0,
            numRoom = viewModel.answers[7].substringAfter("Jumlah Kamar: ").toIntOrNull() ?: 0,
            numBathroom = viewModel.answers[8].substringAfter("Jumlah Kamar Mandi: ").toIntOrNull() ?: 0,
            theme = viewModel.answers[9], // Tema
            budget = "251 juta - 500 juta",
            buildingTime = "2024-07-17",
            notes = "buat rumah yang cepettttt"
        )
        val userId = "a67a265a-6f6f-49c0-95c1-f3f19e045a8b"

        val getToken = authManager.getAccess(AuthenticationManager.TOKEN).toString()
        val token = "Bearer $getToken"

        homeViewModel.submitProject(token, userId, projectRequest)
        homeViewModel.projectResponse.observe(viewLifecycleOwner) {
            if (it != null) {
                dismiss()
            }
        }
    }

    private fun updateMapLocation(latLng: LatLng, title: String) {
        map.clear()
        map.addMarker(MarkerOptions().position(latLng).title(title))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        viewModel.selectedLocation = latLng
        viewModel.selectedLocationName = title
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
    }
}
