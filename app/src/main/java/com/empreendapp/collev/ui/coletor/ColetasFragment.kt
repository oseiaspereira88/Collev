package com.empreendapp.collev.ui.coletor

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.empreendapp.collev.R
import com.empreendapp.collev.adapters.recycler.ColetasAdapter
import com.empreendapp.collev.util.ColetaStatus.*
import com.empreendapp.collev.model.Coleta
import com.empreendapp.collev.util.ColetaStatus.Companion.AGENDADA
import com.empreendapp.collev.util.ColetaStatus.Companion.ATENDIDA
import com.empreendapp.collev.util.ColetaStatus.Companion.SOLICITADA
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
    private var tvEmptySolicitacoes: TextView? = null
    private var tvEmptyAgenda: TextView? = null
    private var tvEmptyHistorico: TextView? = null
    private var solicitacoesAdapter: ColetasAdapter? = null
    private var agendaAdapter: ColetasAdapter? = null
    private var historicoAdapter: ColetasAdapter? = null

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

        tvEmptySolicitacoes = rootView.findViewById(R.id.tvEmptySolicitacoes)
        tvEmptyAgenda = rootView.findViewById(R.id.tvEmptyAgenda)
        tvEmptyHistorico = rootView.findViewById(R.id.tvEmptyHistorico)

        rvSolicitacoes?.layoutManager = LinearLayoutManager(context)
        rvAgenda?.layoutManager = LinearLayoutManager(context)
        rvHistorico?.layoutManager = LinearLayoutManager(context)

        rvSolicitacoes?.itemAnimator = DefaultItemAnimator()
        rvAgenda?.itemAnimator = DefaultItemAnimator()
        rvHistorico?.itemAnimator = DefaultItemAnimator()

        rvSolicitacoes?.setHasFixedSize(true)
        rvAgenda?.setHasFixedSize(true)
        rvHistorico?.setHasFixedSize(true)

        initAdapters()
        setChildEventListener()
    }

    private fun initAdapters() {
        listSolicitacoes = ArrayList()
        listAgenda = ArrayList()
        listHistorico = ArrayList()

        solicitacoesAdapter = ColetasAdapter(requireActivity(), listSolicitacoes!!, this)
        agendaAdapter = ColetasAdapter(requireActivity(), listAgenda!!, this)
        historicoAdapter = ColetasAdapter(requireActivity(), listHistorico!!, this)

        rvSolicitacoes!!.adapter = solicitacoesAdapter
        rvAgenda!!.adapter = agendaAdapter
        rvHistorico!!.adapter = historicoAdapter
    }

    private fun initFirebase(){
        database = LibraryClass.firebaseDB!!.reference
        auth = FirebaseAuth.getInstance()
    }

    private fun setChildEventListener(){
        database = database?.child("coletas")
            ?.orderByChild("solicitante")
            ?.equalTo(auth!!.currentUser!!.uid)!!.ref

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("DEBUG", "onChildAdded -> Key: ${snapshot.key} - value: ${snapshot.value}")
                snapshot.getValue(Coleta::class.java)?.let { coleta ->
                    coleta.id = snapshot.key
                    if(coleta.ativo!!){
                        when(coleta.status){
                            SOLICITADA -> {
                                listSolicitacoes!!.add(coleta)
                                updateList(1, listSolicitacoes!!, solicitacoesAdapter!!)
                            }
                            AGENDADA -> {
                                listAgenda!!.add(coleta)
                                updateList(2, listAgenda!!, agendaAdapter!!)
                            }
                            ATENDIDA -> {
                                listHistorico!!.add(coleta)
                                updateList(3, listHistorico!!, historicoAdapter!!)
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
                        AGENDADA -> {
                            listSolicitacoes!!.removeIf { it.id == coleta.id }
                            listAgenda!!.add(coleta)
                            updateList(1, listSolicitacoes!!, solicitacoesAdapter!!)
                            updateList(2, listAgenda!!, agendaAdapter!!)
                        }
                        ATENDIDA -> {
                            listAgenda!!.removeIf { it.id == coleta.id }
                            listHistorico!!.add(coleta)
                            updateList(2, listAgenda!!, agendaAdapter!!)
                            updateList(3, listHistorico!!, historicoAdapter!!)
                        }
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.d("DEBUG", "onChildRemoved: -> Key: ${snapshot.key} - value: ${snapshot.value}")
                snapshot.getValue(Coleta::class.java)?.let { coleta ->
                    if(coleta.ativo!!){
                        when(coleta.status){
                            SOLICITADA -> {
                                listSolicitacoes!!.removeIf { it.id == snapshot.key }
                                updateList(1, listSolicitacoes!!, solicitacoesAdapter!!)
                            }
                            AGENDADA -> {
                                listAgenda!!.removeIf { it.id == snapshot.key }
                                updateList(2, listAgenda!!, agendaAdapter!!)
                            }
                            ATENDIDA -> {
                                listHistorico!!.removeIf { it.id == snapshot.key }
                                updateList(3, listHistorico!!, historicoAdapter!!)
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
            }
        }
        database?.addChildEventListener(childEventListener)
    }

    private fun updateList(listId: Int, list: ArrayList<Coleta>, adapter: ColetasAdapter){
        adapter.coletas = list
        adapter.notifyDataSetChanged()
        checkVisibleList(listId, list)
    }

    public fun resetList(listId: Int){
        when(listId){
            1 -> updateList(1, listSolicitacoes!!, solicitacoesAdapter!!)
            2 -> updateList(2, listAgenda!!, agendaAdapter!!)
            3 -> updateList(3, listHistorico!!, historicoAdapter!!)
        }
    }

    private fun checkVisibleList(listId: Int,  list: ArrayList<Coleta>) {
        when(listId){
            1 -> {
                if(list.isEmpty()){
                    tvEmptySolicitacoes!!.visibility = View.VISIBLE
                    rvSolicitacoes!!.visibility = View.GONE
                } else{
                    tvEmptySolicitacoes!!.visibility = View.GONE
                    rvSolicitacoes!!.visibility = View.VISIBLE
                }
            }
            2 -> {
                if(list.isEmpty()){
                    tvEmptyAgenda!!.visibility = View.VISIBLE
                    rvAgenda!!.visibility = View.GONE
                } else{
                    tvEmptyAgenda!!.visibility = View.GONE
                    rvAgenda!!.visibility = View.VISIBLE
                }
            }
            3 -> {
                if(list.isEmpty()){
                    tvEmptyHistorico!!.visibility = View.VISIBLE
                    rvHistorico!!.visibility = View.GONE
                } else{
                    tvEmptyHistorico!!.visibility = View.GONE
                    rvHistorico!!.visibility = View.VISIBLE
                }
            }
        }
    }
}