package com.empreendapp.collev.fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.empreendapp.collev.R;
import com.empreendapp.collev.model.User;
import com.empreendapp.collev.util.FirebaseConnection;
import com.empreendapp.collev.util.LibraryClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class CategoriaPerfilFragment extends Fragment {
    private FirebaseDatabase firebaseBD;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_categoria_perfil, null); //->container, false
        initViews(rootView);
        return rootView;
    }

    private void initFirebase() {
        firebaseBD = LibraryClass.getFirebaseDB();
        auth = FirebaseConnection.getFirebaseAuth();
    }

    public void initViews(View rootView){
        CardView cv_option_coletor = (CardView) rootView.findViewById(R.id.cv_option_coletor);
        CardView cv_option_voluntario = (CardView) rootView.findViewById(R.id.cv_option_voluntario);

        initFirebase();
        User user = new User();
        user.setId(auth.getCurrentUser().getUid());

        cv_option_coletor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateButton(v);

                user.setTipo("Coletor");
                user.saveTipoInFirebase();

                //chamada do LocalizacaoPerfilFragment
                NavHostFragment.findNavController(getParentFragment())
                        .navigate(R.id.action_categoriaPerfilFragment_to_localizacaoPerfilFragment);
            }
        });

        cv_option_voluntario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateButton(v);

                user.setTipo("Coletor");
                user.saveTipoInFirebase();

                //chamada do LocalizacaoPerfilFragment
                NavHostFragment.findNavController(getParentFragment())
                        .navigate(R.id.action_categoriaPerfilFragment_to_localizacaoPerfilFragment);
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