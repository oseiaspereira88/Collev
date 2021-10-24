package com.empreendapp.collev.model

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.empreendapp.collev.ui.colaborador.ColaboradorFragment
import com.empreendapp.collev.util.DefaultFunctions.Companion.alert
import com.empreendapp.collev.util.LibraryClass
import com.google.android.gms.tasks.Task
import java.util.*
import kotlin.collections.ArrayList

class Coleta {
    var id: String? = null
    var solicitante: String? = null
    var coletor: String? = null
    var local: String? = null
    var status: String? = null
    var diasPossiveis: ArrayList<Int>? = null
    var periodoIn: String? = null
    var periodoOut: String? = null
    var ativo: Boolean? = false
    var ativo_solicitante: String? = null

    constructor()

    fun generateIdAndSave(ctx: Context, spName: String, vf: ColaboradorFragment) {
        LibraryClass.firebaseDB!!.reference!!.child("coletas")!!.push()
            .get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                id = task.result.key.toString()
                var sp: SharedPreferences = ctx.getSharedPreferences(spName, MODE_PRIVATE)
                sp.edit().putString("ColetaSolicitada", "" + this.id).apply()
                saveInFirebase()
                vf.verColeta()
            } else{
                alert("Erro ao salvar a coleta no db!", 2, ctx)
            }
        }
    }

    fun saveInFirebase(): Task<Void> {
        var bdRef = LibraryClass.firebaseDB?.reference
        bdRef = id?.let { bdRef?.child("coletas")?.child(it) }!!
        this.id = null
        return bdRef.setValue(this)
    }

    fun ativar(): Coleta{
        this.ativo = true
        this.ativo_solicitante = ativo.toString() + "_" + solicitante
        return this
    }

    fun desativar(): Coleta{
        this.ativo = false
        this.ativo_solicitante = ativo.toString() + "_" + solicitante
        return this
    }
}