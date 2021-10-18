package com.empreendapp.collev.adapters.recycler

import android.app.Activity
import com.empreendapp.collev.model.Coleta
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.ImageView
import com.empreendapp.collev.R
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.empreendapp.collev.enums.ColetaStatus.*
import com.empreendapp.collev.ui.system.dialogs.ColetaDialog
import java.util.ArrayList

class ColetasAdapter(var act: Activity, var coletas: ArrayList<Coleta>) :
    RecyclerView.Adapter<ColetasAdapter.ViewHolder>() {

    class ViewHolder(var viewItem: ConstraintLayout) : RecyclerView.ViewHolder(viewItem) {
        var tvTituloColeta: TextView? = null
        var tvSubtituloColeta: TextView? = null
        var imgColeta: ImageView? = null

        init {
            this.tvTituloColeta = viewItem.findViewById(R.id.tvTituloColeta)
            this.tvSubtituloColeta = viewItem.findViewById(R.id.tvSubtituloColeta)
            this.imgColeta = viewItem.findViewById(R.id.imgColeta)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_coleta, parent, false) as ConstraintLayout
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var coleta = coletas[position]

        when(coleta.status){
            SOLICITADA.name -> {
                holder.tvTituloColeta!!.text = "Fulano solicitou uma coleta"
                holder.tvSubtituloColeta!!.text = "Um recipiente de 50 litros está cheio"
                holder.imgColeta!!.setImageResource(R.drawable.icon_oil_05)
            }
            AGENDADA.name -> {
                holder.tvTituloColeta!!.text = "Você agendou a coleta da empresa tal" //nome da empresa ou do colaborador
                holder.tvSubtituloColeta!!.text = "Leve um recipiente de 50 litros"
                holder.imgColeta!!.setImageResource(R.drawable.icon_history)
            }
            ATENDIDA.name -> {
                holder.tvTituloColeta!!.text = "Coleta na empresa tal concluida!"
                holder.tvSubtituloColeta!!.text = "Realizada no dia 21/11/2021"
                holder.imgColeta!!.setImageResource(R.drawable.icon_check_01)
            }
        }

        holder.viewItem.setOnClickListener{
            animateButton(holder.viewItem)
            var fragmentManager = (act as FragmentActivity).supportFragmentManager
            ColetaDialog().show(fragmentManager, "ColetaDialog")
        }

    }

    private fun animateButton(viewItem: ConstraintLayout) {
        YoYo.with(Techniques.Pulse).duration(300).repeat(0).playOn(viewItem)
    }

    override fun getItemCount(): Int {
        return coletas.size
    }

}
