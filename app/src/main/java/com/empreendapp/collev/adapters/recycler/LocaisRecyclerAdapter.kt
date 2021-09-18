package com.empreendapp.collev.adapters.recycler

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.empreendapp.collev.R
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.empreendapp.collev.model.Local
import java.util.ArrayList

class LocaisRecyclerAdapter(var ctx: Context, var locais: ArrayList<Local>) : RecyclerView.Adapter<LocaisRecyclerAdapter.ViewHolder>() {
    class ViewHolder(var viewItem: ConstraintLayout) : RecyclerView.ViewHolder(viewItem){
        var tvLocalNome: TextView? = null
        var rvListUsers: RecyclerView? = null

        init {
            this.tvLocalNome = viewItem.findViewById(R.id.tvLocalNome)
            this.rvListUsers = viewItem.findViewById(R.id.rvListUsers)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_local, parent, false) as ConstraintLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var local = locais[position]
        holder.tvLocalNome?.setText(local.nome)
        //holder.rvListUsers? =
    }

    override fun getItemCount(): Int {
        return locais.size
    }
}

