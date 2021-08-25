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
import com.empreendapp.collev.util.CryptWithMD5
import com.google.firebase.auth.FirebaseAuth
import com.empreendapp.collev.model.User
import com.google.android.gms.common.ConnectionResult
import com.empreendapp.collev.util.FirebaseConnection
import com.empreendapp.collev.util.LibraryClass
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseUser
import java.nio.charset.StandardCharsets
import java.security.MessageDigest


class LoginActivity : AppCompatActivity() {
    private var firebaseBD: FirebaseDatabase? = null
    private lateinit var auth: FirebaseAuth
    private var tvEntrar: TextView? = null
    private var tvCadastreSe: TextView? = null
    private var editEmail: EditText? = null
    private var editSenha: EditText? = null
    private var email: String = ""
    private var senha: String = ""

    public override fun onStart() {
        super.onStart()
        initFirebase();
        if (auth.currentUser != null) {
            if (auth.currentUser!!.isEmailVerified()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                //alert("Confirme sua conta no email", 1)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        InitPerfilActivity.setStatusBarBorderRadius(this)
        initViews()
    }

    private fun initFirebase() {
        firebaseBD = LibraryClass.getFirebaseDB()
        auth = FirebaseConnection.getFirebaseAuth()
    }

    private fun initViews() {
        tvEntrar = findViewById<View>(R.id.tv_entrar) as TextView
        tvCadastreSe = findViewById<View>(R.id.tv_cadastre_se) as TextView
        editEmail = findViewById<View>(R.id.edit_email) as EditText
        editSenha = findViewById<View>(R.id.edit_senha) as EditText
        val itCadastro = Intent(this, CadastroActivity::class.java)

        tvEntrar!!.setOnClickListener { v -> //animate
            YoYo.with(Techniques.Pulse).duration(300).repeat(0).playOn(v)
            val handler = Handler()
            val r = Runnable {
                getCampos()
                if (validate()) login()
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

    private fun validate(): Boolean {
        var isValided = true
        if (email.isEmpty() || email.length < 10 ||
            !email.contains('@') || !email.contains('.')
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

    private fun getCampos(){
        if(!editEmail!!.text.toString().isEmpty()) email = editEmail!!.text.toString()
        if(!editSenha!!.text.toString().isEmpty()) senha = editSenha!!.text.toString()
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

    private fun login() {
        auth.signInWithEmailAndPassword(email, encrypt(senha))
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("INFO:", "signInWithEmail:success")
                    val user: FirebaseUser? = auth.currentUser
                    if (user != null) {
                        if (!user.isEmailVerified) {
                            alert("Verifique o email de confirmação!", 0);
                        } else {
                            if (user.email?.let { it1 ->
                                    User().haveNameAndEmailEqualSP(
                                        applicationContext,
                                        it1
                                    )
                                } == true) {
                                val newUser = User()
                                newUser.restaureNameSP(applicationContext)
                                newUser.email = user.email
                                newUser.id = user.uid
                                newUser.saveInFirebase()
                                newUser.deleteNameSP(applicationContext)
                                startActivity(Intent(this, InitPerfilActivity::class.java))
                            } else {
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                        }
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("ERROR:", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Email ou senha incoretos!", Toast.LENGTH_SHORT)
                        .show()

                    // checar se o email é um dos cadastrados no bd;
                    // se sim, notificar erro no campo que apenas a senha está invalida.
                    // se não, dizer que os dois campos estão invalidos
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