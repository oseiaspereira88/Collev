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

    constructor()
}