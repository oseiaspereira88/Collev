package com.empreendapp.collev.adapters.recycler

import android.app.Activity
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.empreendapp.collev.R
import com.empreendapp.collev.model.Coleta
import com.empreendapp.collev.ui.coletor.ColetasFragment
import com.empreendapp.collev.util.ColetaStatus.Companion.AGENDADA
import com.empreendapp.collev.util.ColetaStatus.Companion.ATENDIDA
import com.empreendapp.collev.util.ColetaStatus.Companion.SOLICITADA
import com.empreendapp.collev.util.DefaultFunctions.Companion.alert
import com.empreendapp.collev.util.DefaultFunctions.Companion.animateButton
import com.empreendapp.collev.util.DefaultFunctions.Companion.animateInputError
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ColetasAdapter(var act: Activity, var coletas: ArrayList<Coleta>, var fragment: ColetasFragment) :
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
            SOLICITADA -> {
                holder.tvTituloColeta!!.text = "Fulano solicitou uma coleta"
                holder.tvSubtituloColeta!!.text = "Um recipiente de 50 litros está cheio"
                holder.imgColeta!!.setImageResource(R.drawable.icon_oil_05)

                holder.viewItem.setOnClickListener{
                    animateButton(holder.viewItem)

                    val dialogBuilder = AlertDialog.Builder(act)
                    val dialogView = act.layoutInflater.inflate(R.layout.dialog_marcacao_coleta, null)
                    dialogBuilder.setView(dialogView)
                    val dialog = dialogBuilder.create()
                    dialog!!.getWindow()?.setBackgroundDrawableResource(R.drawable.transparent_bg)

                    var daysList = getDaysList(coleta.diasPossiveis)
                    var adapter = ArrayAdapter(act, android.R.layout.simple_spinner_item, daysList)
                    adapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    var spinner = dialogView!!.findViewById<Spinner>(R.id.spDias)
                    spinner!!.adapter = adapter
                    dialog.show()

                    var tvPeriodo = dialogView!!.findViewById<TextView>(R.id.tvInfoAgendarColeta)
                    tvPeriodo.text = "Informe quando será realizada a coleta e o horário entre as ${coleta.periodoIn} e ${coleta.periodoOut} horas"

                    var llHorario = dialogView!!.findViewById<LinearLayout>(R.id.llHorario)
                    var tvHorario = dialogView!!.findViewById<TextView>(R.id.tvHorario)

                    llHorario.setOnClickListener{
                        val cal1 = Calendar.getInstance()
                        val finalTimeSetListener1 =
                            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                                cal1.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                cal1.set(Calendar.MINUTE, minute)

                                var horaMarcada = SimpleDateFormat("HH:mm").format(cal1.time).toString().replace(":", "").toInt()
                                if (horaMarcada >= coleta.periodoIn!!.toString().replace(":", "").toInt()
                                    && horaMarcada <= coleta.periodoOut.toString().replace(":", "").toInt()) {

                                    tvHorario.text = SimpleDateFormat("HH:mm").format(cal1.time)
                                } else {
                                    animateInputError(dialogView)
                                    alert("Selecione um horário entre as ${coleta.periodoIn}h e as ${coleta.periodoOut}h!", 2, act)
                                }
                            }
                        var tpFinalDialog = TimePickerDialog(
                            act,
                            finalTimeSetListener1,
                            cal1.get(Calendar.HOUR),
                            cal1.get(Calendar.MINUTE),
                            true
                        )
                        tpFinalDialog.setTitle("Qual será o horário?")
                        tpFinalDialog.show()
                    }

                    var imgCancelDialog = dialogView!!.findViewById<ImageView>(R.id.imgCancelDialog)
                    imgCancelDialog.setOnClickListener {
                        dialog.cancel()
                    }

                    var tvAgendar = dialogView!!.findViewById<TextView>(R.id.tvAgendar)
                    tvAgendar.setOnClickListener {
                        if(!tvHorario.text.equals("Selecione")){
                            dialogView.visibility = View.GONE

                            val builder = AlertDialog.Builder(act)
                            builder.setMessage("Confirma a coleta para a ${spinner.selectedItem} às ${tvHorario.text} horas?")
                                .setCancelable(false)
                                .setPositiveButton("Sim") { confirmDialog, id ->
                                    dialog.cancel()

                                    coleta.diaMarcado = spinner.selectedItem.toString()
                                    coleta.horaMarcada = tvHorario.text.toString()
                                    coleta.status = AGENDADA
                                    coleta.desativar()

                                    coleta.saveInFirebase().addOnCompleteListener {
                                        if(it.isSuccessful){
                                            YoYo.with(Techniques.SlideOutRight)
                                                .duration(1000)
                                                .repeat(0).playOn(holder.viewItem)

                                            val h = Handler()
                                            val r = Runnable {
                                                if(coletas.contains(coleta)) coletas.remove(coleta)
                                                fragment.resetList(1)

                                                YoYo.with(Techniques.FadeIn)
                                                    .duration(50)
                                                    .repeat(0).playOn(holder.viewItem)
                                            }
                                            h.postDelayed(r, 1300)

                                            alert("A coleta foi agendada ✔️", 2, act)
                                        } else{
                                            alert("Coleta não agendada \uD83D\uDE15", 2, act)
                                            alert("Verifique sua conexão com a internet!", 2, act)
                                        }
                                    }
                                }
                                .setNegativeButton("Não") { confirmDialog, id ->
                                    dialogView.visibility = View.VISIBLE
                                    confirmDialog.cancel()
                                }
                            val alert = builder.create()
                            alert.show()
                        } else{
                            alert("Selecione um horário!", 2, act)
                            animateInputError(dialogView)
                        }
                    }
                }
            }
            AGENDADA -> {
                holder.tvTituloColeta!!.text = "Você agendou a coleta da empresa tal" //nome da empresa ou do colaborador
                holder.tvSubtituloColeta!!.text = "Leve um recipiente de 50 litros"
                holder.imgColeta!!.setImageResource(R.drawable.icon_history)

                holder.viewItem.setOnClickListener{
                    animateButton(holder.viewItem)

                    val dialogBuilder = AlertDialog.Builder(act)
                    val dialogView = act.layoutInflater.inflate(R.layout.dialog_finalizar_coleta, null)
                    dialogBuilder.setView(dialogView)
                    val dialog = dialogBuilder.create()
                    dialog!!.getWindow()?.setBackgroundDrawableResource(R.drawable.transparent_bg)
                    dialog.show()

                    dialogView!!.findViewById<ImageView>(R.id.imgCancelDialog).setOnClickListener {
                        dialog.cancel()
                    }

                    dialogView!!.findViewById<TextView>(R.id.tvNaoConcluirColeta).setOnClickListener {
                        dialog.cancel()
                    }

                    dialogView!!.findViewById<TextView>(R.id.tvConcluirColeta).setOnClickListener {
                        coleta.status = ATENDIDA
                        coleta.saveInFirebase().addOnCompleteListener {
                            if(it.isSuccessful){
                                YoYo.with(Techniques.SlideOutRight)
                                    .duration(1000)
                                    .repeat(0).playOn(holder.viewItem)

                                dialog.cancel()

                                val h = Handler()
                                val r = Runnable {
                                    if(coletas.contains(coleta)) coletas.remove(coleta)
                                    fragment.resetList(2)

                                    YoYo.with(Techniques.FadeIn)
                                        .duration(50)
                                        .repeat(0).playOn(holder.viewItem)
                                }
                                h.postDelayed(r, 1300)
                                alert("A coleta foi finalizada ✔️", 2, act)
                            } else{
                                alert("Coleta não dinalizada \uD83D\uDE15", 2, act)
                                alert("Verifique sua conexão com a internet!", 2, act)
                            }
                        }
                    }
                }
            }
            ATENDIDA -> {
                holder.tvTituloColeta!!.text = "Coleta na empresa tal concluida!"
                holder.tvSubtituloColeta!!.text = "Realizada no dia 21/11/2021"
                holder.imgColeta!!.setImageResource(R.drawable.icon_check_01)

                holder.viewItem.setOnClickListener{
                    animateButton(holder.viewItem)

                    val dialogBuilder = AlertDialog.Builder(act)
                    val dialogView = act.layoutInflater.inflate(R.layout.dialog_marcacao_coleta, null)
                    dialogBuilder.setView(dialogView)
                    val dialog = dialogBuilder.create()
                    dialog!!.getWindow()?.setBackgroundDrawableResource(R.drawable.transparent_bg)

                    var daysList = getDaysList(coleta.diasPossiveis)
                    var adapter = ArrayAdapter(act, android.R.layout.simple_spinner_item, daysList)
                    adapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    var spinner = dialogView!!.findViewById<Spinner>(R.id.spDias)
                    spinner!!.adapter = adapter
                    dialog.show()

                    dialogView!!.findViewById<TextView>(R.id.tvPeriodoIn).text = coleta.periodoIn
                    dialogView!!.findViewById<TextView>(R.id.tvPeriodoOut).text = coleta.periodoOut

                    var llHorario = dialogView!!.findViewById<LinearLayout>(R.id.llHorario)
                    var tvHorario = dialogView!!.findViewById<TextView>(R.id.tvHorario)

                    llHorario.setOnClickListener{
                        val cal1 = Calendar.getInstance()
                        val finalTimeSetListener1 =
                            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                                cal1.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                cal1.set(Calendar.MINUTE, minute)

                                var horaMarcada = SimpleDateFormat("HH:mm").format(cal1.time).toString().replace(":", "").toInt()
                                if (horaMarcada >= coleta.periodoIn!!.toString().replace(":", "").toInt()
                                    && horaMarcada <= coleta.periodoOut.toString().replace(":", "").toInt()) {

                                    tvHorario.text = SimpleDateFormat("HH:mm").format(cal1.time)
                                } else {
                                    animateInputError(dialogView)
                                    alert("Selecione um horário entre as ${coleta.periodoIn}h e as ${coleta.periodoOut}h!", 2, act)
                                }
                            }
                        var tpFinalDialog = TimePickerDialog(
                            act,
                            finalTimeSetListener1,
                            cal1.get(Calendar.HOUR),
                            cal1.get(Calendar.MINUTE),
                            true
                        )
                        tpFinalDialog.setTitle("Qual será o horário?")
                        tpFinalDialog.show()
                    }

                    var imgCancelDialog = dialogView!!.findViewById<ImageView>(R.id.imgCancelDialog)
                    imgCancelDialog.setOnClickListener {
                        dialog.cancel()
                    }

                    var tvAgendar = dialogView!!.findViewById<TextView>(R.id.tvAgendar)
                    tvAgendar.setOnClickListener {
                        if(!tvHorario.text.equals("Selecione")){
                            dialogView.visibility = View.GONE

                            val builder = AlertDialog.Builder(act)
                            builder.setMessage("Confirma a coleta para a ${spinner.selectedItem} as ${tvHorario.text} horas?")
                                .setCancelable(false)
                                .setPositiveButton("Sim") { confirmDialog, id ->
                                    dialog.cancel()

                                    coleta.diaMarcado = spinner.selectedItem.toString() + "-feira"
                                    coleta.periodoIn = tvHorario.text.toString()
                                    coleta.status = AGENDADA
                                    coleta.saveInFirebase().addOnCompleteListener {
                                        if(it.isSuccessful){
                                            YoYo.with(Techniques.SlideOutRight)
                                                .duration(1000)
                                                .repeat(0).playOn(holder.viewItem)

                                            val h = Handler()
                                            val r = Runnable {
                                                if(coletas.contains(coleta)) coletas.remove(coleta)
                                                fragment.resetList(1)

                                                YoYo.with(Techniques.FadeIn)
                                                    .duration(50)
                                                    .repeat(0).playOn(holder.viewItem)
                                            }
                                            h.postDelayed(r, 1300)

                                            alert("A coleta foi agendada ✔️", 2, act)
                                        } else{
                                            alert("Coleta não agendada \uD83D\uDE15", 2, act)
                                            alert("Verifique sua conexão com a internet!", 2, act)
                                        }
                                    }
                                }
                                .setNegativeButton("Não") { confirmDialog, id ->
                                    dialogView.visibility = View.VISIBLE
                                    confirmDialog.cancel()
                                }
                            val alert = builder.create()
                            alert.show()
                        } else{
                            alert("Selecione um horário!", 2, act)
                            animateInputError(dialogView)
                        }
                    }
                }
            }
        }
    }

    private fun getDaysList(diasPossiveis: ArrayList<Int>?): Array<out Any> {
        var selectedDays = ArrayList<String>()
        var days = arrayOf("Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado")

        diasPossiveis!!.forEach{
            selectedDays.add(days[it])
        }

        return selectedDays.toArray()
    }

    override fun getItemCount(): Int {
        return coletas.size
    }

}
