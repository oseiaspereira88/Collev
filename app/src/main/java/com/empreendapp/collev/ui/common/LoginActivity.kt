package com.empreendapp.collev.ui.common

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.empreendapp.collev.R
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.empreendapp.collev.model.User
import com.empreendapp.collev.util.DefaultFunctions.Companion.alert
import com.empreendapp.collev.util.DefaultFunctions.Companion.alertSnack
import com.empreendapp.collev.util.DefaultFunctions.Companion.whenAlertSleep
import com.empreendapp.collev.util.DefaultFunctions.Companion.animateButton
import com.empreendapp.collev.util.DefaultLayout.Companion.setStatusBarBorderRadiusWhite
import com.google.android.gms.common.ConnectionResult
import com.empreendapp.collev.util.FirebaseConnection
import com.empreendapp.collev.util.InputValidate.Companion.validateEmail
import com.empreendapp.collev.util.InputValidate.Companion.validateSenha
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
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.android.synthetic.main.activity_login.editEmail
import java.lang.Exception


class LoginActivity : AppCompatActivity() {
    private var callbackManager: CallbackManager? = null
    private var firebaseBD: FirebaseDatabase? = null
    private lateinit var auth: FirebaseAuth
    private var email: String = ""
    private var senha: String = ""
    private lateinit var mGoogleSignClient: GoogleSignInClient


    public override fun onStart() {
        super.onStart()
        initFirebase()
        if (auth.currentUser != null) {
            if (auth.currentUser!!.isEmailVerified()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
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
        tvEntrar!!.setOnClickListener { v -> //animate
            animateButton(v)
            val handler = Handler()
            val r = Runnable {
                getCampos()
                if (validateEmail(editEmail) && validateSenha(editSenha)){
                    login()
                }
            }
            handler.postDelayed(r, 300)
        }

        tvCadastrar!!.setOnClickListener { v -> //animate
            animateButton(v)
            val handler = Handler()
            val r = Runnable { startActivity(Intent(this, CadastroActivity::class.java)) }
            handler.postDelayed(r, 300)
        }

        btnGoogle.setOnClickListener {
            signInGoogle()
            alertSnack("The google button was clicked!", 1, clLogin)
        }

        tvResetSenha.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            val dialogView = this.layoutInflater.inflate(R.layout.dialog_input_email, null)
            dialogBuilder.setView(dialogView)
            val dialog = dialogBuilder.create()
            dialog!!.getWindow()?.setBackgroundDrawableResource(R.drawable.transparent_bg)
            dialog.show()

            val tvDialogSolicitarAlteracao = dialogView.findViewById<TextView>(R.id.tvDialogSolicitarAlteracao)
            val editDialogResetEmail = dialogView.findViewById<EditText>(R.id.editDialogResetEmail)
            val imgCancelDialog = dialogView.findViewById<ImageView>(R.id.imgCancelDialog)

            tvDialogSolicitarAlteracao.setOnClickListener {
                if(validateEmail(editDialogResetEmail)){
                    auth.sendPasswordResetEmail(editDialogResetEmail.text.toString())
                        .addOnCompleteListener {
                        if(it.isSuccessful){
                            alertSnack("Um email de alteração de senha foi enviado!", 2, clLogin)
                            dialog.dismiss()
                        } else{
                            if(it.exception is FirebaseAuthInvalidUserException){
                                dialog.dismiss()
                                alertSnack("Esse email não corresponde a nenhum usuário!", 2, clLogin)
                                whenAlertSleep(Runnable { dialog.show() })
                            } else{
                                alertSnack("Houve um problema ao solicitar alteração de senha!", 2, clLogin)
                            }
                        }
                    }
                }
            }

            imgCancelDialog.setOnClickListener { dialog.dismiss() }
        }
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
        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val currentUser: FirebaseUser? = auth.currentUser
                    if (currentUser != null) {
                        if (!currentUser.isEmailVerified) {
                            alertSnack("Verifique o email de confirmação!", 0, clLogin)
                        } else {
                            if (User().haveNameAndEmailEqualSP(this, currentUser.email!!)) {
                                var newUser = User()
                                newUser.restaureNameSP(this) // obs: pendência: caso não exista nome salvo, pedir o nome do usuário.
                                newUser.email = currentUser.email
                                newUser.id = currentUser.uid
                                newUser.saveInFirebase()
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
                            } else {
                                alertSnack(e.getLocalizedMessage(), 1, clLogin)
                            }
                        } catch (e: FirebaseNetworkException) {
                            alertSnack(getString(R.string.error_connection_fail), 1, clLogin)
                        } catch (e: Exception) {
                            alertSnack(getString(R.string.error_auth), 1, clLogin)
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