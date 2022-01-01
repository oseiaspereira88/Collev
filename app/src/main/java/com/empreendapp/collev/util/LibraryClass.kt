package com.empreendapp.collev.util

import android.app.Application
import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import com.empreendapp.collev.util.sharedpreferences.ConfigSP.Companion.PREF

object LibraryClass : Application() {
    private var firebaseBD: FirebaseDatabase? = null

    val firebaseDB: FirebaseDatabase?
        get() {
            if (firebaseBD == null) {
                firebaseBD = FirebaseDatabase.getInstance()
            }
            return firebaseBD
        }

    fun saveSP(ctx: Context, key: String?, value: String?) {
        val sp = ctx.getSharedPreferences(PREF, MODE_PRIVATE)
        sp.edit().putString(key, value).apply()
    }

    fun getSP(ctx: Context, key: String?): String? {
        val sp =
            ctx.getSharedPreferences(PREF, MODE_PRIVATE)
        return sp.getString(key, "")
    }
}