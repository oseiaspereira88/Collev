package com.empreendapp.collev.model

import android.content.Context
import android.content.SharedPreferences;
import com.empreendapp.collev.util.LibraryClass;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.empreendapp.collev.util.CryptWithMD5
import java.security.NoSuchAlgorithmException


@JsonIgnoreProperties(*arrayOf("id", "senha"))
open class User{
    private val TOKEN = "com.empreendapp.collev.models.User.TOKEN"
    private val ID = "com.empreendapp.collev.models.User.ID"
    private val NOME = "com.empreendapp.collev.models.User.NOME"
    private val EMAIL = "com.empreendapp.collev.models.User.EMAIL"
    private var PREF = "com.empreendapp.collev.PREF"

    var id: String? = null
    var nome: String? = null
    var email: String? = null
    var senha: String? = null
    var endereco: String? = null
    var localidade: Local? = null
    var nome_empresa: String? = null

    open fun saveInFirebase() {
        var bdRef = LibraryClass.getFirebaseDB().reference
        bdRef = id?.let { bdRef.child("users").child(it) }!!
        this.id = null
        bdRef.setValue(this)
    }

    open fun saveEnderecoInFirebase() {
        var bdRef = LibraryClass.getFirebaseDB().reference
        bdRef = id?.let { bdRef.child("users").child(it) }!!
        bdRef.setValue(endereco)
    }

    open fun saveLocalidadeInFirebase() {
        var bdRef = LibraryClass.getFirebaseDB().reference
        bdRef = id?.let { bdRef.child("users").child(it) }!!
        bdRef.setValue(localidade)
    }

    open fun saveNomeEmpresaInFirebase() {
            var bdRef = LibraryClass.getFirebaseDB().reference
            bdRef = id?.let { bdRef.child("users").child(it) }!!
            bdRef.setValue(nome_empresa)
        }

    open fun saveNameAndEmailSP(ctx: Context) {
        val sp: SharedPreferences = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        sp.edit().putString(NOME, nome).apply()
        sp.edit().putString(EMAIL, email).apply()
    }

    open fun restaureNameSP(ctx: Context) {
        val sp: SharedPreferences = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val nome = sp.getString(NOME, "")
        this.nome = nome
    }

    open fun deleteNameSP(ctx: Context) {
        val sp: SharedPreferences = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        sp.edit().putString(NOME, "").apply()
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
        senha = CryptWithMD5.gerarMD5Hash(senha)
    }

    open fun saveIdSP(ctx: Context, id: String?) {
        val sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        sp.edit().putString(ID, id).apply()
    }
}