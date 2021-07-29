package com.empreendapp.collev.ui.system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.empreendapp.collev.R;

import static com.empreendapp.collev.ui.system.PerfilActivity.setStatusBarBorderRadius;

public class IntroActivity extends AppCompatActivity {
    ImageView imgLogo;
    TextView tvBoasVindas;
    RadioGroup rgSlide;
    RadioButton bSlide1, bSlide2, bSlide3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        setStatusBarBorderRadius(this);
        initViews();
        showIntroAnimate();
    }

    private void showIntroAnimate() {
        final Intent it = new Intent(this, LoginActivity.class);
        imgLogo.setVisibility(View.VISIBLE);

        //play animate logo
        YoYo.with(Techniques.FadeIn).duration(1600).repeat(0).playOn(imgLogo);

        //contar tempo (700ms)
        final Handler handler = new Handler();
        final Runnable r = new Runnable()
        {
            public void run() {
                tvBoasVindas.setVisibility(View.VISIBLE);

                //play animate logo
                YoYo.with(Techniques.FadeIn).duration(800).repeat(0).playOn(tvBoasVindas);

                final Handler handler2 = new Handler();
                final Runnable r2 = new Runnable() {
                    public void run() {
                        startActivity(it);
                        finish();
                    }
                }; handler2.postDelayed(r2, 1600);
            }
        };
        handler.postDelayed(r, 2000);

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