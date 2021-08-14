package com.empreendapp.collev.adapters

import android.content.Context
import android.os.Handler
import com.empreendapp.collev.model.Coleta
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import com.empreendapp.collev.R
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import java.util.ArrayList

class Coletas2Adapter(var ctx: Context, var coletas: ArrayList<Coleta>) : RecyclerView.Adapter<Coletas2Adapter.ViewHolder>() {

    class ViewHolder(var viewItem: ConstraintLayout) : RecyclerView.ViewHolder(viewItem){
        var tvColeta: TextView? = null
        var tvNumber: TextView? = null
        var llDataHora: LinearLayout? = null

        init {
            this.tvColeta = viewItem.findViewById<TextView>(R.id.tvColeta);
            this.tvNumber = viewItem.findViewById<TextView>(R.id.tvNumber);
            this.llDataHora = viewItem.findViewById<LinearLayout>(R.id.llDataHora);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent?.context).inflate(R.layout.coleta_item, parent, false)
        return ViewHolder(viewItem as ConstraintLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var coleta = coletas[position]
        holder.tvColeta?.setText("A empresa tal solicitou a coleta " + coleta.id_coleta)
        holder.tvNumber?.setText(if(position<9) "0" + (position +1) else "" + (position +1))

        holder.viewItem?.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
            when (motionEvent.action){
                MotionEvent.ACTION_DOWN -> {
                    holder.llDataHora?.visibility = View.VISIBLE;
                    YoYo.with(Techniques.SlideInRight).duration(500).repeat(0).playOn(holder.llDataHora)
                    YoYo.with(Techniques.RubberBand).duration(300).repeat(0).playOn(holder.viewItem)
                }
                MotionEvent.ACTION_UP -> {
                    YoYo.with(Techniques.SlideOutRight).duration(1600).repeat(0).playOn(holder.llDataHora)
                    val handler2 = Handler()
                    val r2 = Runnable {
                        holder.llDataHora?.visibility = View.GONE;
                    }
                    handler2.postDelayed(r2, 1600)
                }
                MotionEvent.ACTION_CANCEL -> {
                    YoYo.with(Techniques.SlideOutRight).duration(2600).repeat(0).playOn(holder.llDataHora)
                    val handler2 = Handler()
                    val r2 = Runnable {
                        holder.llDataHora?.visibility = View.GONE;
                    }
                    handler2.postDelayed(r2, 2600)
                }
            }
            return@OnTouchListener true
        })

        holder.viewItem?.setOnLongClickListener{
            holder.llDataHora?.visibility = View.GONE;
            true
        }


    }

    override fun getItemCount(): Int {
        return coletas.size
    }

}
