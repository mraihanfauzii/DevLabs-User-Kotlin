package com.hackathon.devlabsuser.ui.main.discover

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.hackathon.devlabsuser.adapter.discover.DiscoverFragmentAdapter
import com.hackathon.devlabsuser.databinding.FragmentDiscoverBinding

class DiscoverFragment : Fragment() {
    private var _binding : FragmentDiscoverBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewPagerAdapter: DiscoverFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
                    0 -> tab.text = "Rekomendasi"
                    1 -> tab.text = "Terbaru"
                    2 -> tab.text = "Trending"
                }
            }.attach()
        }
    }
}