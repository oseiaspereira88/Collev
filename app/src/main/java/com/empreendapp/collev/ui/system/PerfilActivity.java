package com.empreendapp.collev.ui.system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.empreendapp.collev.R;
import com.empreendapp.collev.adapters.PerfilOptionAdapter;

public class PerfilActivity extends AppCompatActivity {
    ViewPager pager;
    PerfilOptionAdapter adapter;
    TextView tvPagTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        initViews();
    }

    private void initViews() {
        pager = (ViewPager) findViewById(R.id.pager);
        tvPagTitle = (TextView) findViewById(R.id.tv_pag_title);

        adapter = new PerfilOptionAdapter(this, tvPagTitle);
        pager.setAdapter(adapter);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i("script", "onPageScrolled");
            }

            @Override
            public void onPageSelected(int position) {
                Log.i("script", "onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("script", "onPageScrollStateChanged");
            }
        });
    }

    public void openProfileDialogCreator() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione uma das Opções");

        View view = View.inflate(this, R.layout.perfil_option_model, null);

        CardView option1 = (CardView) findViewById(R.id.cv_option_1);
        CardView option2 = (CardView) findViewById(R.id.cv_option_2);

        View.OnClickListener onClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardView cv = (CardView) v;
                //cv.setBackground();
            }
        };

        builder.setView(view);

        // Set up the buttons
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //proxima questão ou se for a última questão, finaliza o perfil;

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}