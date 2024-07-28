package com.hackathon.devlabsuser.adapter.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hackathon.devlabsuser.ui.main.profile.favorite.FavoriteArchitectFragment
import com.hackathon.devlabsuser.ui.main.profile.favorite.FavoriteArticleFragment
import com.hackathon.devlabsuser.ui.main.profile.favorite.FavoritePortfolioFragment

class FavoriteFragmentAdapter (fm: FragmentManager, lifecycle: Lifecycle, data: Bundle) :
    FragmentStateAdapter(fm, lifecycle) {
    private var fragmentBundle: Bundle

    init {
        fragmentBundle = data
    }

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        var fragment = Fragment()
        when (position) {
            0 -> fragment = FavoritePortfolioFragment()
            1 -> fragment = FavoriteArchitectFragment()
            2 -> fragment = FavoriteArticleFragment()
        }
        fragment.arguments = this.fragmentBundle
        return fragment
    }
}