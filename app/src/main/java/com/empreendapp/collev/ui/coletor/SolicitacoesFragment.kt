package com.empreendapp.collev.ui.coletor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.empreendapp.collev.R
import com.empreendapp.collev.adapters.ColetasAdapter
import com.empreendapp.collev.model.Coleta

class SolicitacoesFragment : Fragment() {
    private var rvSolicitacoes : RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_solicitacoes, container, false)
        intViews(rootView);
        return rootView
    }

    private fun intViews(rootView: View) {
        var adapter = ColetasAdapter(getAllSolicitacoes())
        rvSolicitacoes = rootView.findViewById<RecyclerView>(R.id.rv_list_solicitacoes)
        rvSolicitacoes?.layoutManager = LinearLayoutManager(context)
        rvSolicitacoes?.itemAnimator = DefaultItemAnimator()
        rvSolicitacoes?.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun getAllSolicitacoes(): ArrayList<Coleta> {
        var coletas = ArrayList<Coleta>()

        for(i in 1..17){
            coletas.add(Coleta(i))
        }

        return coletas
    }
}