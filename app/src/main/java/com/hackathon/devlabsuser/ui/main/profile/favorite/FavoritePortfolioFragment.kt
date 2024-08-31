package com.hackathon.devlabsuser.ui.main.profile.favorite

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hackathon.devlabsuser.adapter.favorite.FavoritePortfolioAdapter
import com.hackathon.devlabsuser.databinding.FragmentFavoritePortfolioBinding
import com.hackathon.devlabsuser.model.Portfolio
import com.hackathon.devlabsuser.ui.architect.DetailPortfolioActivity
import com.hackathon.devlabsuser.viewmodel.FavoriteViewModel

class FavoritePortfolioFragment : Fragment() {
    private var _binding : FragmentFavoritePortfolioBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoritePortfolioAdapter: FavoritePortfolioAdapter
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private var portfolioList: List<Portfolio> = emptyList()
    private var isAscending = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritePortfolioBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritePortfolioAdapter = FavoritePortfolioAdapter()
        favoriteViewModel.allPortfolioFavorites.observe(viewLifecycleOwner) { portfolios ->
            portfolioList = portfolios
            favoritePortfolioAdapter.getPortfolios(portfolios)
        }
        binding.apply {
            rvArticle.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = favoritePortfolioAdapter
            }
            btnSort.setOnClickListener {
                isAscending = !isAscending
                sortPortfolios(isAscending)
            }
            searchInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    filter(s.toString())
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }
        favoritePortfolioAdapter.setOnItemClickCallback(object: FavoritePortfolioAdapter.OnItemClickCallback {
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
    }

    private fun sortPortfolios(isAscending: Boolean) {
        val sortedList = if (isAscending) {
            portfolioList.sortedBy { it.name }
        } else {
            portfolioList.sortedByDescending { it.name }
        }
        favoritePortfolioAdapter.getPortfolios(sortedList)
    }

    private fun filter(text: String) {
        val filteredList = portfolioList.filter { it.name.contains(text, ignoreCase = true) }
        favoritePortfolioAdapter.getPortfolios(filteredList)
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}