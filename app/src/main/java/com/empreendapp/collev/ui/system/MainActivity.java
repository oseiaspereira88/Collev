package com.empreendapp.collev.ui.system;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.empreendapp.collev.R;

import static com.empreendapp.collev.ui.system.PerfilActivity.setStatusBarBorderRadius;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setStatusBarBorderRadius(this);
    }
}