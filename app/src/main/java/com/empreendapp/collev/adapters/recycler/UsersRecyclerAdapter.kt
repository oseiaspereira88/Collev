package com.empreendapp.collev.adapters.recycler

import android.content.Context
import android.os.Handler
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import com.empreendapp.collev.R
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.empreendapp.collev.model.Local
import com.empreendapp.collev.model.User
import com.google.firebase.auth.FirebaseAuth
import java.util.ArrayList

class UsersRecyclerAdapter(
    var ctx: Context,
    var users: ArrayList<User>,
    var locais: ArrayList<Local>
) : RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder>() {
    class ViewHolder(var viewItem: ConstraintLayout) : RecyclerView.ViewHolder(viewItem) {
        var tvUserName: TextView? = null
        var tvUserType: TextView? = null
        var cvUser: CardView? = null
        var cvUserOptions: CardView? = null
        var sLocais: Spinner? = null
        var imgConfirmar: ImageView? = null
        var imgExcluirUser: ImageView? = null

        init {
            this.tvUserName = viewItem.findViewById(R.id.tvUserName)
            this.tvUserType = viewItem.findViewById(R.id.tvUserType)
            this.cvUser = viewItem.findViewById(R.id.cvUser)
            this.cvUserOptions = viewItem.findViewById(R.id.cvUserOptions)
            this.sLocais = viewItem.findViewById(R.id.sLocais)
            this.imgConfirmar = viewItem.findViewById(R.id.imgConfirmar)
            this.imgExcluirUser = viewItem.findViewById(R.id.imgExcluirUser)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_user, parent, false) as ConstraintLayout
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var user = users[position]
        holder.tvUserName?.text = user.nome
        holder.tvUserType?.text = user.tipo

        holder.viewItem.setOnClickListener {
            if (holder.cvUser!!.visibility == View.VISIBLE) {
                YoYo.with(Techniques.Pulse).duration(700).repeat(0).playOn(holder.viewItem)
            }
        }

        holder.viewItem.setOnLongClickListener {
            if (holder.cvUserOptions!!.visibility == View.GONE) {
                rotateIn(holder)
            } else {
                rotateOut(holder)
            }
            true
        }

        var locaisNameList = Array(locais.size) { String() }
        locais.forEachIndexed { index, local ->
            local.nome?.let { locaisNameList.set(index, local.nome!!) }
        }

        val adapter: ArrayAdapter<*> =
            ArrayAdapter(ctx, android.R.layout.simple_spinner_item, locaisNameList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.sLocais!!.adapter = adapter

        holder.imgConfirmar?.setOnClickListener {
            val userLocal = locais.filter { local -> local.id == users.get(position).id_local }[0]
            if (userLocal.nome != holder.sLocais!!.selectedItem.toString()) {
                val selectedLocal = locais.filter { local -> local.nome == holder.sLocais!!.selectedItem.toString() }[0]
                users.get(position).id_local = selectedLocal.id
                users.get(position).saveIdLocalInFirebase()
            }
        }

        holder.imgExcluirUser?.setOnClickListener{
            FirebaseAuth.getInstance()
        }
    }

    private fun rotateIn(holder: ViewHolder) {
        YoYo.with(Techniques.FlipOutX).duration(600).repeat(0).playOn(holder.cvUser)
        val h = Handler()
        val r = Runnable {
            holder.cvUser!!.visibility = View.GONE
            holder.cvUserOptions!!.visibility = View.VISIBLE
            YoYo.with(Techniques.FlipInX).duration(600).repeat(0).playOn(holder.cvUserOptions)
        }
        h.postDelayed(r, 500)
    }

    private fun rotateOut(holder: ViewHolder) {
        YoYo.with(Techniques.FlipOutX).duration(500).repeat(0).playOn(holder.cvUserOptions)
        val h = Handler()
        val r = Runnable {
            holder.cvUserOptions!!.visibility = View.GONE
            holder.cvUser!!.visibility = View.VISIBLE
            YoYo.with(Techniques.FlipInX).duration(500).repeat(0).playOn(holder.cvUser)
        }
        h.postDelayed(r, 500)
    }


    override fun getItemCount(): Int {
        return users.size
    }
}

