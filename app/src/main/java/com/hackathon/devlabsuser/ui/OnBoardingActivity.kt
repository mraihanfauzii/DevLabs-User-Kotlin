package com.hackathon.devlabsuser.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hackathon.devlabsuser.R
import com.hackathon.devlabsuser.databinding.ActivityOnBoardingBinding
import com.hackathon.devlabsuser.ui.authentication.LoginActivity

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            val login = Intent(this@OnBoardingActivity, LoginActivity::class.java)
            startActivity(login)
            finish()
        }
    }
}