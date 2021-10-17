package com.empreendapp.collev.adapters.recycler

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
        //var llDataHora: LinearLayout? = null

        init {
            this.tvColeta = viewItem.findViewById<TextView>(R.id.tvColetaEtapa);
            //this.llDataHora = viewItem.findViewById<LinearLayout>(R.id.llDataHora);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent?.context).inflate(R.layout.item_notificacao, parent, false)
        return ViewHolder(viewItem as ConstraintLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var coleta = notificacoes[position]
        holder.tvColeta?.setText("Sua coleta do dia 12/07 foi finalizada. " + coleta.id_coleta)

        holder.viewItem?.setOnLongClickListener{
            //holder.llDataHora?.visibility = View.GONE;
            true
        }


    }

    override fun getItemCount(): Int {
        return notificacoes.size
    }

}
