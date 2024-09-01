package com.hackathon.devlabsuser.ui.main.construction

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hackathon.devlabsuser.adapter.progress.ProgressAdapter
import com.hackathon.devlabsuser.databinding.FragmentMenungguKonfirmasiBinding
import com.hackathon.devlabsuser.databinding.FragmentSedangDikerjakanBinding
import com.hackathon.devlabsuser.model.Portfolio
import com.hackathon.devlabsuser.model.Project
import com.hackathon.devlabsuser.ui.architect.DetailPortfolioActivity
import com.hackathon.devlabsuser.ui.authentication.AuthenticationManager
import com.hackathon.devlabsuser.viewmodel.DiscoverViewModel
import com.hackathon.devlabsuser.viewmodel.ProgressViewModel

class SedangDikerjakanFragment : Fragment() {
    private var _binding : FragmentSedangDikerjakanBinding? = null
    private val binding get() = _binding!!
    private lateinit var authManager: AuthenticationManager
    private lateinit var progressAdapter: ProgressAdapter
    private lateinit var progressViewModel: ProgressViewModel
    private var projectList: List<Project> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSedangDikerjakanBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressAdapter = ProgressAdapter()
        progressAdapter.notifyDataSetChanged()
        progressAdapter.setOnItemClickCallback(object: ProgressAdapter.OnItemClickCallback {
            override fun onItemClicked(project: Project) {
                Intent(context, DetailProjectActivity::class.java).also {
                    it.putExtra(DetailProjectActivity.ID, project.id)
                    startActivity(it)
                }
            }
        })

        authManager = AuthenticationManager(requireContext())
        val getToken = authManager.getAccess(AuthenticationManager.TOKEN).toString()
        val token = "Bearer $getToken"

        progressViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[ProgressViewModel::class.java]
        progressViewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            showLoading(loading)
        }
        progressViewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        progressViewModel.getProjectsByUserIdProgress(token)
        progressViewModel.projectsProgress.observe(viewLifecycleOwner) {
            if (it != null) {
                progressAdapter.getLatestPortfolios(it)
            }
            projectList = it
        }

        binding.apply {
            searchInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    filter(s.toString())
                }
                override fun afterTextChanged(s: Editable?) {}
            })
            rvLatestPortfolio.apply {
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                adapter = progressAdapter
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun filter(text: String) {
        val filteredList = projectList.filter { it.name.contains(text, ignoreCase = true) }
        progressAdapter.getLatestPortfolios(filteredList)
    }
}