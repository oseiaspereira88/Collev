package com.empreendapp.collev.adapters.recycler

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.empreendapp.collev.R
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.empreendapp.collev.model.Local
import com.empreendapp.collev.model.User
import com.empreendapp.collev.util.LibraryClass
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import java.util.ArrayList

class LocaisRecyclerAdapter(var ctx: Context, var locais: ArrayList<Local>) :
    RecyclerView.Adapter<LocaisRecyclerAdapter.ViewHolder>() {
    var usersByLocal: ArrayList<ArrayList<User>>? = ArrayList()

    class ViewHolder(var viewItem: ConstraintLayout) : RecyclerView.ViewHolder(viewItem) {
        var tvLocalNome: TextView? = null
        var tvLocalEmpty: TextView? = null
        var rvListUsers: RecyclerView? = null
        var tvNumberUsersInLocal: TextView? = null
        var adapter: UsersRecyclerAdapter? = null

        init {
            this.tvLocalNome = viewItem.findViewById(R.id.tvLocalNome)
            this.tvLocalEmpty = viewItem.findViewById(R.id.tvLocalEmpty)
            this.rvListUsers = viewItem.findViewById(R.id.rvUsersList)
            this.tvNumberUsersInLocal = viewItem.findViewById(R.id.tvNumberUsersInLocal)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_local, parent, false) as ConstraintLayout
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var local = locais[position]
        holder.tvLocalNome?.text = local.nome
        holder.viewItem.setOnClickListener(getOnClickEmptyLocal(holder))
        local.id?.let { getUserEventListenerByLocal(it, holder, position) }
    }

    private fun getOnClickEmptyLocal(holder: ViewHolder): View.OnClickListener? {
        return View.OnClickListener {
            if (holder.tvLocalEmpty?.visibility == View.GONE) {
                holder.tvLocalEmpty?.visibility = View.VISIBLE
                holder.rvListUsers?.visibility = View.GONE
                YoYo.with(Techniques.FadeIn).duration(700).repeat(0).playOn(holder.tvLocalEmpty)
            } else {
                holder.tvLocalEmpty?.visibility = View.GONE
            }
        }
    }

    private fun getOnClickNotEmptyLocal(holder: ViewHolder): View.OnClickListener? {
        return View.OnClickListener {
            if (holder.rvListUsers?.visibility == View.GONE) {
                holder.rvListUsers?.visibility = View.VISIBLE
                YoYo.with(Techniques.FadeIn).duration(700).repeat(0).playOn(holder.rvListUsers)
            } else {
                holder.rvListUsers?.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return locais.size
    }

    private fun getUserEventListenerByLocal(id: String, holder: ViewHolder, position: Int) {
        usersByLocal?.add(position, ArrayList())
        holder.rvListUsers!!.layoutManager = LinearLayoutManager(ctx)
        holder.rvListUsers!!.itemAnimator = DefaultItemAnimator()
        holder.rvListUsers!!.setHasFixedSize(true)

        var ref = LibraryClass.firebaseDB?.reference
        var database = ref?.child("users")?.orderByChild("id_local")?.equalTo(id)

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("DEBUG", "onChildAdded: -> Key: ${snapshot.key} - value: ${snapshot.value}")
                snapshot.getValue(User::class.java)?.let { user ->
                    user.id = snapshot.key
                    usersByLocal?.get(position)?.add(user)
                }
                holder.viewItem.setOnClickListener(getOnClickNotEmptyLocal(holder))
                holder.tvLocalEmpty?.visibility = View.GONE
                holder.rvListUsers?.visibility = View.VISIBLE

                updateList(holder, position)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("DEBUG", "onChildChanged: -> Key: ${snapshot.key} - value: ${snapshot.value}")
                snapshot.getValue(User::class.java)?.let { userChanged ->
                    userChanged.id = snapshot.key
                    if (userChanged.id_local == id) {
                        usersByLocal?.get(position)?.forEachIndexed { index, oldUser ->
                            if (oldUser.id == userChanged.id) usersByLocal?.get(position)!![index] =
                                userChanged
                        }
                        updateList(holder, position)
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.d("DEBUG", "onChildRemoved: -> Key: ${snapshot.key} - value: ${snapshot.value}")
                snapshot.getValue(User::class.java)?.let { user ->
                    usersByLocal?.get(position)!!.removeIf { it.id == snapshot.key }
                }
                if(usersByLocal?.get(position)!!.isEmpty()){
                    holder.viewItem.setOnClickListener(getOnClickEmptyLocal(holder))
                    holder.rvListUsers?.visibility = View.GONE
                    holder.tvLocalEmpty?.visibility = View.VISIBLE
                }
                updateList(holder, position)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("DEBUG", "onChildMoved: -> Key: ${snapshot.key} - value: ${snapshot.value}")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("W", "postLocals:onCancelled", error.toException())
                Toast.makeText(ctx, "Failed to load locals.", Toast.LENGTH_SHORT).show()
            }

        }
        database?.addChildEventListener(childEventListener)
    }

    private fun updateList(holder: ViewHolder, position: Int) {
        usersByLocal?.get(position)?.let { itens ->
            holder.adapter = ctx?.let { ctx -> UsersRecyclerAdapter(ctx, itens) }
            holder.tvNumberUsersInLocal?.text = itens.size.toString()
            holder.rvListUsers!!.adapter = holder.adapter
        }
    }
}

