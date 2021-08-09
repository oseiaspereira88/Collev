package com.empreendapp.collev.model

class Feedback {
    var id_feedback: Int? = null
    var id_coleta: Int? = null
    var id_emitente: Int? = null
    var mensagem: String? = null

    constructor(id_feedback: Int?, id_coleta: Int?, id_emitente: Int?, mensagem: String?) {
        this.id_feedback = id_feedback
        this.id_coleta = id_coleta
        this.id_emitente = id_emitente
        this.mensagem = mensagem
    }

    constructor() {}
}