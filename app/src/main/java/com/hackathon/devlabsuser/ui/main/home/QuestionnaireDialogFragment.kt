package com.hackathon.devlabsuser.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.hackathon.devlabsuser.R
import com.hackathon.devlabsuser.databinding.*
import com.hackathon.devlabsuser.viewmodel.QuestionnaireViewModel
import kotlinx.coroutines.launch

class QuestionnaireDialogFragment : DialogFragment() {

    private val viewModel: QuestionnaireViewModel by viewModels()
    private lateinit var binding: ViewDataBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false)
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
        bindViews()
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
            // Assume we get the location from some map widget
            val location = "Sample Location"
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
}
