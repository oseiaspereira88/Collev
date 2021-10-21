package com.empreendapp.collev.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
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

        fun animateButton(view: View) {
            YoYo.with(Techniques.Pulse)
                .duration(300)
                .repeat(0).playOn(view)
        }

        fun animateInputError(view: View) {
            YoYo.with(Techniques.Swing)
                .duration(300)
                .repeat(0).playOn(view)
        }

        fun checkExit(activity: Activity) {
            val builder = AlertDialog.Builder(activity)
            builder.setMessage("Deseja realmente sair?")
                .setCancelable(false)
                .setPositiveButton("Sim") { dialog, id ->  activity.finish() }
                .setNegativeButton("Não") { dialog, id -> dialog.cancel() }
            val alert = builder.create()
            alert.show()
        }
    }
}