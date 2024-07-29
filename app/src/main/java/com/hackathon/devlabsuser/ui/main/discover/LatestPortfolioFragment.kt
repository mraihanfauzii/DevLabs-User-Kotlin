package com.hackathon.devlabsuser.ui.main.discover

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hackathon.devlabsuser.adapter.discover.LatestPortfoliosAdapter
import com.hackathon.devlabsuser.adapter.home.PromoAdapter
import com.hackathon.devlabsuser.databinding.FragmentLatestPortfolioBinding
import com.hackathon.devlabsuser.ui.authentication.AuthenticationManager
import com.hackathon.devlabsuser.viewmodel.DiscoverViewModel

class LatestPortfolioFragment : Fragment() {
    private var _binding : FragmentLatestPortfolioBinding? = null
    private val binding get() = _binding!!
    private lateinit var authManager: AuthenticationManager
    private lateinit var latestPortfoliosAdapter: LatestPortfoliosAdapter
    private lateinit var discoverViewModel: DiscoverViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLatestPortfolioBinding.inflate(inflater, container, false)
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
        }

        binding.rvLatestPortfolio.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = latestPortfoliosAdapter
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}