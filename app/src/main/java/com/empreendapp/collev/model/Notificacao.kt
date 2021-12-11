package com.empreendapp.collev.model

import android.content.Context
import com.empreendapp.collev.util.DefaultFunctions.Companion.alert
import com.empreendapp.collev.util.LibraryClass
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import java.util.*

class Notificacao {
    var id: String? = null
    var notificado: String? = null
    var mensagem: String? = null
    var tipo: String? = null //solicitacao, agendamento, conclusao
    var foiLido: Boolean? = null
    var data: Date? = null

    constructor()

    //maker("msg").toUser("notificado").withType("tipo").apply(context)

    fun maker(mensagem: String): Notificacao {
        this.mensagem = mensagem
        return this
    }

    fun toUser(notificado: String): Notificacao {
        this.notificado = notificado
        return this
    }

    fun withType(tipo: String): Notificacao {
        this.tipo = tipo
        return this
    }

    fun apply(ctx: Context): Task<DataSnapshot> {
        this.foiLido = false
        this.data = Date()
        return this.generateId()
    }

    fun generateId(): Task<DataSnapshot> {
        return LibraryClass.firebaseDB!!.reference!!
            .child("notificacoes")!!.push().get()
    }

    fun saveInFirebase(): Task<Void> {
        var bdRefNotificacao = LibraryClass.firebaseDB!!.reference
        bdRefNotificacao = bdRefNotificacao!!.child("notificacoes")!!.child(id!!)
        this.id = null
        return bdRefNotificacao.setValue(this)
    }
}