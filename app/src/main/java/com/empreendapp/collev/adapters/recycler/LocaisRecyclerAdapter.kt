package com.empreendapp.collev.adapters.recycler

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.empreendapp.collev.R
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.empreendapp.collev.model.Local
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class LocaisRecyclerAdapter(options: FirebaseRecyclerOptions<Local>
) : FirebaseRecyclerAdapter<Local, LocaisRecyclerAdapter.ViewHolder>(options) {

    class ViewHolder(viewItem: ConstraintLayout) : RecyclerView.ViewHolder(viewItem){
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Local) {
        holder.tvLocalNome!!.text = model.nome
    }

}
