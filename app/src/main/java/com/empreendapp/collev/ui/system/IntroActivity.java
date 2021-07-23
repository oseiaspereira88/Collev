package com.empreendapp.collev.ui.system;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.empreendapp.collev.R;

public class IntroActivity extends AppCompatActivity {
    ImageView imgLogo;
    TextView tvBoasVindas;
    RadioGroup rgSlide;
    RadioButton bSlide1, bSlide2, bSlide3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        initViews();
    }

    private void initViews() {
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        tvBoasVindas = (TextView) findViewById(R.id.tvBoasVindas);
        rgSlide = (RadioGroup) findViewById(R.id.rgSlide);
        bSlide1 = (RadioButton) findViewById(R.id.bSlide1);
        bSlide2 = (RadioButton) findViewById(R.id.bSlide2);
        bSlide3 = (RadioButton) findViewById(R.id.bSlide3);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton rButton = (RadioButton) v;
                rgSlide.clearCheck();
                rButton.setChecked(true);
            }
        };

        bSlide1.setOnClickListener(onClickListener);
        bSlide2.setOnClickListener(onClickListener);
        bSlide3.setOnClickListener(onClickListener);
    }


}