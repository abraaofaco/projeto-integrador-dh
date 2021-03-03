package com.pidh.a5plus.screen.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pidh.a5plus.other.enum.Tabs
import com.pidh.a5plus.screen.fragment.home.HomeFragment
import com.pidh.a5plus.screen.fragment.home.TabItemFragment

class TabsAdapter(val fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = Tabs.values().size

    override fun createFragment(position: Int): Fragment {
        val tab = Tabs.values()[position]

        return TabItemFragment(tab, fragment)
    }
}