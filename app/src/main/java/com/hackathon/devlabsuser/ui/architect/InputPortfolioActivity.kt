
package com.hackathon.devlabsuser.ui.architect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hackathon.devlabsuser.databinding.ActivityInputPortfolioBinding

class InputPortfolioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputPortfolioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputPortfolioBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}