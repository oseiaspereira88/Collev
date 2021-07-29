package com.empreendapp.collev.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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
        CardView cv_option_1 = (CardView) rootView.findViewById(R.id.cv_option_1);
        CardView cv_option_2 = (CardView) rootView.findViewById(R.id.cv_option_2);

        cv_option_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateButton(v);
                if(cv_option_2.getCardBackgroundColor().getDefaultColor() != 99){
                    cv_option_1.setCardBackgroundColor(99);
                } else{
                    cv_option_2.setCardBackgroundColor(Color.WHITE);
                    cv_option_1.setCardBackgroundColor(99);
                }
            }
        });

        cv_option_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateButton(v);
                if(cv_option_1.getCardBackgroundColor().getDefaultColor() != 99){
                    cv_option_2.setCardBackgroundColor(99);
                } else{
                    cv_option_1.setCardBackgroundColor(Color.WHITE);
                    cv_option_2.setCardBackgroundColor(99);
                }
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