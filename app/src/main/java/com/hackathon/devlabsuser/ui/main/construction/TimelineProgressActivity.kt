package com.hackathon.devlabsuser.ui.main.construction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hackathon.devlabsuser.databinding.ActivityTimelineProgressBinding

class TimelineProgressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTimelineProgressBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimelineProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    companion object {
        const val ID = "id"
    }
}