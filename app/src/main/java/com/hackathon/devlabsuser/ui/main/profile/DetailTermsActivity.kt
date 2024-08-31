package com.hackathon.devlabsuser.ui.main.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hackathon.devlabsuser.R
import com.hackathon.devlabsuser.databinding.ActivityDetailTermsBinding

class DetailTermsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailTermsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTermsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}