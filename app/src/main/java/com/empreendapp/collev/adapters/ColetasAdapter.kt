package com.empreendapp.collev.adapters

import com.empreendapp.collev.model.Coleta
import androidx.recyclerview.widget.RecyclerView
import androidx.cardview.widget.CardView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.empreendapp.collev.R
import android.widget.TextView
import java.util.ArrayList

class ColetasAdapter(var coletas: ArrayList<Coleta>) : RecyclerView.Adapter<ColetasAdapter.ViewHolder>() {

    class ViewHolder(var cardView: CardView) : RecyclerView.ViewHolder(cardView){
        var tvColeta: TextView? = null

        init {
            this.tvColeta = cardView.findViewById<TextView>(R.id.tvColeta);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val card = LayoutInflater.from(parent?.context).inflate(R.layout.coleta_item, parent, false)
        return ViewHolder(card as CardView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var coleta = coletas[position]
        holder.tvColeta?.setText("A empresa tal solicitou a coleta " + coleta.id_coleta)
    }

    override fun getItemCount(): Int {
        return coletas.size
    }

}