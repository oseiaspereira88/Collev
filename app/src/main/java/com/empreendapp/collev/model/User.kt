package com.empreendapp.collev.model

import android.content.Context
import android.content.SharedPreferences;
import android.os.Handler
import com.empreendapp.collev.util.LibraryClass
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.empreendapp.collev.util.CryptWithMD5
import com.empreendapp.collev.util.sharedpreferences.DefaultsSP.Companion.EMAIL
import com.empreendapp.collev.util.sharedpreferences.DefaultsSP.Companion.ID
import com.empreendapp.collev.util.sharedpreferences.DefaultsSP.Companion.NOME
import com.empreendapp.collev.util.sharedpreferences.DefaultsSP.Companion.PREF
import com.empreendapp.collev.util.sharedpreferences.DefaultsSP.Companion.TIPO
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import java.security.NoSuchAlgorithmException


@JsonIgnoreProperties(*arrayOf("id", "senha"))
open class User {
    var id: String? = null
    var nome: String? = null
    var email: String? = null
    var senha: String? = null
    var tipo: String? = null
    var endereco: String? = null
    var id_local: String? = null
    var nome_empresa: String? = null
    var profile_image_id: String? = null
    var initialized_profile: String? = null
    var ativo: Boolean? = null

    fun getCurrentUser(database: DatabaseReference, auth: FirebaseAuth): Task<DataSnapshot>? {
        return database!!.child("users").child(auth!!.uid.toString()).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = task.result.getValue(User::class.java)
                    setUser(currentUser)
                } else {
                    try {
                        task.exception!!
                    } catch (e: FirebaseNetworkException) {
                        val h = Handler()
                        val r = Runnable {
                            getCurrentUser(database, auth)
                        }
                        h.postDelayed(r, 2000)
                    }
                }
            }
    }

    fun setUser(user: User?){
        id = user?.id
        nome = user?.nome
        email = user?.email
        tipo = user?.tipo
        endereco = user?.endereco
        id_local = user?.id_local
        nome_empresa = user?.nome_empresa
        profile_image_id = user?.profile_image_id
        initialized_profile = user?.initialized_profile
    }

    open fun saveInFirebase() {
        var bdRef = LibraryClass.firebaseDB?.reference
        bdRef = id?.let { bdRef?.child("users")?.child(it) }!!
        this.id = null
        bdRef.setValue(this)
    }

    open fun saveTipoInFirebase() {
        var bdRef = LibraryClass.firebaseDB?.reference
        bdRef = id?.let { bdRef?.child("users")?.child(it) }!!
        bdRef.child("tipo").setValue(tipo)
    }

    open fun saveEnderecoInFirebase() {
        var bdRef = LibraryClass.firebaseDB?.reference
        bdRef = id?.let { bdRef?.child("users")?.child(it) }!!
        bdRef.child("endereco").setValue(endereco)
    }

    open fun saveIdLocalInFirebase() {
        var bdRef = LibraryClass.firebaseDB?.reference
        bdRef = id?.let { bdRef?.child("users")?.child(it) }!!
        bdRef.child("id_local").setValue(id_local)
    }

    open fun saveNomeEmpresaInFirebase(): Task<Void> {
        var bdRef = LibraryClass.firebaseDB?.reference
        bdRef = id?.let { bdRef?.child("users")?.child(it) }!!
        return bdRef.child("nome_empresa").setValue(nome_empresa)
    }

    open fun saveNomeInFirebase(): Task<Void> {
        var bdRef = LibraryClass.firebaseDB!!.reference
        bdRef = bdRef!!.child("users")!!.child(id!!)

        return bdRef.child("nome").setValue(nome)
    }

    open fun saveImageProfileIdInFirebase(): Task<Void> {
        var bdRef = LibraryClass.firebaseDB!!.reference
        bdRef = bdRef!!.child("users")!!.child(id!!)

        return bdRef.child("profile_image_id").setValue(profile_image_id)
    }

    open fun saveNameAndEmailSP(ctx: Context) {
        val sp: SharedPreferences = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        sp.edit().putString(NOME, nome).apply()
        sp.edit().putString(EMAIL, email).apply()
    }

    open fun restaureNameSP(ctx: Context) {
        val sp: SharedPreferences = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        nome = sp.getString(NOME, "")
    }

    open fun deleteNameSP(ctx: Context) {
        val sp: SharedPreferences = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        sp.edit().putString(NOME, "").apply()
    }

    open fun saveTipoSP(ctx: Context) {
        val sp: SharedPreferences = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        sp.edit().putString(TIPO, this.tipo).apply()
    }

    open fun restaureTipoSP(ctx: Context) {
        val sp: SharedPreferences = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        tipo = sp.getString(TIPO, "")
    }

    open fun haveNameAndEmailEqualSP(ctx: Context, email: String): Boolean {
        var have = false
        val sp: SharedPreferences = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val nome = sp.getString(NOME, "")
        val emailSP = sp.getString(EMAIL, "")
        if (nome != null && nome !== "" && email == emailSP) {
            have = true
        }
        return have
    }

    @Throws(NoSuchAlgorithmException::class)
    open fun gerarCryptSenha() {
        senha = senha?.let { CryptWithMD5.gerarMD5Hash(it) }
    }

    open fun saveIdSP(ctx: Context, id: String?) {
        val sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        sp.edit().putString(ID, id).apply()
    }
}