package com.hackathon.devlabsuser.ui.architect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.hackathon.devlabsuser.databinding.ActivityDetailPortfolioBinding

class DetailPortfolioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPortfolioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPortfolioBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}