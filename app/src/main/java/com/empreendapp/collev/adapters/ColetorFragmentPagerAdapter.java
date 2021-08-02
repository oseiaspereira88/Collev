package com.empreendapp.collev.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.empreendapp.collev.MapsFragment;
import com.empreendapp.collev.fragments.CategoriaPerfilFragment;
import com.empreendapp.collev.fragments.DemandaPerfilFragment;
import com.empreendapp.collev.fragments.LocalizacaoPerfilFragment;

public class ColetorFragmentPagerAdapter extends FragmentPagerAdapter {
    private String optionsTitles [] = {"Categoria", "Localização", "Demanda"};

    public ColetorFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return optionsTitles.length;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MapsFragment();
            case 1:
                return new MapsFragment();
            case 2:
                return new MapsFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return optionsTitles[position];
    }
}
