package com.empreendapp.collev.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.empreendapp.collev.util.LibraryClass.PREF

class DefaultFunctions {

    companion object {
        fun alert(txtAlert: String, delay: Int, ctx: Context) {
            if (delay == 1) {
                Toast.makeText(ctx, txtAlert, Toast.LENGTH_SHORT).show()
            } else if (delay == 2) {
                Toast.makeText(ctx, txtAlert, Toast.LENGTH_LONG).show()
            }
        }

//        fun existeColetaSolicitada(ctx: Context, idUser: String): Boolean {
//            val sp: SharedPreferences = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
//            return sp.contains("coleta-solicitada-" + idUser)
//        }
//
//        fun getColetaSolicitadaId(ctx: Context, idUser: String): String? {
//            val sp: SharedPreferences = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
//            return sp.getString("coleta-solicitada-" + idUser, "")
//        }
    }
}