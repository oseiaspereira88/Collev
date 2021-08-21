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

class LoginActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var tvEntrar: TextView? = null
    var tvCadastreSe: TextView? = null
    var editEmail: EditText? = null
    var editSenha: EditText? = null

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        InitPerfilActivity.setStatusBarBorderRadius(this)
        auth = Firebase.auth
        initViews()
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

    private fun validate(): Boolean {
        return (!editEmail?.text.toString().isEmpty() && editEmail?.text.toString().length >= 10 &&
                editEmail?.text.toString().contains('@') && editEmail?.text.toString().contains('.') &&
                !editSenha?.text.toString().isEmpty()) && editSenha?.text.toString().length >= 6
    }

    private fun login(){
        auth.signInWithEmailAndPassword(editEmail?.text.toString(), editSenha?.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("INFO:", "signInWithEmail:success")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("ERROR:", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed: " + task.exception.toString(),
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}