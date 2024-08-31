package com.hackathon.devlabsuser.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.hackathon.devlabsuser.BR
import com.hackathon.devlabsuser.R
import com.hackathon.devlabsuser.databinding.QuestionArchitectRecBinding
import com.hackathon.devlabsuser.model.RecommendedArchitectsRequest
import com.hackathon.devlabsuser.ui.architect.ArchitectRecActivity
import com.hackathon.devlabsuser.ui.authentication.AuthenticationManager
import com.hackathon.devlabsuser.viewmodel.HomeViewModel
import com.hackathon.devlabsuser.viewmodel.QuestionnaireViewModel

class QuestionnaireRecomDialogFragment : DialogFragment() {

    private val viewModel: QuestionnaireViewModel by viewModels({
        if (parentFragment != null) requireParentFragment() else requireActivity()
    })
    private val homeViewModel: HomeViewModel by viewModels({
        if (parentFragment != null) requireParentFragment() else requireActivity()
    })
    private lateinit var authManager: AuthenticationManager
    private lateinit var binding: QuestionArchitectRecBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = QuestionArchitectRecBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        setupQuestion1()
    }

    private fun setupQuestion1() {
        val questionBinding = binding as QuestionArchitectRecBinding // Ganti dengan binding yang sesuai
        val areaInput = questionBinding.inputProjectArea
        val cityInput = questionBinding.inputProjectCity
        val themeSpinner = questionBinding.priceSpinner
        val budgetSpinner = questionBinding.priceSpinnerr

        questionBinding.buttonNext.setOnClickListener {
            val area = areaInput.text.toString().toIntOrNull()
            val city = cityInput.text.toString()
            val theme = themeSpinner.selectedItem.toString()
            val budget = budgetSpinner.selectedItem.toString()

            if (area == null || city.isEmpty() || theme.isEmpty() || budget.isEmpty()) {
                Toast.makeText(requireContext(), "Isi semua pertanyaan!", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.area = area
                viewModel.city = city
                viewModel.theme = theme
                viewModel.budget = budget
                submitAnswers()
            }
        }
    }

    private fun submitAnswers() {
        try {
            val request = RecommendedArchitectsRequest(
                area = viewModel.area!!,
                city = viewModel.city!!,
                theme = viewModel.theme!!,
                budget = viewModel.budget!!
            )

            val getToken = authManager.getAccess(AuthenticationManager.TOKEN).toString()
            val token = "Bearer $getToken"

            homeViewModel.getRecommendedArchitects(token, request)

            homeViewModel.recArchitectResponse.observe(viewLifecycleOwner) { architects ->
                if (architects != null) {
                    val intent = Intent(context, ArchitectRecActivity::class.java)
                    intent.putParcelableArrayListExtra("architects", ArrayList(architects))
                    startActivity(intent)
                    dismiss()
                }
            }

        } catch (e: Exception) {
            Log.e("QuestionnaireDialogFragment", "Terjadi kesalahan: ${e.message}")
            // Tangani kesalahan dengan menampilkan pesan error atau tindakan lainnya
        }
    }
}