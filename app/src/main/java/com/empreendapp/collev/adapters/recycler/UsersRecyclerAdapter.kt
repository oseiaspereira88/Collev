package com.empreendapp.collev.adapters.recycler

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.empreendapp.collev.R
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.empreendapp.collev.model.User
import java.util.ArrayList

class UsersRecyclerAdapter(var ctx: Context, var users: ArrayList<User>) : RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder>() {
    class ViewHolder(var viewItem: ConstraintLayout) : RecyclerView.ViewHolder(viewItem){
        var rootView: ConstraintLayout? = viewItem
        var tvUserName: TextView? = null
        var tvUserType: TextView? = null

        init {
            this.tvUserName = viewItem.findViewById(R.id.tvUserName)
            this.tvUserType = viewItem.findViewById(R.id.tvUserType)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_user, parent, false) as ConstraintLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var user = users[position]
        holder.tvUserName?.text = user.nome
        holder.tvUserType?.text = user.tipo
    }

    override fun getItemCount(): Int {
        return users.size
    }
}

