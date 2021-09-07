package com.empreendapp.collev.model

import android.content.Context
import android.content.SharedPreferences;
import com.empreendapp.collev.util.LibraryClass;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.empreendapp.collev.util.CryptWithMD5
import java.security.NoSuchAlgorithmException


@JsonIgnoreProperties(*arrayOf("id", "senha"))
open class User {
    private val TOKEN = "com.empreendapp.collev.models.User.TOKEN"
    private val ID = "com.empreendapp.collev.models.User.ID"
    private val NOME = "com.empreendapp.collev.models.User.NOME"
    private val EMAIL = "com.empreendapp.collev.models.User.EMAIL"
    private val TIPO = "com.empreendapp.collev.models.User.TIPO"
    private var PREF = "com.empreendapp.collev.PREF"

    var id: String? = null
    var nome: String? = null
    var email: String? = null
    var senha: String? = null
    var tipo: String? = null
    var endereco: String? = null
    var id_local: String? = null
    var nome_empresa: String? = null

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

    open fun saveNomeEmpresaInFirebase() {
        var bdRef = LibraryClass.firebaseDB?.reference
        bdRef = id?.let { bdRef?.child("users")?.child(it) }!!
        bdRef.child("nome_empresa").setValue(nome_empresa)
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

    open fun saveTipoSP(ctx: Context) {
        val sp: SharedPreferences = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        sp.edit().putString(TIPO, this.tipo).apply()
    }

    open fun restaureTipoSP(ctx: Context) {
        val sp: SharedPreferences = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val tipo = sp.getString(TIPO, "")
        this.tipo = tipo
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