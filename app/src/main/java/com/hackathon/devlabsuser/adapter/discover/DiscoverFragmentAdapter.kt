package com.hackathon.devlabsuser.adapter.discover

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hackathon.devlabsuser.ui.main.discover.AllPortfolioFragment
import com.hackathon.devlabsuser.ui.main.discover.TrendingFragment
import com.hackathon.devlabsuser.ui.main.discover.AllArchitectFragment

class DiscoverFragmentAdapter(fm: FragmentManager, lifecycle: Lifecycle, data: Bundle) :
    FragmentStateAdapter(fm, lifecycle) {
    private var fragmentBundle: Bundle

    init {
        fragmentBundle = data
    }

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        var fragment = Fragment()
        when (position) {
            0 -> fragment = TrendingFragment()
            1 -> fragment = AllPortfolioFragment()
            2 -> fragment = AllArchitectFragment()
        }
        fragment.arguments = this.fragmentBundle
        return fragment
    }
}