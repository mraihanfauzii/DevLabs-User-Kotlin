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
import com.hackathon.devlabsuser.adapter.discover.themes.ThemesAdapter
import com.hackathon.devlabsuser.adapter.discover.TrendingPortfoliosAdapter
import com.hackathon.devlabsuser.adapter.home.PromoAdapter
import com.hackathon.devlabsuser.databinding.FragmentTrendingBinding
import com.hackathon.devlabsuser.model.Portfolio
import com.hackathon.devlabsuser.model.Promo
import com.hackathon.devlabsuser.model.UserData
import com.hackathon.devlabsuser.ui.architect.ArchitectActivity
import com.hackathon.devlabsuser.ui.architect.DetailPortfolioActivity
import com.hackathon.devlabsuser.ui.authentication.AuthenticationManager
import com.hackathon.devlabsuser.ui.main.home.DetailPromoActivity
import com.hackathon.devlabsuser.viewmodel.DiscoverViewModel

class TrendingFragment : Fragment() {
    private var _binding : FragmentTrendingBinding? = null
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
        _binding = FragmentTrendingBinding.inflate(inflater, container, false)
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
                intent.putExtra(ArchitectActivity.ID, architect.id)
                intent.putExtra(ArchitectActivity.PHOTO_URL, architect.profilePicture)
                intent.putExtra(ArchitectActivity.NAME, architect.profileName)
                intent.putExtra(ArchitectActivity.EMAIL, architect.email)
                intent.putExtra(ArchitectActivity.PHONE_NUMBER, architect.phoneNumber)
                intent.putExtra(ArchitectActivity.DESCRIPTION, architect.profileDescription)
                intent.putExtra(ArchitectActivity.ROLE, architect.role)
                startActivity(intent)
            }
        })

        trendingPortfoliosAdapter = TrendingPortfoliosAdapter()
        trendingPortfoliosAdapter.notifyDataSetChanged()
        trendingPortfoliosAdapter.setOnItemClickCallback(object: TrendingPortfoliosAdapter.OnItemClickCallback {
            override fun onItemClicked(portfolio: Portfolio) {
                val attachmentsList = portfolio.attachments ?: emptyList()
                Intent(context, DetailPortfolioActivity::class.java).apply {
                    putExtra(DetailPortfolioActivity.PORTFOLIO_CREATED_AT, portfolio.createdAt)
                    putExtra(DetailPortfolioActivity.PORTFOLIO_NAME, portfolio.name)
                    putExtra(DetailPortfolioActivity.PORTFOLIO_DESCRIPTION, portfolio.description)
                    putExtra(DetailPortfolioActivity.PORTFOLIO_THEME_NAME, portfolio.theme?.name)
                    putExtra(DetailPortfolioActivity.PORTFOLIO_THEME_ID, portfolio.theme?.id)
                    putExtra(DetailPortfolioActivity.PORTFOLIO_THEME_IMAGE, portfolio.theme?.image)
                    putExtra(DetailPortfolioActivity.PORTFOLIO_ID, portfolio.id)
                    putExtra(DetailPortfolioActivity.PORTFOLIO_ARCHITECT_ID, portfolio.architect.id)
                    putExtra(DetailPortfolioActivity.PORTFOLIO_ARCHITECT_NAME, portfolio.architect.name)
                    putExtra(DetailPortfolioActivity.PORTFOLIO_ARCHITECT_PICTURE, portfolio.architect.picture)
                    putExtra(DetailPortfolioActivity.PORTFOLIO_CLICK_COUNT, portfolio.clickCount)
                    putExtra(DetailPortfolioActivity.PORTFOLIO_ESTIMATED_BUDGET, portfolio.estimatedBudget)
                    putParcelableArrayListExtra(DetailPortfolioActivity.PORTFOLIO_ATTACHMENTS, ArrayList(attachmentsList))
                    startActivity(this)
                }
            }
        })

        authManager = AuthenticationManager( requireContext())
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