package com.hackathon.devlabsuser.ui.main.home

import android.app.DatePickerDialog
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
import com.hackathon.devlabsuser.ui.architect.ArchitectActivity
import com.hackathon.devlabsuser.ui.authentication.AuthenticationManager
import com.hackathon.devlabsuser.viewmodel.HomeViewModel
import java.util.Calendar

class QuestionnaireDialogFragment : DialogFragment(), OnMapReadyCallback {

    private val viewModel: QuestionnaireViewModel by viewModels({
        if (parentFragment != null) requireParentFragment() else requireActivity()
    })
    private val homeViewModel: HomeViewModel by viewModels({
        if (parentFragment != null) requireParentFragment() else requireActivity()
    })
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
            7 -> R.layout.question_project_name_budget_building_time
            8 -> R.layout.question_notes
            9 -> R.layout.question_summary
            else -> throw IllegalArgumentException("Invalid question index")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getString("architect_id")

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

            7 -> setupQuestion11to13()
            8 -> setupQuestion14()
            9 -> setupSummary()
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
                Log.d("Questionnaire", "After Q1: currentQuestion=${viewModel.currentQuestion}, answers=${viewModel.answers}")
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

    private fun setupQuestion11to13() {
        val questionBinding = binding as QuestionProjectNameBudgetBuildingTimeBinding
        val priceSpinner = questionBinding.priceSpinner
        val tvDate = questionBinding.tvDate
        val projectName = questionBinding.inputProjectName

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                tvDate.text = "Tanggal yang dipilih: $selectedDate"
                viewModel.buildingTime = selectedDate // Inisialisasi nilai buildingTime
            }, year, month, day)

        questionBinding.btnSelectDate.setOnClickListener {
            datePickerDialog.show()
        }

        questionBinding.buttonNext.setOnClickListener {
            val budget = priceSpinner.selectedItem.toString()
            if (budget.isEmpty() || projectName.text.toString().isEmpty() || viewModel.buildingTime == null) {
                Toast.makeText(requireContext(), "Isi semua pertanyaan!", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.projectName = projectName.text.toString()
                viewModel.budget = budget

                saveAnswerAndMoveToNext("next")
            }
        }
    }

    private fun setupQuestion14() {
        val questionBinding = binding as QuestionNotesBinding
        val inputNotes = questionBinding.inputNotes

        questionBinding.buttonNext.setOnClickListener {
            if (inputNotes.text.isEmpty()) {
                Toast.makeText(requireContext(), "Masukkan notes !", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.notes = inputNotes.text.toString()
                saveAnswerAndMoveToNext("Notes ${inputNotes.text.toString()}")
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

        if (viewModel.currentQuestion < 10) {
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
        if (parentFragment != null) { // Jika dipanggil dari Fragment
            (parentFragment as? HomeFragment)?.openQuestionnaire(viewModel.currentQuestion)
        } else { // Jika dipanggil dari Activity
            (activity as? ArchitectActivity)?.openQuestionnaire(viewModel.currentQuestion)
        }
    }

    private fun submitAnswers() {
        try {
            val answers = viewModel.answers

            // Pastikan list `answers` memiliki semua jawaban yang diperlukan
            if (answers.size < 9) { // Check if there are 10 answers
                Log.e("QuestionnaireDialogFragment", "Insufficient number of answers. 8 answers are required, but only ${answers.size} were provided.")
                Toast.makeText(context, "Make sure all questions have been answered.", Toast.LENGTH_SHORT).show()
                return
            }

            // Membuat ProjectRequest sesuai dengan format yang diperlukan oleh API
            val projectRequest = ProjectRequest(
                name = answers[0], // Jenis Layanan
                lat = viewModel.selectedLocation?.latitude ?: 0.0,
                long = viewModel.selectedLocation?.longitude ?: 0.0,
                type = answers[2], // Jenis Proyek
                buildingType = answers[3], // Tipe Bangunan
                area = answers[4].substringBefore(",").toIntOrNull() ?: 0, // Luas Bangunan
                numPerson = answers[5].substringBefore(",").toIntOrNull() ?: 0, // Jumlah Penghuni
                numRoom = answers[6].substringAfter("Jumlah Kamar: ").toIntOrNull() ?: 0,
                numBathroom = answers[6].substringAfter("Jumlah Kamar Mandi: ").toIntOrNull() ?: 0,
                numFloor = answers[4].substringAfter("Jumlah Lantai: ").toIntOrNull() ?: 0,
                theme = answers[6].substringBefore(",").substringAfter("Tema: "), // Tema (ambil dari awal hingga koma)
                projectName = viewModel.projectName!!,
                budget = viewModel.budget!!,
                buildingTime = viewModel.buildingTime!!,
                notes = viewModel.notes!!
            )



            // Mendapatkan token untuk autentikasi
            val getToken = authManager.getAccess(AuthenticationManager.TOKEN).toString()
            val token = "Bearer $getToken"

            var id = viewModel.architectId

            // Mengirim permintaan untuk membuat proyek baru
            homeViewModel.submitProject(token, id!!, projectRequest)
            homeViewModel.projectResponse.observe(viewLifecycleOwner) {
                if (it != null) {
                    dismiss() // Menutup dialog setelah pengiriman berhasil
                }
            }

        } catch (e: IndexOutOfBoundsException) {
            Log.e("QuestionnaireDialogFragment", "Terjadi kesalahan: ${e.message}")
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
