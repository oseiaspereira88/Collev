package com.empreendapp.collev.model

import java.util.*

class Local {
    var id_local: Int? = null
    var nome: String? = null
    var descricao: String? = null

    constructor(nome: String?, descricao: String?) {
        this.nome = nome
        this.descricao = descricao
    }

    constructor()
}