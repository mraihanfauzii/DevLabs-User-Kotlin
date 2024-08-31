package com.hackathon.devlabsuser.ui.architect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.hackathon.devlabsuser.R
import com.hackathon.devlabsuser.databinding.ActivityDetailPortfolioBinding
import com.hackathon.devlabsuser.model.Architect
import com.hackathon.devlabsuser.model.Attachments
import com.hackathon.devlabsuser.model.Portfolio
import com.hackathon.devlabsuser.model.PortfolioTheme
import com.hackathon.devlabsuser.viewmodel.FavoriteViewModel

class DetailPortfolioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPortfolioBinding
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPortfolioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val createdAt = intent.getStringExtra(PORTFOLIO_CREATED_AT)
        val name = intent.getStringExtra(PORTFOLIO_NAME)
        val description = intent.getStringExtra(PORTFOLIO_DESCRIPTION)
        val themeName = intent.getStringExtra(PORTFOLIO_THEME_NAME)
        val themeId = intent.getStringExtra(PORTFOLIO_THEME_ID)
        val themeImage = intent.getStringExtra(PORTFOLIO_THEME_IMAGE)
        val id = intent.getStringExtra(PORTFOLIO_ID)
        val architectId = intent.getStringExtra(PORTFOLIO_ARCHITECT_ID)
        val architectName = intent.getStringExtra(PORTFOLIO_ARCHITECT_NAME)
        val architectPicture = intent.getStringExtra(PORTFOLIO_ARCHITECT_PICTURE)
        val clickCount = intent.getStringExtra(PORTFOLIO_CLICK_COUNT)
        val estimatedBudget = intent.getIntExtra(PORTFOLIO_ESTIMATED_BUDGET, 0)
        val attachments = intent.getParcelableArrayListExtra<Attachments>(PORTFOLIO_ATTACHMENTS)

        val architect = Architect(architectId ?: "", architectName ?: "", architectPicture ?: "")
        val theme = PortfolioTheme(themeId ?: "", themeName ?: "", themeImage ?: "")
        val portfolio = Portfolio(id ?: "", architect, theme, name ?: "", description ?: "", estimatedBudget, createdAt ?: "", clickCount ?: "", attachments ?: emptyList())

        binding.apply {
            Glide.with(this@DetailPortfolioActivity)
                .load(themeImage)
                .centerCrop()
                .into(ivPhotoUrl)
            tvArticleTitle.text = name
            tvClickCount.text = clickCount
            tvStoryDescription.text = description
            tvCreatedAt.text = createdAt
            tvTheme.text = themeName

            favoriteViewModel.allPortfolioFavorites.observe(this@DetailPortfolioActivity) { favorites ->
                val isFavorited = favorites.any { it.id == id }
                toggleFavorite.isChecked = isFavorited
                toggleFavorite.setBackgroundResource(if (isFavorited) R.drawable.ic_favorite_after else R.drawable.ic_favorite_before)
            }

            toggleFavorite.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    toggleFavorite.setBackgroundResource(R.drawable.ic_favorite_after)
                    favoriteViewModel.insertPortfolioFavorite(portfolio)
                } else {
                    toggleFavorite.setBackgroundResource(R.drawable.ic_favorite_before)
                    favoriteViewModel.deletePortfolioFavorite(portfolio)
                }
            }
        }
    }

    companion object {
        const val PORTFOLIO_CREATED_AT = "portfolio_created_at"
        const val PORTFOLIO_NAME = "portfolio_name"
        const val PORTFOLIO_DESCRIPTION = "portfolio_description"
        const val PORTFOLIO_THEME_ID = "portfolio_theme_id"
        const val PORTFOLIO_THEME_NAME = "portfolio_theme_name"
        const val PORTFOLIO_THEME_IMAGE = "portfolio_theme_image"
        const val PORTFOLIO_ID = "portfolio_id"
        const val PORTFOLIO_ARCHITECT_ID = "portfolio_architect_id"
        const val PORTFOLIO_ARCHITECT_NAME = "portfolio_architect_name"
        const val PORTFOLIO_ARCHITECT_PICTURE = "portfolio_architect_picture"
        const val PORTFOLIO_CLICK_COUNT = "portfolio_click_count"
        const val PORTFOLIO_ESTIMATED_BUDGET = "portfolio_estimated_budget"
        const val PORTFOLIO_ATTACHMENTS = "portfolio_attachments"
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}