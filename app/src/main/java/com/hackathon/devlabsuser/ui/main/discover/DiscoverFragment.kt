package com.hackathon.devlabsuser.ui.main.discover

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.hackathon.devlabsuser.adapter.discover.DiscoverFragmentAdapter
import com.hackathon.devlabsuser.databinding.FragmentDiscoverBinding

class DiscoverFragment : Fragment() {
    private var _binding : FragmentDiscoverBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewPagerAdapter: DiscoverFragmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = Bundle()
        viewPagerAdapter = DiscoverFragmentAdapter(childFragmentManager, lifecycle, bundle)
        with(binding) {
            ViewPager.adapter = viewPagerAdapter
            TabLayoutMediator(TabLayout, ViewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = "Trending"
                    1 -> tab.text = "Portofolio"
                    2 -> tab.text = "Arsitek"
                    3 -> tab.text = "Kontraktor"
                }
            }.attach()
        }
        disableSwipeInViewPager(binding.ViewPager)
    }
    private fun disableSwipeInViewPager(viewPager: ViewPager2) {
        viewPager.isUserInputEnabled = false // Disable swipe for ViewPager2
    }
}