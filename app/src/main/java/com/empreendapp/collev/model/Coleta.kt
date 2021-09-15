package com.empreendapp.collev.model

import java.util.*

class Coleta {
    var id_coleta: String? = null
    var id_solicitante: String? = null
    var id_coletor: String? = null
    var local: String? = null
    var status //solicitada, agendada, confirmada e atendida; ou cancelada;
            : String? = null
    var feedbacks: ArrayList<Feedback>? = null
    var dataSolicitacao: Date? = null
    var dataConclusao: Date? = null

    constructor()

    constructor(
        id_coleta: String?,
        id_solicitante: String?,
        id_coletor: String?,
        local: String?,
        status: String?,
        feedbacks: ArrayList<Feedback>?,
        dataSolicitacao: Date?,
        dataConclusao: Date?
    ) {
        this.id_coleta = id_coleta
        this.id_solicitante = id_solicitante
        this.id_coletor = id_coletor
        this.local = local
        this.status = status
        this.feedbacks = feedbacks
        this.dataSolicitacao = dataSolicitacao
        this.dataConclusao = dataConclusao
    }

    //constructor(id_coleta: Int?) {this.id_coleta = id_coleta}
}