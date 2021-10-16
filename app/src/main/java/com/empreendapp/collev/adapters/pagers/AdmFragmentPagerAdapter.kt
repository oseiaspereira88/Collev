package com.empreendapp.collev.adapters.pagers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.empreendapp.collev.ui.adm.AdmFragment
import com.empreendapp.collev.ui.adm.ColetoresFragment
import com.empreendapp.collev.ui.system.fragments.MenuFragment

class AdmFragmentPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {
    private val optionsTitles = arrayOf("", "", "")
    override fun getCount(): Int {
        return optionsTitles.size
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> AdmFragment()
            1 -> ColetoresFragment()
            2 -> MenuFragment()
            else -> AdmFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return optionsTitles[position]
    }
}