package com.empreendapp.collev.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.material.snackbar.Snackbar
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

class DefaultFunctions {

    companion object {
        fun alert(msg: String, delay: Int, ctx: Context) {
            if (delay == 1) {
                Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
            } else if (delay == 2) {
                Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show()
            }
        }

        fun alertSnack(msg: String, delay: Int, ctxView: View) {
            if (delay == 1) {
                Snackbar.make(ctxView, msg, Snackbar.LENGTH_SHORT).show()
            } else if (delay == 2) {
                Snackbar.make(ctxView, msg, Snackbar.LENGTH_LONG).show()
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

        fun animateTutorialPulse(view: View){
            view.visibility = View.VISIBLE

            YoYo.with(Techniques.Pulse)
                .duration(900)
                .repeat(6).playOn(view)

            val handler = Handler()
            val r = Runnable {
                view.visibility = View.INVISIBLE
            }

            handler.postDelayed(r, 5400)
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

        fun whenAlertSleep(r: Runnable){
            val h = Handler()
            h.postDelayed(r, 3200)
        }

         fun encrypt(text: String): String {
            val md = MessageDigest.getInstance("MD5")
            val hashInBytes = md.digest(text.toByteArray(StandardCharsets.UTF_8))
            val sb = StringBuilder()
            for (b in hashInBytes) {
                sb.append(kotlin.String.format("%02x", b))
            }
            return sb.toString()
        }
    }
}