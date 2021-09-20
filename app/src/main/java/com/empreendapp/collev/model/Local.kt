package com.empreendapp.collev.model

import com.empreendapp.collev.util.LibraryClass

class Local {
    var id: String? = null
    var nome: String? = null
    var descricao: String? = null
    var users_number: Int? = null
    var empty: Boolean? = null

    constructor(nome: String?, descricao: String?, users_number: Int, empty: Boolean?) {
        this.nome = nome
        this.descricao = descricao
        this.users_number = users_number
        this.empty = empty
    }

    constructor()

    open fun saveEmptyInFirebase() {
        var bdRef = LibraryClass.firebaseDB?.reference
        bdRef = id?.let { bdRef?.child("locais")?.child(it) }!!
        bdRef.child("empty").setValue(empty)
    }
}