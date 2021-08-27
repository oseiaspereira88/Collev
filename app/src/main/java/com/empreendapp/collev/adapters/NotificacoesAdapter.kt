package com.empreendapp.collev.adapters

import android.content.Context
import android.os.Handler
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import com.empreendapp.collev.R
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.empreendapp.collev.model.Notificacao
import java.util.ArrayList

class NotificacoesAdapter(var ctx: Context, var notificacoes: ArrayList<Notificacao>) : RecyclerView.Adapter<NotificacoesAdapter.ViewHolder>() {

    class ViewHolder(var viewItem: ConstraintLayout) : RecyclerView.ViewHolder(viewItem){
        var tvColeta: TextView? = null
        var llDataHora: LinearLayout? = null

        init {
            this.tvColeta = viewItem.findViewById<TextView>(R.id.tvNotificacao);
            this.llDataHora = viewItem.findViewById<LinearLayout>(R.id.llDataHora);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent?.context).inflate(R.layout.item_notificacao, parent, false)
        return ViewHolder(viewItem as ConstraintLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var coleta = notificacoes[position]
        holder.tvColeta?.setText("A empresa HotDoguinhos solicitou a coleta " + coleta.id_coleta)

        holder.viewItem?.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
            when (motionEvent.action){
                MotionEvent.ACTION_DOWN -> {
                    holder.llDataHora?.visibility = View.VISIBLE;
                    YoYo.with(Techniques.SlideInRight).duration(500).repeat(0).playOn(holder.llDataHora)
                }
                MotionEvent.ACTION_UP -> {
                    YoYo.with(Techniques.SlideOutRight).duration(1600).repeat(0).playOn(holder.llDataHora)
                    val handler2 = Handler()
                    val r2 = Runnable {
                        holder.llDataHora?.visibility = View.GONE;
                    }
                    handler2.postDelayed(r2, 1600)
                }
                MotionEvent.ACTION_CANCEL -> {
                    YoYo.with(Techniques.SlideOutRight).duration(2600).repeat(0).playOn(holder.llDataHora)
                    val handler2 = Handler()
                    val r2 = Runnable {
                        holder.llDataHora?.visibility = View.GONE;
                    }
                    handler2.postDelayed(r2, 2600)
                }
            }
            return@OnTouchListener true
        })

        holder.viewItem?.setOnLongClickListener{
            holder.llDataHora?.visibility = View.GONE;
            true
        }


    }

    override fun getItemCount(): Int {
        return notificacoes.size
    }

}
