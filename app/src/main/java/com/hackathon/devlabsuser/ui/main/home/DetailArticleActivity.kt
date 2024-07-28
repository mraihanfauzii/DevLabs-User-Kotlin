package com.hackathon.devlabsuser.ui.main.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.hackathon.devlabsuser.databinding.ActivityDetailArticleBinding

class DetailArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailArticleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra(TITLE)
        val photoUrl = intent.getStringExtra(PHOTO_URL)
        val description = intent.getStringExtra(DESCRIPTION)

        binding.apply {
            Glide.with(this@DetailArticleActivity)
                .load(photoUrl)
                .centerCrop()
                .into(ivPhotoUrl)
            tvArticleTitle.text = title
            tvStoryDescription.text = description
        }
    }

    companion object {
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val PHOTO_URL = "photo_url"
    }
}