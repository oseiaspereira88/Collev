package com.empreendapp.collev.adapters.pagers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.empreendapp.collev.ui.system.fragments.NotificacoesFragment
import com.empreendapp.collev.ui.coletor.ColetasFragment
import com.empreendapp.collev.ui.system.fragments.MenuFragment

class ColetorFragmentPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {
    private val optionsTitles = arrayOf("", "", "")
    override fun getCount(): Int {
        return optionsTitles.size
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ColetasFragment()
            1 -> NotificacoesFragment()
            2 -> MenuFragment()
            else -> MenuFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return optionsTitles[position]
    }
}