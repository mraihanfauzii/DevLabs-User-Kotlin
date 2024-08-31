package com.hackathon.devlabsuser.ui.architect

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hackathon.devlabsuser.adapter.architect.PortfolioAdapter
import com.hackathon.devlabsuser.databinding.FragmentArchitectPortfolioBinding
import com.hackathon.devlabsuser.model.Portfolio
import com.hackathon.devlabsuser.ui.authentication.AuthenticationManager
import com.hackathon.devlabsuser.viewmodel.ArchitectViewModel

class ArchitectPortfolioFragment : Fragment() {
    private var _binding: FragmentArchitectPortfolioBinding? = null
    private val binding get() = _binding!!
    private val portfolioViewModel: ArchitectViewModel by viewModels()
    private lateinit var portfolioAdapter: PortfolioAdapter
    private lateinit var authenticationManager: AuthenticationManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArchitectPortfolioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authenticationManager = AuthenticationManager(requireContext())

        portfolioAdapter = PortfolioAdapter(emptyList())
        portfolioAdapter.setOnItemClickCallback(object: PortfolioAdapter.OnItemClickCallback {
            override fun onItemClicked(portfolio: Portfolio) {
                Intent(context, DetailPortfolioActivity::class.java).also {
                    it.putExtra(DetailPortfolioActivity.PORTFOLIO_ATTACHMENTS, "https://www.bumpy-insects-reply-yearly.a276.dcdg.xyz" + portfolio.attachments?.firstOrNull()?.path)
                    it.putExtra(DetailPortfolioActivity.PORTFOLIO_NAME, portfolio.name)
                    it.putExtra(DetailPortfolioActivity.PORTFOLIO_DESCRIPTION, portfolio.description)
                    it.putExtra(DetailPortfolioActivity.PORTFOLIO_ID, portfolio.id)
                    startActivity(it)
                }
            }
        })

        binding.rvPortfolios.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPortfolios.adapter = portfolioAdapter

        portfolioViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            showLoading(isLoading)
        })

        portfolioViewModel.getPortfolio.observe(viewLifecycleOwner, Observer { portfolios ->
            portfolioAdapter.updatePortfolios(portfolios.data)
        })

        portfolioViewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        arguments?.getString("architect_id")?.let { architectId ->
            val getToken = authenticationManager.getAccess(AuthenticationManager.TOKEN).toString()
            val token = "Bearer $getToken"
            portfolioViewModel.getPortfolio(token, architectId)
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    companion object {
        fun newInstance(architectId: String): ArchitectPortfolioFragment {
            val fragment = ArchitectPortfolioFragment()
            val args = Bundle().apply {
                putString("architect_id", architectId)
            }
            fragment.arguments = args
            return fragment
        }
    }
}