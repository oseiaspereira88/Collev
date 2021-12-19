package com.empreendapp.collev.ui.system

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.EditText
import android.os.Bundle
import com.empreendapp.collev.R
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.daimajia.androidanimations.library.YoYo
import com.daimajia.androidanimations.library.Techniques
import com.empreendapp.collev.model.User
import com.empreendapp.collev.util.DefaultFunctions.Companion.alertSnack
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.security.NoSuchAlgorithmException
import com.empreendapp.collev.util.DefaultLayout.Companion.setStatusBarBorderRadiusWhite
import com.google.android.gms.common.ConnectionResult
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException

import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_cadastro.*
import java.lang.Exception
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
        setStatusBarBorderRadiusWhite(this)
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
                                    alertSnack("Um email foi enviado para seu email.", 1, clCadastro)
                                } else {
                                    alertSnack("Houve um erro ao soliciar a confirmação do cadastro!", 1, clCadastro)
                                }
                            }
                    }

                    finish()
                } else{
                        try {
                            throw task.exception!!
                        } catch (e: FirebaseAuthInvalidUserException){
                            if (e.errorCode.equals("ERROR_EMAIL_ALREADY_IN_USE")) {
                                alertSnack(getString(R.string.error_email_already_in_use), 1, clCadastro)
                                editEmail!!.setError(getString(R.string.error_email_already_in_use))
                                editEmail!!.requestFocus()
                            } else{
                                alertSnack("Falha ao cadastrar: " + e.message, 0, clCadastro)
                            }
                        } catch (e: FirebaseAuthUserCollisionException) {
                            alertSnack(getString(R.string.error_user_exists), 0, clCadastro)
                            editEmail!!.setError(getString(R.string.error_user_exists))
                            editEmail!!.requestFocus()
                        } catch (e: Exception){
                            alertSnack("Falha ao cadastrar: " + e.message, 0, clCadastro)
                        }
                }
            }
    }

    fun onConnectionFailed(connectionResult: ConnectionResult) {
        alertSnack("Error: $connectionResult", 1, clCadastro)
    }
}