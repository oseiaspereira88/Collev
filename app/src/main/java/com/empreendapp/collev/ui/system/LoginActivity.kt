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


class LoginActivity : AppCompatActivity() {
    private var firebaseBD: FirebaseDatabase? = null
    private lateinit var auth: FirebaseAuth
    private var tvEntrar: TextView? = null
    private var tvCadastreSe: TextView? = null
    private var editEmail: EditText? = null
    private var editSenha: EditText? = null

    public override fun onStart() {
        super.onStart()
        initFirebase();
        if(auth.currentUser != null){
            if(auth.currentUser!!.isEmailVerified()){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else{
                alert("Confirme sua conta no email", 1)
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        val it = Intent(baseContext, MainActivity::class.java)
        if (user != null && user.isEmailVerified) {
            if (user.email?.let { it1 -> User().haveNameAndEmailEqualSP(applicationContext, it1) } == true) {
                val newUser = User()
                newUser.restaureNameSP(applicationContext)
                newUser.email = user.email
                newUser.id = user.uid
                newUser.saveInFirebase()
                newUser.deleteNameSP(applicationContext)
                startActivity(it)
            } else {
                if (user != null) {
                    startActivity(it)
                }
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
                if (validate()) {
                    login()
                } else {
                    editEmail!!.error = "Digite um email válido!"
                    editSenha!!.error = "Digite uma senha válida!"
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

    private fun validate(): Boolean {
        return (!editEmail?.text.toString().isEmpty() && editEmail?.text.toString().length >= 10 &&
                editEmail?.text.toString().contains('@') && editEmail?.text.toString().contains('.') &&
                !editSenha?.text.toString().isEmpty()) && editSenha?.text.toString().length >= 6
    }

    private fun login(){
        auth.signInWithEmailAndPassword(editEmail?.text.toString().trim(), CryptWithMD5.gerarMD5Hash(editSenha?.text.toString().trim()))
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("INFO:", "signInWithEmail:success")
                    val user: FirebaseUser? = auth.currentUser
                    if (user != null) {
                        if (user.isEmailVerified()) {
                            alert("Verifique o email de confirmação!", 0);
                        } else{
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("ERROR:", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed", Toast.LENGTH_SHORT).show()
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