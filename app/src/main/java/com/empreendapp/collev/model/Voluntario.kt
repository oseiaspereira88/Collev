package com.empreendapp.collev.model

import com.empreendapp.collev.util.LibraryClass

class Voluntario(): User(){
    var recipiente: String? = null

    open fun saveRecipienteInFirebase() {
        var bdRef = LibraryClass.firebaseDB?.reference
        bdRef = id?.let { bdRef?.child("users")?.child(it) }!!
        bdRef.child("recipiente").setValue(recipiente)
    }
}