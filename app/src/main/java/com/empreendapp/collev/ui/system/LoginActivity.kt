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
import com.daimajia.androidanimations.library.YoYo
import com.daimajia.androidanimations.library.Techniques
import com.empreendapp.collev.model.User
import com.empreendapp.collev.util.DefaultFunctions.Companion.alert
import com.empreendapp.collev.util.DefaultLayout.Companion.setStatusBarBorderRadiusWhite
import com.google.android.gms.common.ConnectionResult
import com.empreendapp.collev.util.FirebaseConnection
import com.empreendapp.collev.util.LibraryClass
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger;
import com.facebook.FacebookException

import com.facebook.login.LoginResult

import com.facebook.FacebookCallback

import com.facebook.login.LoginManager

import com.facebook.CallbackManager
import com.google.firebase.auth.*

import com.google.firebase.auth.FirebaseAuthUserCollisionException

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

import java.lang.Exception


class LoginActivity : AppCompatActivity() {
    private var callbackManager: CallbackManager? = null
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
        setStatusBarBorderRadiusWhite(this)
        initViews()
        loginGoogle()
        initFacebook()
        initFacebookCallback()
    }

    private fun initFacebookCallback() {
        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    // App code
                }

                override fun onCancel() {
                    // App code
                }

                override fun onError(exception: FacebookException) {
                    // App code
                }
            })
    }

    private fun initFacebook() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
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
            alert("The google button was clicked!", 1, this)
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

    private fun getCampos() {
        if (!editEmail!!.text.toString().isEmpty()) email = editEmail!!.text.toString()
        if (!editSenha!!.text.toString().isEmpty()) senha = editSenha!!.text.toString()
    }

    private fun encrypt(text: String): String {
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
                    val userFirebase: FirebaseUser? = auth.currentUser
                    if (userFirebase != null) {
                        if (!userFirebase.isEmailVerified) {
                            alert("Verifique o email de confirmação!", 0, this)
                        } else {
                            if (userFirebase.email?.let { it1 ->
                                    User().haveNameAndEmailEqualSP(
                                        applicationContext,
                                        it1
                                    )
                                } == true) {
                                var newUser = User()
                                newUser.restaureNameSP(applicationContext) // obs: pendência: caso não exista nome salvo, pedir o nome do usuário.
                                newUser.email = userFirebase.email
                                newUser.id = userFirebase.uid
                                newUser.saveInFirebase()
                                newUser.deleteNameSP(applicationContext)
                                startActivity(Intent(this, InitPerfilActivity::class.java))
                            } else {
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                        }
                    }
                } else{
                    if (!task.isSuccessful) {
                        try {
                            throw task.exception!!
                        } catch (e: FirebaseAuthInvalidCredentialsException) {
                            editSenha!!.setError(getString(R.string.error_incorrect_password))
                            editSenha!!.requestFocus()
                        } catch (e: FirebaseAuthInvalidUserException) {
                            val errorCode = e.errorCode

                            if (errorCode.equals("ERROR_USER_NOT_FOUND")) {
                                editEmail!!.setError(getString(R.string.error_user_not_found))
                                editEmail!!.requestFocus()
                            } else if (errorCode.equals("ERROR_USER_DISABLED")) {
                                editEmail!!.setError(getString(R.string.error_user_disable))
                                editEmail!!.requestFocus()
                            }
//                            else if (errorCode.equals("ERROR_EMAIL_ALREADY_IN_USE")) {
//                                alert(getString(R.string.error_email_already_in_use), 1, this)
//                                editEmail!!.setError(getString(R.string.error_email_already_in_use))
//                                editEmail!!.requestFocus()
//                            }
                            else {
                                alert(e.getLocalizedMessage(), 1, this)
                            }
                        } catch (e: FirebaseAuthWebException) {
                            alert(getString(R.string.error_connection_fail), 1, this)
                        }
//                        catch (e: FirebaseAuthUserCollisionException) {
//                            alert(getString(R.string.error_user_exists), 0, this)
//                            editEmail!!.setError(getString(R.string.error_user_exists))
//                            editEmail!!.requestFocus()
//                        }
                        catch (e: Exception) {
                            alert(getString(R.string.error_auth), 1, this)
                            alert(e.toString(), 2, this)
                            Log.e(TAG, e.message!!)
                        }
                    }
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

    private fun signInGoogle() {
        val signInIntent: Intent = mGoogleSignClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun loginWithFacebook() {
//        loginButton = findViewById<View>(R.id.login_button) as LoginButton
//        loginButton.setReadPermissions("email")
//         If using in a fragment
//         If using in a fragment
//        loginButton.setFragment(this)
//
//         Callback registration
//        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
//            override fun onSuccess(loginResult: LoginResult?) {
//                // App code
//            }
//
//            override fun onCancel() {
//                // App code
//            }
//
//            override fun onError(exception: FacebookException) {
//                // App code
//            }
//        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "Sucesso: " + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.d(TAG, "Error: ${e.statusCode} " + e.status)
            }
        } else {
            callbackManager!!.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
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


    private fun updateUI(user: FirebaseUser?) {

    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

    fun onConnectionFailed(connectionResult: ConnectionResult) {
        alert("Error: $connectionResult", 1, this)
    }
}