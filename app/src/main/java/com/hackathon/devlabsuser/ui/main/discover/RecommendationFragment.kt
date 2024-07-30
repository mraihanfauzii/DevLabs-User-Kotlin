package com.hackathon.devlabsuser.ui.main.discover

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hackathon.devlabsuser.adapter.discover.AllArchitectAdapter
import com.hackathon.devlabsuser.adapter.discover.ThemesAdapter
import com.hackathon.devlabsuser.adapter.discover.TrendingPortfoliosAdapter
import com.hackathon.devlabsuser.databinding.FragmentRecommendationBinding
import com.hackathon.devlabsuser.model.UserData
import com.hackathon.devlabsuser.ui.architect.ArchitectActivity
import com.hackathon.devlabsuser.ui.authentication.AuthenticationManager
import com.hackathon.devlabsuser.viewmodel.DiscoverViewModel

class RecommendationFragment : Fragment() {
    private var _binding : FragmentRecommendationBinding? = null
    private val binding get() = _binding!!
    private lateinit var authManager: AuthenticationManager
    private lateinit var trendingPortfoliosAdapter: TrendingPortfoliosAdapter
    private lateinit var architectAdapter: AllArchitectAdapter
    private lateinit var themeAdapter: ThemesAdapter
    private lateinit var discoverViewModel: DiscoverViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecommendationBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        themeAdapter = ThemesAdapter()
        themeAdapter.notifyDataSetChanged()

        architectAdapter = AllArchitectAdapter(object: AllArchitectAdapter.OnItemClickCallback {
            override fun onItemClicked(architect: UserData) {
                val intent = Intent(context, ArchitectActivity::class.java)
                intent.putExtra("id", architect.id)
                intent.putExtra("profile_picture", architect.profilePicture)
                intent.putExtra("profile_name", architect.profileName)
                intent.putExtra("email", architect.email)
                intent.putExtra("phone_number", architect.phoneNumber)
                intent.putExtra("profile_description", architect.profileDescription)
                intent.putExtra("role", architect.role)
                startActivity(intent)
            }
        })

        trendingPortfoliosAdapter = TrendingPortfoliosAdapter()
        trendingPortfoliosAdapter.notifyDataSetChanged()

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

        discoverViewModel.getAllThemes(token)
        discoverViewModel.getAllThemes.observe(viewLifecycleOwner) {
            if (it != null) {
                themeAdapter.getThemes(it)
            }
        }

        discoverViewModel.getAllArchitect(token)
        discoverViewModel.getAllArchitect.observe(viewLifecycleOwner) {
            if (it != null) {
                architectAdapter.getArchitects(it)
            }
        }

        discoverViewModel.getTrendingPortfolios(token)
        discoverViewModel.getTrendingPortfolios.observe(viewLifecycleOwner) {
            if (it != null) {
                trendingPortfoliosAdapter.getTrendingPortfolios(it)
            }
        }

        binding.rvTema.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = themeAdapter
        }
        binding.rvArsitek.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = architectAdapter
        }
        binding.rvTrendingPortofolio.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = trendingPortfoliosAdapter
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}