package com.hackathon.devlabsuser.adapter.progress

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hackathon.devlabsuser.ui.main.construction.MenungguKonfirmasiFragment
import com.hackathon.devlabsuser.ui.main.construction.SedangDikerjakanFragment
import com.hackathon.devlabsuser.ui.main.construction.SelesaiFragment

class ConstructionFragmentAdapter(fm: FragmentManager, lifecycle: Lifecycle, data: Bundle) :
    FragmentStateAdapter(fm, lifecycle) {
    private var fragmentBundle: Bundle

    init {
        fragmentBundle = data
    }

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        var fragment = Fragment()
        when (position) {
            0 -> fragment = MenungguKonfirmasiFragment()
            1 -> fragment = SedangDikerjakanFragment()
            2 -> fragment = SelesaiFragment()
        }
        fragment.arguments = this.fragmentBundle
        return fragment
    }
}