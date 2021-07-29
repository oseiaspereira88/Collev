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

public class LoginActivity extends AppCompatActivity {
    TextView tvEntrar, tvCadastreSe;
    EditText editEmail, editSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setStatusBarBorderRadius(this);
        initViews();
    }

    private void initViews() {
        tvEntrar = (TextView) findViewById(R.id.tv_entrar);
        tvCadastreSe = (TextView) findViewById(R.id.tv_cadastre_se);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editSenha = (EditText) findViewById(R.id.edit_senha);

        final Intent itMain = new Intent(this, MainActivity.class);
        final Intent itCadastro = new Intent(this, CadastroActivity.class);

        tvEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //animate
                YoYo.with(Techniques.Pulse).duration(300).repeat(0).playOn(v);

                final Handler handler = new Handler();
                final Runnable r = new Runnable() {
                    public void run() {
                        if (!editEmail.getText().toString().isEmpty() && !editSenha.getText().toString().isEmpty()) {
                            startActivity(itMain);
                            finish();
                        } else{
                            editEmail.setError("Digite um email válido!");
                            editSenha.setError("Digite uma senha valida!");
                        }
                    }
                };
                handler.postDelayed(r, 300);
            }
        });

        tvCadastreSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //animate
                YoYo.with(Techniques.Pulse).duration(300).repeat(0).playOn(v);

                final Handler handler = new Handler();
                final Runnable r = new Runnable() {
                    public void run() {
                        startActivity(itCadastro);
                    }
                };
                handler.postDelayed(r, 300);
            }
        });
    }
}