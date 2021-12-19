package com.empreendapp.collev.adapters.recycler

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.empreendapp.collev.R
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.empreendapp.collev.model.Notificacao
import com.empreendapp.collev.util.DefaultFunctions.Companion.animateButton
import java.util.ArrayList

class NotificacoesAdapter(var ctx: Context, var notificacoes: ArrayList<Notificacao>) : RecyclerView.Adapter<NotificacoesAdapter.ViewHolder>() {

    class ViewHolder(var viewItem: ConstraintLayout) : RecyclerView.ViewHolder(viewItem){
        var tvNotificacaoTitulo: TextView? = null
        var tvNotificacaoTempo: TextView? = null

        init {
            this.tvNotificacaoTitulo = viewItem.findViewById(R.id.tvNotificacaoTitulo)
            this.tvNotificacaoTempo = viewItem.findViewById(R.id.tvNotificacaoTempo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent?.context).inflate(R.layout.item_notificacao, parent, false)
        return ViewHolder(viewItem as ConstraintLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var notificacao = notificacoes[position]
        holder.tvNotificacaoTitulo?.setText(notificacao.mensagem)

        holder.viewItem?.setOnClickListener{
            animateButton(it)
        }


    }

    override fun getItemCount(): Int {
        return notificacoes.size
    }

}
