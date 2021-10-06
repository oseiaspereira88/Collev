package com.empreendapp.collev.ui.system

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.empreendapp.collev.R
import com.makeramen.roundedimageview.RoundedImageView

class PerfilActivity : AppCompatActivity() {
    var imgBackPerfil: ImageView? = null
    var imgPerfil: RoundedImageView? = null
    var clCamera: ConstraintLayout? = null
    var tvNome: TextView? = null
    var imgEditNome: ImageView? = null
    var tvEmpresa: TextView? = null
    var imgEditEmpresa: ImageView? = null
    var tvEmail: TextView? = null
    var imgEditEmail: ImageView? = null
    var imgEditSenha: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        initViews()
        initListeners()
    }

    fun initViews(){
        imgBackPerfil = findViewById(R.id.imgBackPerfil)
        imgPerfil = findViewById(R.id.imgPerfil)
        clCamera = findViewById(R.id.clCamera)
        tvNome = findViewById(R.id.tvNome)
        imgEditNome = findViewById(R.id.imgEditNome)
        tvEmpresa = findViewById(R.id.tvEmpresa)
        imgEditEmpresa = findViewById(R.id.imgEditEmpresa)
        tvEmail = findViewById(R.id.tvEmail)
        imgEditEmail = findViewById(R.id.imgEditEmail)
        imgEditSenha = findViewById(R.id.imgEditSenha)
    }

    private fun initListeners() {
        imgBackPerfil!!.setOnClickListener{
            Toast.makeText(this, "Back!", Toast.LENGTH_SHORT).show()
        }

        clCamera!!.setOnClickListener {
            Toast.makeText(this, "OnClick()!", Toast.LENGTH_SHORT).show()
        }
    }
}