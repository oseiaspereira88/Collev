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
import java.nio.charset.StandardCharsets
import java.security.MessageDigest


class CadastroActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var user: User? = null
    private var tvCadastrar: TextView? = null
    private var editNome: EditText? = null
    private var editEmail: EditText? = null
    private var editSenha: EditText? = null
    private var nome: String = ""
    private var email: String = ""
    private var senha: String = ""

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
                getCampos()
                if (validate()) cadastrar()
            }
            handler.postDelayed(r, 300)
        }
    }

    private fun getCampos(){
        if(!editNome!!.text.toString().isEmpty()) nome = editNome!!.text.toString()
        if(!editEmail!!.text.toString().isEmpty()) email = editEmail!!.text.toString()
        if(!editSenha!!.text.toString().isEmpty()) senha = editSenha!!.text.toString()
    }

    private fun validate(): Boolean {
        var isValided = true
        if (nome.isEmpty() || nome.length < 6) {
            editNome?.error = "Digite um nome válido!"
            isValided = false
        }
        if (email.isEmpty() || email.length < 10
            || !email.contains('@') || !email.contains('.')
        ) {
            editEmail?.error = "Digite um email válido!"
            isValided = false
        }
        if (senha.isEmpty() || senha.length < 6) {
            editSenha?.error = "Digite uma senha válida!"
            isValided = false
        }
        return isValided
    }

    @Throws(NoSuchAlgorithmException::class)
    private fun initUser() {
        user = User()
        user!!.nome = nome.trim()
        user!!.email = email.trim()
        user!!.senha = encrypt(senha.trim())
        user!!.saveNameAndEmailSP(applicationContext)
    }

    private fun encrypt(text: String): String{
        val md = MessageDigest.getInstance("MD5")
        val hashInBytes = md.digest(text.toByteArray(StandardCharsets.UTF_8))
        val sb = StringBuilder()
        for (b in hashInBytes) {
            sb.append(kotlin.String.format("%02x", b))
        }
        return sb.toString()
    }

    private fun cadastrar() {
        initUser();

        auth.createUserWithEmailAndPassword(email, encrypt(senha))
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
                                    alert("Um email foi enviado para seu email.", 1)
                                } else {
                                    alert("Houve um erro ao soliciar a confirmação do cadastro!", 1)
                                }
                            }
                    }

                    finish()
                } else {
                    Log.w("ERROR:", "createUserWithEmail:failure", task.exception)
                    alert("Falha no cadastro!", 0)
                    alert("Possivelmente o email já foi utilizado em outro cadastro.", 1)
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