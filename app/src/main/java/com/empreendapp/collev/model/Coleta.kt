package com.empreendapp.collev.model

import java.util.*

class Coleta {
    var id_coleta: Int? = null
    var id_solicitante: Int? = null
    var id_coletor: Int? = null
    var local: String? = null
    var status //solicitada, agendada, confirmada e atendida; ou cancelada;
            : String? = null
    var feedbacks: ArrayList<Feedback>? = null
    var dataSolicitacao: Date? = null
    var dataConclusao: Date? = null

    constructor()

    constructor(
        id_coleta: Int?,
        id_solicitante: Int?,
        id_coletor: Int?,
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

    constructor(id_coleta: Int?) {this.id_coleta = id_coleta}
}