package com.empreendapp.collev.ui.system.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.empreendapp.collev.R
import com.empreendapp.collev.adapters.recycler.LocaisRecyclerAdapter
import com.empreendapp.collev.adapters.recycler.NotificacoesAdapter
import com.empreendapp.collev.model.Notificacao
import com.empreendapp.collev.util.LibraryClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import java.util.*
import kotlin.collections.ArrayList

class NotificacoesFragment : Fragment() {
    private var rvNotificacoes: RecyclerView? = null
    private var clAnimNotificacoes: ConstraintLayout? = null
    private var adapter: NotificacoesAdapter? = null
    private var notificacoes: ArrayList<Notificacao>? = null
    private var database: DatabaseReference? = null
    private var uid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_notificacoes, container, false)
        intViews(rootView)
        return rootView
    }

    private fun intViews(rootView: View) {
        rvNotificacoes = rootView.findViewById(R.id.rv_notificacoes)
        clAnimNotificacoes = rootView.findViewById(R.id.clAnimNotificacoes)

        rvNotificacoes?.layoutManager = LinearLayoutManager(context)
        rvNotificacoes?.itemAnimator = DefaultItemAnimator()
        rvNotificacoes!!.setHasFixedSize(true)

        database = LibraryClass.firebaseDB?.reference?.child("notificacoes")
        notificacoes = ArrayList()

        database?.addChildEventListener(getNotificacoesChildEventListener())
    }

    private fun getNotificacoesChildEventListener(): ChildEventListener {
        return  object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("DEBUG", "onChildAdded -> Key: ${snapshot.key} - value: ${snapshot.value}")
                snapshot.getValue(Notificacao::class.java)?.let { notificacao ->
                    notificacao.id = snapshot.key
                    if(notificacao.notificado == FirebaseAuth.getInstance().uid){
                        notificacoes!!.add(notificacao)
                    }
                }
                updateList()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("DEBUG", "onChildChanged -> Key: ${snapshot.key} - value: ${snapshot.value}")
                snapshot.getValue(Notificacao::class.java)?.let { notificacao ->
                    notificacao.id = snapshot.key
                    notificacoes?.forEachIndexed{ index, old -> if(old.id == notificacao.id) notificacoes!![index] = notificacao}
                }
                updateList()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.d("DEBUG", "onChildRemoved: -> Key: ${snapshot.key} - value: ${snapshot.value}")
                snapshot.getValue(Notificacao::class.java)?.let { notificacao ->
                    notificacoes!!.removeIf { it.id == snapshot.key}
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("DEBUG", "onChildMoved: -> Key: ${snapshot.key} - value: ${snapshot.value}")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("W", "postNotificacaos:onCancelled", error.toException())
                Toast.makeText(
                    context, "Failed to load notificacaos.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateList(){
        notificacoes?.let { itens ->
            adapter = context?.let { ctx -> NotificacoesAdapter(ctx, itens) }
            rvNotificacoes!!.adapter = adapter

            if(!itens.isEmpty()){
                clAnimNotificacoes!!.visibility = View.GONE
            }
        }
    }

}