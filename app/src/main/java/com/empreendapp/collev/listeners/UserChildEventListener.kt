package com.empreendapp.collev.listeners

import android.util.Log
import com.empreendapp.collev.model.User
import com.google.firebase.FirebaseError
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError


class UserChildEventListener() : ChildEventListener {

    override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {

        val u = dataSnapshot.getValue(
            User::class.java
        )
        Log.i("log", "ADDED")
        Log.i("log", "Name: " + u!!.nome)
        Log.i("log", "Email: " + u!!.email)
    }

    override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
        val u = dataSnapshot.getValue(
            User::class.java
        )
        Log.i("log", "CHANGED")
        Log.i("log", "Name: " + u!!.nome)
        Log.i("log", "Email: " + u!!.email)
    }

    override fun onChildRemoved(dataSnapshot: DataSnapshot) {
        val u = dataSnapshot.getValue(
            User::class.java
        )
        Log.i("log", "REMOVED")
        Log.i("log", "Name: " + u!!.nome)
        Log.i("log", "Email: " + u!!.email)
    }

    override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
        TODO("Not yet implemented")
    }

    override fun onCancelled(error: DatabaseError) {
        TODO("Not yet implemented")
    }
}