package com.hackathon.devlabsuser.ui.main.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hackathon.devlabsuser.R
import com.hackathon.devlabsuser.databinding.ActivityDetailHelpCenterBinding

class DetailHelpCenterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailHelpCenterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHelpCenterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}