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
import com.hackathon.devlabsuser.model.Project
import com.hackathon.devlabsuser.ui.authentication.AuthenticationManager
import com.hackathon.devlabsuser.ui.main.home.DetailArticleActivity
import com.hackathon.devlabsuser.viewmodel.ProgressViewModel

class MenungguKonfirmasiFragment : Fragment() {
    private var _binding : FragmentMenungguKonfirmasiBinding? = null
    private val binding get() = _binding!!
    private lateinit var authManager: AuthenticationManager
    private lateinit var progressAdapter: ProgressAdapter
    private lateinit var progressViewModel: ProgressViewModel
    private var projectList: List<Project> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenungguKonfirmasiBinding.inflate(inflater, container, false)
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
                    it.putExtra(DetailProjectActivity.CITY, project.city)
                    it.putExtra(DetailProjectActivity.CREATED_AT, project.created_at)
                    it.putExtra(DetailProjectActivity.NOTES, project.notes)
                    it.putExtra(DetailProjectActivity.BUILDING_TIME, project.buildingtime)
                    it.putExtra(DetailProjectActivity.BUDGET, project.budget)
                    it.putExtra(DetailProjectActivity.THEME, project.theme)
                    it.putExtra(DetailProjectActivity.NUM_FLOOR, project.numfloor)
                    it.putExtra(DetailProjectActivity.NUM_BATHROOM, project.numbathroom)
                    it.putExtra(DetailProjectActivity.NUM_ROOM, project.numroom)
                    it.putExtra(DetailProjectActivity.NUM_PERSON, project.numperson)
                    it.putExtra(DetailProjectActivity.AREA, project.area)
                    it.putExtra(DetailProjectActivity.BUILDING_TYPE, project.buildingtype)
                    it.putExtra(DetailProjectActivity.TYPE, project.type)
                    it.putExtra(DetailProjectActivity.LONG, project.long)
                    it.putExtra(DetailProjectActivity.LAT, project.lat)
                    it.putExtra(DetailProjectActivity.AMOUNT, project.amount)
                    it.putExtra(DetailProjectActivity.TAX, project.tax)
                    it.putExtra(DetailProjectActivity.PRICE, project.price)
                    it.putExtra(DetailProjectActivity.STATUS_TRANSACTION, project.status_transaction)
                    it.putExtra(DetailProjectActivity.TRANSACTION_ID, project.transaction_id)
                    it.putExtra(DetailProjectActivity.STATUS, project.status)
                    it.putExtra(DetailProjectActivity.NAME, project.name)
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

        progressViewModel.getProjectsByUserIdConfirm(token)
        progressViewModel.projectsConfirm.observe(viewLifecycleOwner) {
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