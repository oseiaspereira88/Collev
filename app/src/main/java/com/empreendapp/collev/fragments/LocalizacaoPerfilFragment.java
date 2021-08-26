package com.empreendapp.collev.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.empreendapp.collev.R;
import com.empreendapp.collev.model.Local;
import com.empreendapp.collev.model.User;
import com.empreendapp.collev.util.FirebaseConnection;
import com.empreendapp.collev.util.LibraryClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class LocalizacaoPerfilFragment extends Fragment {
    private FirebaseDatabase firebaseBD;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_localizacao_perfil, container, false); //->container, false
        initViews(rootView);
        return rootView;
    }

    private void initFirebase() {
        firebaseBD = LibraryClass.getFirebaseDB();
        auth = FirebaseConnection.getFirebaseAuth();
    }

    public void initViews(View rootView){
        initFirebase();

        User user = new User();
        user.setId(auth.getCurrentUser().getUid());

        rootView.findViewById(R.id.tvConcluir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editNomeEmpresa = (EditText) rootView.findViewById(R.id.edit_nome_empresa);

                if(!editNomeEmpresa.getText().toString().isEmpty()){
                    user.setEndereco("Rua Terezinha Campelo, 117");
                    user.setNome_empresa(editNomeEmpresa.getText().toString());
                    Local local = new Local();
                    local.setId_localidade(0);
                    local.setNome("Local-Generico");
                    local.setDescricao("Esse é um local provisório para o ADM gerenciar os novos usuários.");
                    user.setLocalidade(local);
                    // o sistema poderia sugerir localidades próximas automaticamente se baseando na proximidade dos establececimentos em local generico
                    // com os que já estão alocados.
                    user.saveEnderecoInFirebase();
                    user.saveNomeEmpresaInFirebase();
                    user.saveLocalidadeInFirebase();

                    NavHostFragment.findNavController(getParentFragment())
                            .navigate(R.id.action_localizacaoPerfilFragment_to_mainActivity);
                } else{
                    editNomeEmpresa.setError("Esse campo é obrigatório!");
                }
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