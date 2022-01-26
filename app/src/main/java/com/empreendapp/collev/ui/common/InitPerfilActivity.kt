package com.empreendapp.collev.ui.common

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.NavController
import android.os.Bundle
import com.empreendapp.collev.R
import android.view.KeyEvent
import com.empreendapp.collev.util.DefaultFunctions.Companion.checkExitAndSingout
import com.empreendapp.collev.util.DefaultLayout.Companion.setStatusBarBorderRadiusWhite
import com.empreendapp.collev.util.FirebaseConnection

class InitPerfilActivity : AppCompatActivity() {
    var navHostFragment: NavHostFragment? = null
    var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init_perfil)
        setStatusBarBorderRadiusWhite(this)
        initViews()
    }

    private fun initViews() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv_perfil) as NavHostFragment?
        navController = navHostFragment!!.navController
    }

    // Código botão Voltar
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            checkExitAndSingout(this, FirebaseConnection!!.getFirebaseAuth()!!)
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }
}