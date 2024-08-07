package com.hackathon.devlabsuser.ui.main.discover

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
import com.hackathon.devlabsuser.adapter.discover.LatestPortfoliosAdapter
import com.hackathon.devlabsuser.databinding.FragmentAllPortfolioBinding
import com.hackathon.devlabsuser.model.Portfolio
import com.hackathon.devlabsuser.ui.authentication.AuthenticationManager
import com.hackathon.devlabsuser.viewmodel.DiscoverViewModel

class AllPortfolioFragment : Fragment() {
    private var _binding : FragmentAllPortfolioBinding? = null
    private val binding get() = _binding!!
    private lateinit var authManager: AuthenticationManager
    private lateinit var latestPortfoliosAdapter: LatestPortfoliosAdapter
    private lateinit var discoverViewModel: DiscoverViewModel
    private var portfolioList: List<Portfolio> = emptyList()
    private var isAscending = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllPortfolioBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        latestPortfoliosAdapter = LatestPortfoliosAdapter()
        latestPortfoliosAdapter.notifyDataSetChanged()

        authManager = AuthenticationManager(requireContext())
        val getToken = authManager.getAccess(AuthenticationManager.TOKEN).toString()
        val token = "Bearer $getToken"

        discoverViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[DiscoverViewModel::class.java]
        discoverViewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            showLoading(loading)
        }
        discoverViewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        discoverViewModel.getRecentPortfolios(token)
        discoverViewModel.getRecentPortfolios.observe(viewLifecycleOwner) {
            if (it != null) {
                latestPortfoliosAdapter.getLatestPortfolios(it)
            }
            portfolioList = it
        }

        binding.apply {
            searchInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    filter(s.toString())
                }
                override fun afterTextChanged(s: Editable?) {}
            })
            btnSort.setOnClickListener {
                isAscending = !isAscending
                sortPortfolios(isAscending)
            }
            rvLatestPortfolio.apply {
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                adapter = latestPortfoliosAdapter
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun sortPortfolios(isAscending: Boolean) {
        val sortedList = if (isAscending) {
            portfolioList.sortedBy { it.name }
        } else {
            portfolioList.sortedByDescending { it.name }
        }
        latestPortfoliosAdapter.getLatestPortfolios(sortedList)
    }

    private fun filter(text: String) {
        val filteredList = portfolioList.filter { it.name.contains(text, ignoreCase = true) }
        latestPortfoliosAdapter.getLatestPortfolios(filteredList)
    }
}