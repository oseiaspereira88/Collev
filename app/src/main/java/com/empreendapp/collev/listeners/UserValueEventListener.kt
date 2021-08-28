package com.empreendapp.collev.listeners

import android.util.Log
import com.empreendapp.collev.model.User
import com.google.firebase.FirebaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class UserValueEventListener : ValueEventListener {
    var user: User? = null

    override fun onDataChange(dataSnapshot: DataSnapshot) {
        for (d in dataSnapshot.children) {
            val u = d.getValue(
                User::class.java
            )
            user = u;
        }
    }

    override fun onCancelled(error: DatabaseError) {
        TODO("Not yet implemented")
    }
}