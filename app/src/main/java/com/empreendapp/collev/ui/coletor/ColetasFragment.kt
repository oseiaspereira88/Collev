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
import com.empreendapp.collev.adapters.Coletas2Adapter
import com.empreendapp.collev.adapters.ColetasAdapter
import com.empreendapp.collev.interfaces.RecyclerViewClickInterface
import com.empreendapp.collev.model.Coleta

class ColetasFragment : Fragment(), RecyclerViewClickInterface {
    private var rvSolicitacoes : RecyclerView? = null
    private var rvAgenda : RecyclerView? = null
    private var rvHistorico : RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_coletas, container, false)
        intViews(rootView);
        return rootView
    }

    private fun intViews(rootView: View) {
        var adapter1 = ColetasAdapter(rootView.context, getAllDados(3), 1)
        var adapter2 = ColetasAdapter(rootView.context, getAllDados(5), 2)
        var adapter3 = ColetasAdapter(rootView.context, getAllDados(7), 3)

        rvSolicitacoes = rootView.findViewById<RecyclerView>(R.id.rv_list_solicitacoes)
        rvAgenda = rootView.findViewById<RecyclerView>(R.id.rv_list_agenda)
        rvHistorico = rootView.findViewById<RecyclerView>(R.id.rv_list_historico)

        rvSolicitacoes?.layoutManager = LinearLayoutManager(context)
        rvAgenda?.layoutManager = LinearLayoutManager(context)
        rvHistorico?.layoutManager = LinearLayoutManager(context)

        rvSolicitacoes?.itemAnimator = DefaultItemAnimator()
        rvAgenda?.itemAnimator = DefaultItemAnimator()
        rvHistorico?.itemAnimator = DefaultItemAnimator()

        rvSolicitacoes?.adapter = adapter1
        rvAgenda?.adapter = adapter2
        rvHistorico?.adapter = adapter3

        adapter1.notifyDataSetChanged()
        adapter2.notifyDataSetChanged()
        adapter3.notifyDataSetChanged()
    }

    private fun getAllDados(n: Int): ArrayList<Coleta> {
        var coletas = ArrayList<Coleta>()

        for(i in 1..n){
            coletas.add(Coleta(i))
        }

        return coletas
    }
}