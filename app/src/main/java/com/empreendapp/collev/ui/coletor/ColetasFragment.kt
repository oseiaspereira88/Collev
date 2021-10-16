package com.empreendapp.collev.ui.coletor

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.empreendapp.collev.R
import com.empreendapp.collev.adapters.recycler.ColetasAdapter
import com.empreendapp.collev.enums.ColetaStatus.*
import com.empreendapp.collev.model.Coleta
import com.empreendapp.collev.util.LibraryClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference

class ColetasFragment : Fragment() {
    private var database: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    private var rvSolicitacoes : RecyclerView? = null
    private var rvAgenda : RecyclerView? = null
    private var rvHistorico : RecyclerView? = null
    private var listSolicitacoes: ArrayList<Coleta>? = null
    private var listAgenda: ArrayList<Coleta>? = null
    private var listHistorico: ArrayList<Coleta>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_coletas, container, false)
        initFirebase()
        initViews(rootView)
        return rootView
    }

    private fun initViews(rootView: View) {
        rvSolicitacoes = rootView.findViewById(R.id.rvSolicitacoes)
        rvAgenda = rootView.findViewById(R.id.rvAgenda)
        rvHistorico = rootView.findViewById(R.id.rvHistorico)

        rvSolicitacoes?.layoutManager = LinearLayoutManager(context)
        rvAgenda?.layoutManager = LinearLayoutManager(context)
        rvHistorico?.layoutManager = LinearLayoutManager(context)

        rvSolicitacoes?.itemAnimator = DefaultItemAnimator()
        rvAgenda?.itemAnimator = DefaultItemAnimator()
        rvHistorico?.itemAnimator = DefaultItemAnimator()

        rvSolicitacoes?.setHasFixedSize(true)
        rvAgenda?.setHasFixedSize(true)
        rvHistorico?.setHasFixedSize(true)

        setChildEventListener()
    }

    private fun initFirebase(){
        database = LibraryClass.firebaseDB!!.reference
        auth = FirebaseAuth.getInstance()
    }

    private fun setChildEventListener(){
        database = database?.child("coletas")
            ?.orderByChild("solicitante")
            ?.equalTo(auth!!.currentUser!!.uid)!!.ref

        listSolicitacoes = ArrayList()
        listAgenda = ArrayList()
        listHistorico = ArrayList()

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("DEBUG", "onChildAdded -> Key: ${snapshot.key} - value: ${snapshot.value}")
                snapshot.getValue(Coleta::class.java)?.let { coleta ->
                    coleta.id = snapshot.key
                    if(coleta.ativo!!){
                        when(coleta.status){
                            SOLICITADA.name -> {
                                listSolicitacoes!!.add(coleta)
                                updateList(1, listSolicitacoes!!, rvSolicitacoes!!)
                            }
                            AGENDADA.name -> {
                                listAgenda!!.add(coleta)
                                updateList(2, listAgenda!!, rvAgenda!!)
                            }
                            ATENDIDA.name -> {
                                listHistorico!!.add(coleta)
                                updateList(3, listHistorico!!, rvHistorico!!)
                            }
                        }
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("DEBUG", "onChildChanged -> Key: ${snapshot.key} - value: ${snapshot.value}")
                snapshot.getValue(Coleta::class.java)?.let { coleta ->
                    coleta.id = snapshot.key
                    when(coleta.status){
                        AGENDADA.name -> {
                            listSolicitacoes!!.removeIf { it.id == coleta.id }
                            listAgenda!!.add(coleta)
                            updateList(2, listSolicitacoes!!, rvSolicitacoes!!)
                            updateList(2, listAgenda!!, rvAgenda!!)
                        }
                        ATENDIDA.name -> {
                            listAgenda!!.removeIf { it.id == coleta.id }
                            listHistorico!!.add(coleta)
                            updateList(3, listAgenda!!, rvAgenda!!)
                            updateList(3, listHistorico!!, rvHistorico!!)
                        }
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.d("DEBUG", "onChildRemoved: -> Key: ${snapshot.key} - value: ${snapshot.value}")
                snapshot.getValue(Coleta::class.java)?.let { coleta ->
                    if(coleta.ativo!!){
                        when(coleta.status){
                            SOLICITADA.name -> {
                                listSolicitacoes!!.removeIf { it.id == snapshot.key }
                                updateList(1, listSolicitacoes!!, rvSolicitacoes!!)
                            }
                            AGENDADA.name -> {
                                listAgenda!!.removeIf { it.id == snapshot.key }
                                updateList(2, listAgenda!!, rvAgenda!!)
                            }
                            ATENDIDA.name -> {
                                listHistorico!!.removeIf { it.id == snapshot.key }
                                updateList(3, listHistorico!!, rvHistorico!!)
                            }
                        }
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("DEBUG", "onChildMoved: -> Key: ${snapshot.key} - value: ${snapshot.value}")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("W", "postColetas:onCancelled", error.toException())
                Toast.makeText(
                    context, "Falha ao carregar as coletas.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        database?.addChildEventListener(childEventListener)
    }

    private fun updateList(listId: Int, list: ArrayList<Coleta>, rv: RecyclerView){
        var adapter = ColetasAdapter(requireContext(), list, listId)
        rv!!.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}