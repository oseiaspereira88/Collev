package com.empreendapp.collev.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.empreendapp.collev.ui.coletor.AgendaFragment
import com.empreendapp.collev.ui.coletor.HistoricoFragment
import com.empreendapp.collev.ui.coletor.MapsFragment
import com.empreendapp.collev.ui.coletor.SolicitacoesFragment

class ColetorFragmentPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {
    private val optionsTitles = arrayOf("Solicitações", "Agenda", "Histórico")
    override fun getCount(): Int {
        return optionsTitles.size
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> SolicitacoesFragment()
            1 -> AgendaFragment()
            2 -> HistoricoFragment()
            else -> MapsFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return optionsTitles[position]
    }
}