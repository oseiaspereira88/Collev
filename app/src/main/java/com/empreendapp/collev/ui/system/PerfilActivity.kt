package com.empreendapp.collev.ui.system

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.empreendapp.collev.R
import com.empreendapp.collev.model.User
import com.empreendapp.collev.util.DefaultFunctions.Companion.alertSnack
import com.empreendapp.collev.util.DefaultFunctions.Companion.animateButton
import com.empreendapp.collev.util.FirebaseConnection
import com.empreendapp.collev.util.LibraryClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_perfil.*

class PerfilActivity : AppCompatActivity() {
    private var database: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    private var usuario: User? = null
    private var isNameEditing = false
    private var isEmpresaEditing = false

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
        tvPerfilNome.text = usuario!!.nome
        tvPerfilEmail.text = usuario!!.email
        tvPerfilEmpresa.text = usuario!!.nome_empresa
    }

    private fun initListeners() {
        imgBackPerfil!!.setOnClickListener{
            animateButton(it)
            if(isEmpresaEditing){
                toggleEditEmpresa()
            } else if(isNameEditing){
                toggleEditName()
            } else{
                finish()
            }
        }

        clCamera!!.setOnClickListener {
            alertSnack("OnClick()!", 2, clPerfil)
        }

        imgPerfilEditNome.setOnClickListener{
            if(!isNameEditing){
                toggleEditName()
            } else{
                if(editNameValidate(editPerfilNome)){
                    usuario!!.id = auth!!.uid
                    usuario!!.nome = editPerfilNome.text.toString()
                    usuario!!.saveNomeInFirebase().addOnCompleteListener {
                        if(it.isSuccessful){
                            tvPerfilNome.text = editPerfilNome.text.toString()
                            alertSnack("Novo nome salvo!", 1, clPerfil)
                            toggleEditName()
                        } else{
                            alertSnack("Falha na alteração!", 1, clPerfil)
                        }
                    }
                }
            }
        }

        imgPerfilEditEmpresa.setOnClickListener{
            if(!isEmpresaEditing){
                toggleEditEmpresa()
            } else{
                if(editNameValidate(editPerfilEmpresa)){
                    usuario!!.id = auth!!.uid
                    usuario!!.nome_empresa = editPerfilEmpresa.text.toString()
                    usuario!!.saveNomeEmpresaInFirebase().addOnCompleteListener {
                        if(it.isSuccessful){
                            tvPerfilEmpresa.text = editPerfilEmpresa.text.toString()
                            alertSnack("Novo nome salvo!", 1, clPerfil)
                            toggleEditEmpresa()
                        } else{
                            alertSnack("Falha na alteração!", 1, clPerfil)
                        }
                    }
                }
            }
        }

        imgPerfilResetSenha.setOnClickListener{
            alertSnack("Ação de reset senha!", 1, clPerfil)
        }
    }

    private fun toggleEditName(){
        if(isNameEditing){
            editPerfilNome.setText("")
            tilEditNome.visibility = View.INVISIBLE
            llPerfilNome.visibility = View.VISIBLE
            imgPerfilEditNome.setImageResource(R.drawable.icon_edit)
            animateButton(imgPerfilEditNome)
            isNameEditing = false
        } else{
            editPerfilNome.setText(usuario!!.nome)
            tilEditNome.visibility = View.VISIBLE
            llPerfilNome.visibility = View.INVISIBLE
            imgPerfilEditNome.setImageResource(R.drawable.icon_check_01)
            animateButton(imgPerfilEditNome)
            isNameEditing = true
        }
    }

    private fun toggleEditEmpresa(){
        if(isEmpresaEditing){
            editPerfilEmpresa.setText("")
            tilEditEmpresa.visibility = View.INVISIBLE
            llPerfilEmpresa.visibility = View.VISIBLE
            imgPerfilEditEmpresa.setImageResource(R.drawable.icon_edit)
            animateButton(imgPerfilEditEmpresa)
            isEmpresaEditing = false
        } else{
            editPerfilEmpresa.setText(usuario!!.nome_empresa)
            tilEditEmpresa.visibility = View.VISIBLE
            llPerfilEmpresa.visibility = View.INVISIBLE
            imgPerfilEditEmpresa.setImageResource(R.drawable.icon_check_01)
            animateButton(imgPerfilEditEmpresa)
            isEmpresaEditing = true
        }
    }

    private fun editNameValidate(edit: EditText): Boolean {
        var isValided = true
        if (edit.text.isEmpty() || edit.text.length < 6) {
            edit!!.error = "Digite um nome válido!"
            isValided = false
        }
        return isValided
    }

    override fun onBackPressed() {
        imgBackPerfil.callOnClick()
    }
}