package com.hackathon.devlabsuser.adapter.architect

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hackathon.devlabsuser.ui.architect.ArchitectPortfolioFragment
import com.hackathon.devlabsuser.ui.architect.ArchitectReviewFragment

class ArchitectFragmentAdapter (fm: FragmentManager, lifecycle: Lifecycle, data: Bundle) :
    FragmentStateAdapter(fm, lifecycle) {
    private var fragmentBundle: Bundle

    init {
        fragmentBundle = data
    }

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment = Fragment()
        when (position) {
            0 -> fragment = ArchitectPortfolioFragment()
            1 -> fragment = ArchitectReviewFragment()
        }
        fragment.arguments = this.fragmentBundle
        return fragment
    }
}