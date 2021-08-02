package com.empreendapp.collev.ui.system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
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
import com.empreendapp.collev.adapters.ColetorFragmentPagerAdapter;
import com.empreendapp.collev.fragments.CategoriaPerfilFragment;
import com.google.android.material.tabs.TabLayout;

public class PerfilActivity extends AppCompatActivity {
    String titulos[] = {"Selecione a categoria de sua empresa", "Selecione o nome e localização", "Selecione o tamanho do recipiente"};
    TextView tvOptionTitle;
    NavHostFragment navHostFragment;
    NavController navController;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        setStatusBarBorderRadius(this);
        initViews();
    }

    private void initViews() {
        tvOptionTitle = (TextView) findViewById(R.id.tv_tab_title);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fcv_perfil);
        navController = navHostFragment.getNavController();
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