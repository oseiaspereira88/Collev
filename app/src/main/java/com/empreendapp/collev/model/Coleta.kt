package com.empreendapp.collev.model

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import com.empreendapp.collev.ui.colaborador.ColaboradorFragment
import com.empreendapp.collev.util.ColetaStatus.Companion.AGENDADA
import com.empreendapp.collev.util.ColetaStatus.Companion.ATENDIDA
import com.empreendapp.collev.util.ColetaStatus.Companion.SOLICITADA
import com.empreendapp.collev.util.NotificacaoTipo
import com.empreendapp.collev.util.DefaultFunctions.Companion.alert
import com.empreendapp.collev.util.LibraryClass
import com.google.android.gms.tasks.Task
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
    var diaMarcado: String? = null
    var horaMarcada: String? = null
    var ativo: Boolean? = false
    var ativo_solicitante: String? = null
    var solicitanteName: String? = null
    var coletorName: String? = null
    var empresaColetora: String? = null
    var empresaColaboradora: String? = null
    var volumeRecipiente: String? = null

    constructor()

    fun generateIdAndSave(ctx: Context, spName: String, vf: ColaboradorFragment) {
        LibraryClass.firebaseDB!!.reference!!.child("coletas")!!.push()
            .get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                id = task.result.key.toString()
                var sp: SharedPreferences = ctx.getSharedPreferences(spName, MODE_PRIVATE)
                sp.edit().putString("ColetaSolicitada", "" + this.id).apply()
                saveInFirebase(ctx)
                vf.verColeta()
            } else{
                alert("Erro ao salvar a coleta no db!", 2, ctx)
            }
        }
    }

    fun saveInFirebase(ctx: Context): Task<Void> {
        var bdRef = LibraryClass.firebaseDB!!.reference
        bdRef = bdRef!!.child("coletas")!!.child(id!!)

        this.id = null
        return bdRef.setValue(this).addOnCompleteListener {
            if (it.isSuccessful) {
                val notificacao = Notificacao()

                when (this.status) {
                    SOLICITADA -> {
                        notificacao
                            .maker("A ${empresaColaboradora} solicitou uma coleta")
                            .toUser(coletor!!)
                            .withType(NotificacaoTipo.SOLICITACAO)
                            .apply(ctx)
                            .addOnCompleteListener { task ->
                                if (it.isSuccessful) {
                                    notificacao.id = task.result.key.toString()
                                    notificacao.saveInFirebase()
                                        .addOnCompleteListener {
                                            if (it.isSuccessful) {
                                                Log.i("Notificação", "A notificação foi enviada.")
                                            } else {
                                                Log.i(
                                                    "Notificação",
                                                    "Falha ao enviar a notificação."
                                                )
                                            }
                                        }
                                } else {
                                    alert("Erro ao salvar a coleta no db!", 2, ctx)
                                }
                            }
                    }
                    AGENDADA -> {
                        notificacao.maker("A ${empresaColetora} agendou a coleta")
                            .toUser(solicitante!!)
                            .withType(NotificacaoTipo.AGENDAMENTO)
                            .apply(ctx)
                            .addOnCompleteListener { task ->
                                if (it.isSuccessful) {
                                    notificacao.id = task.result.key.toString()
                                    notificacao.saveInFirebase()
                                        .addOnCompleteListener {
                                            if (it.isSuccessful) {
                                                Log.i("Notificação", "A notificação foi enviada.")
                                            } else {
                                                Log.i(
                                                    "Notificação",
                                                    "Falha ao enviar a notificação."
                                                )
                                            }
                                        }
                                }
                            }
                    }
                    ATENDIDA -> {
                        notificacao.maker("A ${empresaColetora} atendeu a coleta")
                            .toUser(solicitante!!)
                            .withType(NotificacaoTipo.CONCLUSAO)
                            .apply(ctx)
                            .addOnCompleteListener { task ->
                                if (it.isSuccessful) {
                                    notificacao.id = task.result.key.toString()
                                    notificacao.saveInFirebase()
                                        .addOnCompleteListener {
                                            if (it.isSuccessful) {
                                                Log.i("Notificação", "A notificação foi enviada.")
                                            } else {
                                                Log.i(
                                                    "Notificação",
                                                    "Falha ao enviar a notificação."
                                                )
                                            }
                                        }
                                }
                            }
                    }
                }
            }
        }
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