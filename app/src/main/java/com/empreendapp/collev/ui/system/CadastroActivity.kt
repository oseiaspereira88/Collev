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
import com.empreendapp.collev.model.User
import com.empreendapp.collev.util.CryptWithMD5
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.security.NoSuchAlgorithmException
import androidx.annotation.NonNull
import com.google.android.gms.common.ConnectionResult

import com.google.android.gms.tasks.OnCompleteListener

import com.google.firebase.auth.FirebaseUser


class CadastroActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var user: User? = null
    private var tvCadastrar: TextView? = null
    private var editNome: EditText? = null
    private var editEmail: EditText? = null
    private var editSenha: EditText? = null

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

    @Throws(NoSuchAlgorithmException::class)
    private fun initUser() {
        user = User()
        user!!.nome = editNome?.text.toString().trim()
        user!!.email = editEmail?.text.toString().trim()
        user!!.senha = editSenha?.text.toString().trim()
        user!!.gerarCryptSenha()
        user!!.saveNameAndEmailSP(applicationContext)
    }

    private fun cadastrar() {
        initUser();

        auth.createUserWithEmailAndPassword(editEmail?.text.toString().trim(), CryptWithMD5.gerarMD5Hash(editSenha?.text.toString().trim()))
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("INFO:", "createUserWithEmail:success")

                    //*** Sempre que for a primeira vez que o usuário fizer login depois de confirmar o email, inicializar InitPerfil
                    //startActivity(Intent(this, InitPerfilActivity::class.java))

                    val user: FirebaseUser? = auth.currentUser
                    if (user != null) {
                        user.sendEmailVerification()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    alert("Um email foi enviado para confirmar seu cadastro.", 1)
                                } else {
                                    alert("Houve um erro ao soliciar a confirmação do cadastro!", 1)
                                }
                            }
                    }

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

    private fun alert(txtAlert: String, delay: Int) {
        if (delay == 0) {
            Toast.makeText(applicationContext, txtAlert, Toast.LENGTH_SHORT).show()
        } else if (delay == 1) {
            Toast.makeText(applicationContext, txtAlert, Toast.LENGTH_LONG).show()
        }
    }

    fun onConnectionFailed(connectionResult: ConnectionResult) {
        alert("Error: $connectionResult", 1)
    }
}