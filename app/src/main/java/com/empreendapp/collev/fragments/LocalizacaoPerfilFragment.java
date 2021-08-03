package com.empreendapp.collev.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.empreendapp.collev.R;

public class LocalizacaoPerfilFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_localizacao_perfil, container, false); //->container, false
        initViews(rootView);
        return rootView;
    }

    public void initViews(View rootView){
        rootView.findViewById(R.id.tvConcluir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(getParentFragment())
                        .navigate(R.id.action_localizacaoPerfilFragment_to_mainActivity);
            }
        });

        rootView.findViewById(R.id.tvVoltar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(getParentFragment())
                        .navigate(R.id.action_localizacaoPerfilFragment_to_categoriaPerfilFragment);
            }
        });


    }

    public void animateButton(View view) {
        YoYo.with(Techniques.Pulse)
                .duration(250)
                .repeat(0)
                .playOn(view);
    }
}