package com.empreendapp.collev.model

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.empreendapp.collev.ui.voluntario.VoluntarioFragment
import com.empreendapp.collev.util.DefaultFunctions.Companion.alert
import com.empreendapp.collev.util.LibraryClass
import java.util.*

class Coleta {
    var id: String? = null
    var solicitante: String? = null
    var coletor: String? = null
    var local: String? = null
    var status //solicitada, agendada e atendida; ou cancelada;
            : String? = null
    var feedbacks: ArrayList<Feedback>? = null
    var diasPossiveis: List<String>? = null
    var periodoIn: String? = null
    var periodoOut: String? = null
    var ativo: Boolean? = false
    var ativo_solicitante: String? = null

    constructor()

    constructor(
        id: String?,
        solicitante: String?,
        coletor: String?,
        local: String?,
        status: String?,
        feedbacks: ArrayList<Feedback>?,
        diasPossiveis: List<String>?,
        periodoIn: String?,
        periodoOut: String?,
        ativo: Boolean?,
        ativo_solicitante: String?
    ) {
        this.id = id
        this.solicitante = solicitante
        this.coletor = coletor
        this.local = local
        this.status = status
        this.feedbacks = feedbacks
        this.diasPossiveis = diasPossiveis
        this.periodoIn = periodoIn
        this.periodoOut = periodoOut
        this.ativo = ativo
        this.ativo_solicitante = ativo_solicitante
    }

    fun generateIdAndSave(ctx: Context, spName: String, vf: VoluntarioFragment) {
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

    fun saveInFirebase() {
        var bdRef = LibraryClass.firebaseDB?.reference
        bdRef = id?.let { bdRef?.child("coletas")?.child(it) }!!
        this.id = null
        bdRef.setValue(this)
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