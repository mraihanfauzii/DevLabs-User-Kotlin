package com.hackathon.devlabsuser.ui.architect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.hackathon.devlabsuser.databinding.ActivityArchitectRecBinding
import com.hackathon.devlabsuser.model.RecArchitect

class ArchitectRecActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArchitectRecBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArchitectRecBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val architects = intent.getParcelableArrayListExtra<RecArchitect>("architects")

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = ArchitectAdapter(architects ?: emptyList())
    }
}