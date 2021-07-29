package com.empreendapp.collev.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.empreendapp.collev.R;

public class CategoriaPerfilFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_categoria_perfil, null); //->container, false
        initViews(rootView);
        return rootView;
    }

    public void initViews(View rootView){
        //gridView = (GridView) rootView.findViewById(R.id.notifiGrid);
    }
}