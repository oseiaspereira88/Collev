package com.empreendapp.collev.adapters.pagers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.empreendapp.collev.ui.coletor.NotificacoesFragment
import com.empreendapp.collev.ui.coletor.MenuFragment
import com.empreendapp.collev.ui.coletor.ColetasFragment
import com.empreendapp.collev.ui.voluntario.VoluntarioFragment

class VoluntarioFragmentPagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm!!) {
    private val optionsTitles = arrayOf("", "", "")
    override fun getCount(): Int {
        return optionsTitles.size
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> VoluntarioFragment()
            1 -> NotificacoesFragment()
            2 -> MenuFragment()
            else -> MenuFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return optionsTitles[position]
    }
}