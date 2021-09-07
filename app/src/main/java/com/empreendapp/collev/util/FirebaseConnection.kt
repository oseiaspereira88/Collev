package com.empreendapp.collev.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser
import com.empreendapp.collev.util.FirebaseConnection

object FirebaseConnection {
    private var firebaseAuth: FirebaseAuth? = null
    private var authStateListener: AuthStateListener? = null
    var firebaseUser: FirebaseUser? = null
        private set

    fun getFirebaseAuth(): FirebaseAuth? {
        if (firebaseAuth == null) {
            initFirebase()
        }
        return firebaseAuth
    }

    private fun initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance()
        authStateListener = AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                firebaseUser = user
            }
        }
        firebaseAuth!!.addAuthStateListener(authStateListener!!)
    }

    fun logout() {
        firebaseAuth!!.signOut()
    }
}