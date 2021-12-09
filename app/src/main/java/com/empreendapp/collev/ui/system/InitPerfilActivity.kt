package com.empreendapp.collev.ui.system

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.NavController
import android.os.Bundle
import com.empreendapp.collev.R
import com.daimajia.androidanimations.library.YoYo
import com.daimajia.androidanimations.library.Techniques
import android.app.AlertDialog
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import com.empreendapp.collev.util.DefaultFunctions.Companion.checkExit
import com.empreendapp.collev.util.DefaultLayout.Companion.setStatusBarBorderRadiusWhite

class InitPerfilActivity : AppCompatActivity() {
    var navHostFragment: NavHostFragment? = null
    var navController: NavController? = null
    var imgBackPerfil: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init_perfil)
        setStatusBarBorderRadiusWhite(this)
        initViews()
    }

    private fun initViews() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv_perfil) as NavHostFragment?
        imgBackPerfil = findViewById(R.id.imgBackPerfil)

        navController = navHostFragment!!.navController

        imgBackPerfil!!.setOnClickListener{
            finish()
        }
    }

    // Código botão Voltar
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            checkExit(this)
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }
}