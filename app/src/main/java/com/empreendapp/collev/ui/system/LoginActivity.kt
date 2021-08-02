package com.empreendapp.collev.ui.system

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.EditText
import android.os.Bundle
import com.empreendapp.collev.R
import android.content.Intent
import android.os.Handler
import android.view.View
import com.daimajia.androidanimations.library.YoYo
import com.daimajia.androidanimations.library.Techniques

class LoginActivity : AppCompatActivity() {
    var tvEntrar: TextView? = null
    var tvCadastreSe: TextView? = null
    var editEmail: EditText? = null
    var editSenha: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        InitPerfilActivity.setStatusBarBorderRadius(this)
        initViews()
    }

    private fun initViews() {
        tvEntrar = findViewById<View>(R.id.tv_entrar) as TextView
        tvCadastreSe = findViewById<View>(R.id.tv_cadastre_se) as TextView
        editEmail = findViewById<View>(R.id.edit_email) as EditText
        editSenha = findViewById<View>(R.id.edit_senha) as EditText
        val itMain = Intent(this, MainActivity::class.java)
        val itCadastro = Intent(this, CadastroActivity::class.java)
        tvEntrar!!.setOnClickListener { v -> //animate
            YoYo.with(Techniques.Pulse).duration(300).repeat(0).playOn(v)
            val handler = Handler()
            val r = Runnable {
                if (!editEmail!!.text.toString().isEmpty() && !editSenha!!.text.toString().isEmpty()) {
                    startActivity(itMain)
                    finish()
                } else {
                    editEmail!!.error = "Digite um email válido!"
                    editSenha!!.error = "Digite uma senha valida!"
                }
            }
            handler.postDelayed(r, 300)
        }
        tvCadastreSe!!.setOnClickListener { v -> //animate
            YoYo.with(Techniques.Pulse).duration(300).repeat(0).playOn(v)
            val handler = Handler()
            val r = Runnable { startActivity(itCadastro) }
            handler.postDelayed(r, 300)
        }
    }
}