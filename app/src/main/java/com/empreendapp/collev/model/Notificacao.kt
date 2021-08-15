package com.empreendapp.collev.model

import java.util.*

class Notificacao {
    var id_notificacao: Int? = null
    var id_notificado: Int? = null
    var id_coleta: Int? = null
    var mensagem: String? = null
    var foiLido: Boolean? = null
    var dataSolicitacao: Date? = null
    var dataConclusao: Date? = null

    constructor(
        id_notificacao: Int?,
        id_notificado: Int?,
        id_coleta: Int?,
        mensagem: String?,
        foiLido: Boolean?,
        dataSolicitacao: Date?,
        dataConclusao: Date?
    ) {
        this.id_notificacao = id_notificacao
        this.id_notificado = id_notificado
        this.id_coleta = id_coleta
        this.mensagem = mensagem
        this.foiLido = foiLido
        this.dataSolicitacao = dataSolicitacao
        this.dataConclusao = dataConclusao
    }

    constructor(id_notificacao: Int?){
        this.id_notificacao = id_notificacao
    }


}