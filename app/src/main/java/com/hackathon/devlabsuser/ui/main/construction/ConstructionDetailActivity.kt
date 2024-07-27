package com.hackathon.devlabsuser.ui.main.construction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hackathon.devlabsuser.databinding.ActivityConstructionDetailBinding

class ConstructionDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConstructionDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConstructionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}