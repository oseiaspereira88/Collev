package com.empreendapp.collev.ui.system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.empreendapp.collev.R;

import static com.empreendapp.collev.ui.system.PerfilActivity.setStatusBarBorderRadius;

public class CadastroActivity extends AppCompatActivity {
    TextView tvCadastrar;
    EditText editNome, editEmail, editSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        setStatusBarBorderRadius(this);
        initViews();
    }

    private void initViews() {
        tvCadastrar = (TextView) findViewById(R.id.tv_cadastrar);
        editNome = (EditText) findViewById(R.id.edit_nome);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editSenha = (EditText) findViewById(R.id.edit_senha);

        final Intent itPerfil = new Intent(this, PerfilActivity.class);

        tvCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //animate
                YoYo.with(Techniques.Pulse).duration(300).repeat(0).playOn(v);

                final Handler handler = new Handler();
                final Runnable r = new Runnable() {
                    public void run() {
                        if (!editNome.getText().toString().isEmpty() &&
                                !editEmail.getText().toString().isEmpty() &&
                                !editSenha.getText().toString().isEmpty()) {

                            startActivity(itPerfil);
                            finish();
                        } else{
                            editNome.setError("Digite um nome válido!");
                            editEmail.setError("Digite um email válido!");
                            editSenha.setError("Digite uma senha valida!");
                        }
                    }
                };
                handler.postDelayed(r, 300);
            }
        });
    }
}