package com.empreendapp.collev.ui.system

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.NavController
import android.os.Bundle
import com.empreendapp.collev.R
import com.daimajia.androidanimations.library.YoYo
import com.daimajia.androidanimations.library.Techniques
import android.annotation.TargetApi
import android.os.Build
import android.app.Activity
import android.app.AlertDialog
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.TextView

class InitPerfilActivity : AppCompatActivity() {
    var navHostFragment: NavHostFragment? = null
    var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init_perfil)
        setStatusBarBorderRadius(this)
        initViews()
    }

    private fun initViews() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv_perfil) as NavHostFragment?
        navController = navHostFragment!!.navController
    }

    // Código botão Voltar
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            checkExit()
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    private fun checkExit() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Deseja realmente sair?")
                .setCancelable(false)
                .setPositiveButton("Sim") { dialog, id -> finish() }
                .setNegativeButton("Não") { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }

    fun animateButton(view: View?) {
        YoYo.with(Techniques.RotateIn)
                .duration(700)
                .repeat(0)
                .playOn(view)
    }

    companion object {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        fun setStatusBarBorderRadius(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = activity.window
                val background = activity.resources.getDrawable(R.drawable.pag_bg_01)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = activity.resources.getColor(android.R.color.transparent)
                window.navigationBarColor = activity.resources.getColor(android.R.color.transparent)
                window.setBackgroundDrawable(background)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }
}