package com.hackathon.devlabsuser.ui.architect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.hackathon.devlabsuser.databinding.ActivityDetailPortfolioBinding

class DetailPortfolioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPortfolioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPortfolioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val createdAt = intent.getStringExtra(PORTFOLIO_CREATED_AT)
        val name = intent.getStringExtra(PORTFOLIO_NAME)
        val description = intent.getStringExtra(PORTFOLIO_DESCRIPTION)
        val theme = intent.getStringExtra(PORTFOLIO_THEME)
        val id = intent.getStringExtra(PORTFOLIO_ID)
        val architect = intent.getStringExtra(PORTFOLIO_ARCHITECT)
        val clickCount = intent.getStringExtra(PORTFOLIO_CLICK_COUNT)
        val estimatedBudget = intent.getStringExtra(PORTFOLIO_ESTIMATED_BUDGET)
        val attachments = intent.getStringExtra(PORTFOLIO_ATTACHMENTS)

        binding.apply {
            Glide.with(this@DetailPortfolioActivity)
                .load(attachments)
                .centerCrop()
                .into(ivPhotoUrl)
            tvArticleTitle.text = name
            tvClickCount.text = clickCount
            tvStoryDescription.text = description
            tvCreatedAt.text = createdAt
            tvTheme.text = theme
        }
    }

    companion object {
        const val PORTFOLIO_CREATED_AT = "portfolio_created_at"
        const val PORTFOLIO_NAME = "portfolio_name"
        const val PORTFOLIO_DESCRIPTION = "portfolio_description"
        const val PORTFOLIO_THEME = "portfolio_name"
        const val PORTFOLIO_ID = "portfolio_id"
        const val PORTFOLIO_ARCHITECT = "portfolio_architect"
        const val PORTFOLIO_CLICK_COUNT = "portfolio_click_count"
        const val PORTFOLIO_ESTIMATED_BUDGET = "portfolio_estimated_budget"
        const val PORTFOLIO_ATTACHMENTS = "portfolio_attachments"
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}