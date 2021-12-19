package com.empreendapp.collev.ui.system

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.empreendapp.collev.R
import com.empreendapp.collev.model.User
import com.empreendapp.collev.util.FirebaseConnection
import com.empreendapp.collev.util.LibraryClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.android.synthetic.main.activity_perfil.*

class PerfilActivity : AppCompatActivity() {

    private var imgBackPerfil: ImageView? = null
    private var imgPerfil: RoundedImageView? = null
    private var clCamera: ConstraintLayout? = null
    private var tvNome: TextView? = null
    private var imgEditNome: ImageView? = null
    private var tvEmpresa: TextView? = null
    private var imgEditEmpresa: ImageView? = null
    private var tvEmail: TextView? = null
    private var imgEditEmail: ImageView? = null
    private var imgEditSenha: ImageView? = null
    private var database: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    private var usuario: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        initFirebase()

        usuario = User()
        usuario!!.getCurrentUser(database!!, auth!!)!!.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                initViews()
                initListeners()
            }
        }
    }

    private fun initFirebase() {
        database = LibraryClass.firebaseDB?.reference
        auth = FirebaseConnection.getFirebaseAuth()!!
    }

    fun initViews(){
        imgBackPerfil = findViewById(R.id.imgBackPerfil)
        imgPerfil = findViewById(R.id.imgPerfil)
        clCamera = findViewById(R.id.clCamera)
        tvNome = findViewById(R.id.tvPerfilNome)
        imgEditNome = findViewById(R.id.imgEditNome)
        tvEmpresa = findViewById(R.id.tvPerfilEmpresa)
        imgEditEmpresa = findViewById(R.id.imgEditEmpresa)
        tvEmail = findViewById(R.id.tvPerfilEmail)
        imgEditEmail = findViewById(R.id.imgEditEmail)
        imgEditSenha = findViewById(R.id.imgEditSenha)

        tvPerfilNome.text = usuario!!.nome
        tvPerfilEmail.text = usuario!!.email
        tvPerfilEmpresa.text = usuario!!.nome_empresa
    }

    private fun initListeners() {
        imgBackPerfil!!.setOnClickListener{
            finish()
        }

        clCamera!!.setOnClickListener {
            Toast.makeText(this, "OnClick()!", Toast.LENGTH_SHORT).show()
        }
    }
}