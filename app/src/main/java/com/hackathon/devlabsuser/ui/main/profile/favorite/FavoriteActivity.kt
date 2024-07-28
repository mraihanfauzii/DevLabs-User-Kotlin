package com.hackathon.devlabsuser.ui.main.profile.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.hackathon.devlabsuser.R
import com.hackathon.devlabsuser.adapter.favorite.FavoriteFragmentAdapter
import com.hackathon.devlabsuser.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var viewPagerAdapter: FavoriteFragmentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = Bundle()

        viewPagerAdapter = FavoriteFragmentAdapter(supportFragmentManager, lifecycle, bundle)
        with(binding) {
            ViewPager.adapter = viewPagerAdapter
            TabLayoutMediator(TabLayout, ViewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = "Portfolio"
                    1 -> tab.text = "Architect"
                    2 -> tab.text = "Article"
                }
            }.attach()
        }
        // Check if there's an intent to navigate to a specific fragment
        val navigateTo = intent.getStringExtra("navigate_to")
        if (navigateTo == "FavoriteArticleFragment") {
            binding.ViewPager.setCurrentItem(2, true)
        }
    }
}