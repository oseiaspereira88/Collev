package com.empreendapp.collev.ui.system

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.EditText
import android.os.Bundle
import com.empreendapp.collev.R
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.daimajia.androidanimations.library.YoYo
import com.daimajia.androidanimations.library.Techniques
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CadastroActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var tvCadastrar: TextView? = null
    var editNome: EditText? = null
    var editEmail: EditText? = null
    var editSenha: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)
        InitPerfilActivity.setStatusBarBorderRadius(this)
        auth = Firebase.auth
        initViews()
    }

    private fun initViews() {
        tvCadastrar = findViewById<View>(R.id.tv_cadastrar) as TextView
        editNome = findViewById<View>(R.id.edit_nome) as EditText
        editEmail = findViewById<View>(R.id.edit_email) as EditText
        editSenha = findViewById<View>(R.id.edit_senha) as EditText
        tvCadastrar!!.setOnClickListener { v -> //animate
            YoYo.with(Techniques.Pulse).duration(300).repeat(0).playOn(v)
            val handler = Handler()
            val r = Runnable {
                if (validate()) {
                    cadastrar()
                } else {
                    editNome?.error = "Digite um nome válido!"
                    editEmail?.error = "Digite um email válido!"
                    editSenha?.error = "Digite uma senha valida!"
                }
            }
            handler.postDelayed(r, 300)
        }
    }

    private fun validate(): Boolean {
        return (!editNome?.text.toString().isEmpty() && editNome?.text.toString().length >= 6 &&
                !editEmail?.text.toString().isEmpty() && editEmail?.text.toString().length >= 10 &&
                !editSenha?.text.toString().isEmpty()) && editSenha?.text.toString().length >= 6
    }

    private fun cadastrar() {
        auth.createUserWithEmailAndPassword(editEmail?.text.toString(), editSenha?.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("INFO:", "createUserWithEmail:success")
                    startActivity(Intent(this, InitPerfilActivity::class.java))
                    finish()
                } else {
                    Log.w("ERROR:", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}