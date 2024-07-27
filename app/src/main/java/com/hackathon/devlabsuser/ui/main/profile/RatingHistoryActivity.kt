package com.hackathon.devlabsuser.ui.main.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hackathon.devlabsuser.databinding.ActivityRatingHistoryBinding

class RatingHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRatingHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRatingHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}