package com.empreendapp.collev.model

import android.os.Handler
import com.empreendapp.collev.util.LibraryClass
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference

class Colaborador: User(){
    var recipiente: String? = null

    open fun saveRecipienteInFirebase() {
        var bdRef = LibraryClass.firebaseDB?.reference
        bdRef = id?.let { bdRef?.child("users")?.child(it) }!!
        bdRef.child("recipiente").setValue(recipiente)
    }

    fun getCurrentColaborador(database: DatabaseReference, auth: FirebaseAuth): Task<DataSnapshot>? {
        return database!!.child("users").child(auth!!.uid.toString()).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val colaborador = task.result.getValue(Colaborador::class.java)
                    setColaborador(colaborador!!)
                } else {
                    try {
                        task.exception!!
                    } catch (e: FirebaseNetworkException) {
                        val h = Handler()
                        val r = Runnable {
                            getCurrentUser(database, auth)
                        }
                        h.postDelayed(r, 2000)
                    }
                }
            }

    }

    fun setColaborador(colaborador: Colaborador){
        recipiente = colaborador.recipiente
        super.setUser(colaborador)
    }
}