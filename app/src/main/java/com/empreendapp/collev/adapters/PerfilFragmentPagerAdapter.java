package com.empreendapp.collev.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.empreendapp.collev.fragments.CategoriaPerfilFragment;
import com.empreendapp.collev.fragments.DemandaPerfilFragment;
import com.empreendapp.collev.fragments.LocalizacaoPerfilFragment;

public class PerfilFragmentPagerAdapter extends FragmentPagerAdapter {
    private String optionsTitles [] = {"Categoria", "Localização", "Damanda"};

    public PerfilFragmentPagerAdapter(FragmentManager fm) {
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
                return new CategoriaPerfilFragment();
            case 1:
                return new LocalizacaoPerfilFragment();
            case 2:
                return new DemandaPerfilFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return optionsTitles[position];
    }
}
