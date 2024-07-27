package com.hackathon.devlabsuser.ui.main.construction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.hackathon.devlabsuser.adapter.progress.ConstructionFragmentAdapter
import com.hackathon.devlabsuser.databinding.FragmentConstructionBinding

class ConstructionFragment : Fragment() {
    private var _binding : FragmentConstructionBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewPagerAdapter: ConstructionFragmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConstructionBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = Bundle()
        viewPagerAdapter = ConstructionFragmentAdapter(childFragmentManager, lifecycle, bundle)
        with(binding) {
            ViewPager.adapter = viewPagerAdapter
            TabLayoutMediator(TabLayout, ViewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = "Sedang Dikerjakan"
                    1 -> tab.text = "Selesai"
                    2 -> tab.text = "Dibatalkan"
                }
            }.attach()
        }
    }
}