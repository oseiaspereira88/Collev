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
import com.empreendapp.collev.model.User
import com.google.android.gms.common.ConnectionResult
import com.empreendapp.collev.util.FirebaseConnection
import com.empreendapp.collev.util.LibraryClass
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider


class LoginActivity : AppCompatActivity() {
    private var firebaseBD: FirebaseDatabase? = null
    private lateinit var auth: FirebaseAuth
    private var tvEntrar: TextView? = null
    private var tvCadastreSe: TextView? = null
    private var editEmail: EditText? = null
    private var editSenha: EditText? = null
    private var email: String = ""
    private var senha: String = ""
    private lateinit var mGoogleSignClient: GoogleSignInClient


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
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        InitPerfilActivity.setStatusBarBorderRadius(this)
        initViews()
        loginGoogle()
    }

    private fun initFirebase() {
        firebaseBD = LibraryClass.firebaseDB
        auth = FirebaseConnection.getFirebaseAuth()!!
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
        btn_google.setOnClickListener {
            signInGoogle()
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
                                newUser.restaureNameSP(applicationContext) // obs: pendência: caso não exista nome salvo, pedir o nome do usuário.
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

    private fun loginGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id_1))
            .requestEmail()
            .requestProfile()
            .build()
        mGoogleSignClient = GoogleSignIn.getClient(this, gso)
    }
    private fun signInGoogle(){
        val signInIntent: Intent = mGoogleSignClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN){

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "sucesso: " + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            }catch (e: ApiException){
                Log.d(TAG, "Error: ${e.statusCode} " + e.status)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }


    private fun updateUI(user: FirebaseUser?){

    }
    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
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