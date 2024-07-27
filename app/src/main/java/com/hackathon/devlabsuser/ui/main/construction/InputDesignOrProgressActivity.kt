package com.hackathon.devlabsuser.ui.main.construction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hackathon.devlabsuser.databinding.ActivityInputDesignOrProgressBinding

class InputDesignOrProgressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputDesignOrProgressBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputDesignOrProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}