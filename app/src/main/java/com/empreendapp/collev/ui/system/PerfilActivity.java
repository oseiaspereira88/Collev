package com.empreendapp.collev.ui.system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.empreendapp.collev.R;
import com.empreendapp.collev.adapters.PerfilFragmentPagerAdapter;
import com.empreendapp.collev.adapters.PerfilOptionAdapter;
import com.google.android.material.tabs.TabLayout;

public class PerfilActivity extends AppCompatActivity {
    ViewPager pager;
    TextView tvOptionTitle;
    private TabLayout tabLayout;
    PerfilFragmentPagerAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        setStatusBarBorderRadius(this);
        initViews();
    }

    private void initViews() {
        pager = (ViewPager) findViewById(R.id.pager);
        tvOptionTitle = (TextView) findViewById(R.id.tv_option_title);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        fragmentAdapter = new PerfilFragmentPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(pager);

        pager.addOnPageChangeListener(getOnPageChangeListener());
    }

    private ViewPager.OnPageChangeListener getOnPageChangeListener() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        tvOptionTitle.setText("Selecione a categoria de sua empresa");
                        break;
                    case 1:
                        tvOptionTitle.setText("Selecione o nome e localização");
                        break;
                    case 2:
                        tvOptionTitle.setText("Selecione o tamanho do recipiente");
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        };
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarBorderRadius(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.pag_bg_01);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    // Código botão Voltar
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            checkExit();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void checkExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Deseja realmente sair?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void animateButton(View view) {
        YoYo.with(Techniques.RotateIn)
                .duration(700)
                .repeat(0)
                .playOn(view);
    }
}