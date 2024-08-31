package com.hackathon.devlabsuser.ui.architect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.hackathon.devlabsuser.databinding.ActivityArchitectRecBinding
import com.hackathon.devlabsuser.model.RecArchitect

class ArchitectRecActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArchitectRecBinding
    private lateinit var architectAdapter: ArchitectRecAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArchitectRecBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val architects = intent.getParcelableArrayListExtra<RecArchitect>("architects") ?: arrayListOf()

        architectAdapter = ArchitectRecAdapter(architects, object: ArchitectRecAdapter.OnItemClickCallbackRecArchitect {
            override fun onItemClicked(architect: RecArchitect) {
                Intent(this@ArchitectRecActivity, ArchitectActivity::class.java).apply {
                    putExtra(ArchitectActivity.ID, architect.id)
                    putExtra(ArchitectActivity.NAME, architect.profileName)
                    putExtra(ArchitectActivity.DESCRIPTION, architect.profileDescription)
                    putExtra(ArchitectActivity.PHOTO_URL, architect.profilePicture)
                    putExtra(ArchitectActivity.PHONE_NUMBER, architect.phoneNumber)
                    putExtra(ArchitectActivity.EMAIL, architect.email)
                    putExtra(ArchitectActivity.ROLE, architect.role)
                    startActivity(this)
                }
            }
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = architectAdapter
    }
}