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

class CadastroActivity : AppCompatActivity() {
    var tvCadastrar: TextView? = null
    var editNome: EditText? = null
    var editEmail: EditText? = null
    var editSenha: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)
        InitPerfilActivity.setStatusBarBorderRadius(this)
        initViews()
    }

    private fun initViews() {
        tvCadastrar = findViewById<View>(R.id.tv_cadastrar) as TextView
        editNome = findViewById<View>(R.id.edit_nome) as EditText
        editEmail = findViewById<View>(R.id.edit_email) as EditText
        editSenha = findViewById<View>(R.id.edit_senha) as EditText
        val itPerfil = Intent(this, InitPerfilActivity::class.java)
        tvCadastrar!!.setOnClickListener { v -> //animate
            YoYo.with(Techniques.Pulse).duration(300).repeat(0).playOn(v)
            val handler = Handler()
            val r = Runnable {
                if (!editNome!!.text.toString().isEmpty() &&
                        !editEmail!!.text.toString().isEmpty() &&
                        !editSenha!!.text.toString().isEmpty()) {
                    startActivity(itPerfil)
                    finish()
                } else {
                    editNome!!.error = "Digite um nome válido!"
                    editEmail!!.error = "Digite um email válido!"
                    editSenha!!.error = "Digite uma senha valida!"
                }
            }
            handler.postDelayed(r, 300)
        }
    }
}