package com.empreendapp.collev.ui.system.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.empreendapp.collev.R
import com.empreendapp.collev.adapters.recycler.NotificacoesAdapter
import com.empreendapp.collev.model.Notificacao

class NotificacoesFragment : Fragment() {
    private var rvNotificacoes : RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_notificacoes, container, false)
        intViews(rootView);
        return rootView
    }

    private fun intViews(rootView: View) {
        var adapter = NotificacoesAdapter(rootView.context, getAllNotificacoes())
        rvNotificacoes = rootView.findViewById<RecyclerView>(R.id.rv_locais)
        rvNotificacoes?.layoutManager = LinearLayoutManager(context)
        rvNotificacoes?.itemAnimator = DefaultItemAnimator()
        rvNotificacoes?.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun getAllNotificacoes(): ArrayList<Notificacao> {
        var notificacoes = ArrayList<Notificacao>()

        for(i in 1..10){
            notificacoes.add(Notificacao(i))
        }

        return notificacoes
    }
}