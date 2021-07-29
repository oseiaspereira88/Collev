package com.empreendapp.collev.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.empreendapp.collev.R;

public class DemandaPerfilFragment extends Fragment {
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_demanda_perfil, container, false); //->container, false
        initViews(rootView);
        return rootView;
    }

    public void initViews(View rootView){
        //gridView = (GridView) rootView.findViewById(R.id.notifiGrid);
    }
}