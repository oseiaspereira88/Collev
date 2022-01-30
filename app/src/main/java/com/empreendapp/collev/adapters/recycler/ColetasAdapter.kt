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
import com.empreendapp.collev.model.User
import com.empreendapp.collev.ui.coletor.ColetasFragment
import com.empreendapp.collev.util.enums.ColetaStatus.Companion.AGENDADA
import com.empreendapp.collev.util.enums.ColetaStatus.Companion.ATENDIDA
import com.empreendapp.collev.util.enums.ColetaStatus.Companion.SOLICITADA
import com.empreendapp.collev.util.DefaultFunctions.Companion.alert
import com.empreendapp.collev.util.DefaultFunctions.Companion.animateButton
import com.empreendapp.collev.util.DefaultFunctions.Companion.animateInputError
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ColetasAdapter(var act: Activity, var coletas: ArrayList<Coleta>, var fragment: ColetasFragment, var usuario: User?) :
    RecyclerView.Adapter<ColetasAdapter.ViewHolder>() {

    class ViewHolder(var viewItem: ConstraintLayout) : RecyclerView.ViewHolder(viewItem) {
        var tvTituloColeta: TextView? = null
        var tvSubtituloColeta: TextView? = null
        var imgColeta: ImageView? = null
        var dialog: AlertDialog? = null

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
                holder.tvTituloColeta!!.text = "${coleta.empresaColaboradora} solicitou coleta"
                holder.tvSubtituloColeta!!.text = "O recipiente de ${coleta.volumeRecipiente} litros está cheio"
                holder.imgColeta!!.setImageResource(R.drawable.icon_oil_05)

                holder.viewItem.setOnClickListener {
                    animateButton(holder.viewItem)

                    if (holder.dialog == null) {
                        val dialogBuilder = AlertDialog.Builder(act)
                        val dialogView = act.layoutInflater.inflate(R.layout.dialog_coleta_solicitada, null)
                        dialogBuilder.setView(dialogView)
                        holder.dialog = dialogBuilder.create()
                        holder.dialog!!.getWindow()?.setBackgroundDrawableResource(R.drawable.transparent_bg)

                        val fragmentDialogMap1 = dialogView!!.findViewById<View>(R.id.fragmentDialogMap1)
                        fragmentDialogMap1.visibility = View.GONE

                        val llMapaVisibilityToggle = dialogView!!.findViewById<LinearLayout>(R.id.llMapaVisibilityToggle1)
                        val tvMapaVisibilityToggle = llMapaVisibilityToggle.findViewById<TextView>(R.id.tvMapaVisibilityToggle1)

                        llMapaVisibilityToggle.setOnClickListener {
                            if(fragmentDialogMap1.visibility == View.GONE){
                                fragmentDialogMap1.visibility = View.VISIBLE
                                tvMapaVisibilityToggle.text = "Ocultar o Mapa"
                            } else{
                                fragmentDialogMap1.visibility = View.GONE
                                tvMapaVisibilityToggle.text = "Ver no Mapa"
                            }
                        }

                        val daysList = getDaysList(coleta.diasPossiveis)
                        val adapter = ArrayAdapter(act, android.R.layout.simple_spinner_item, daysList)
                        adapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                        val spinner = dialogView!!.findViewById<Spinner>(R.id.spDias)
                        spinner!!.adapter = adapter
                        holder.dialog!!.show()

                        val tvPeriodo = dialogView!!.findViewById<TextView>(R.id.tvInfoAgendarColeta)
                        tvPeriodo.text = "Informe quando será realizada a coleta e o horário entre as ${coleta.periodoIn} e ${coleta.periodoOut} horas"

                        val tvDialogEmpresaName = dialogView!!.findViewById<TextView>(R.id.tvDialogSolicitadaEmpresa)
                        tvDialogEmpresaName.text = "Da " + coleta.empresaColaboradora

                        val llHorario = dialogView!!.findViewById<LinearLayout>(R.id.llHorario)
                        val tvHorario = dialogView!!.findViewById<TextView>(R.id.tvHorario)

                        llHorario.setOnClickListener{
                            val cal1 = Calendar.getInstance()
                            val finalTimeSetListener1 =
                                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                                    cal1.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                    cal1.set(Calendar.MINUTE, minute)

                                    val horaMarcada = SimpleDateFormat("HH:mm").format(cal1.time).toString().replace(":", "").toInt()
                                    if (horaMarcada >= coleta.periodoIn!!.toString().replace(":", "").toInt()
                                        && horaMarcada <= coleta.periodoOut.toString().replace(":", "").toInt()) {

                                        tvHorario.text = SimpleDateFormat("HH:mm").format(cal1.time)
                                    } else {
                                        animateInputError(dialogView)
                                        alert("Selecione um horário entre as ${coleta.periodoIn}h e as ${coleta.periodoOut}h!", 2, act)
                                    }
                                }
                            val tpFinalDialog = TimePickerDialog(
                                act,
                                finalTimeSetListener1,
                                cal1.get(Calendar.HOUR),
                                cal1.get(Calendar.MINUTE),
                                true
                            )
                            tpFinalDialog.setTitle("Qual será o horário?")
                            tpFinalDialog.show()
                        }

                        val imgCancelDialog = dialogView!!.findViewById<ImageView>(R.id.imgCancelDialog)
                        imgCancelDialog.setOnClickListener {
                            holder.dialog!!.dismiss()
                        }

                        val tvAgendar = dialogView!!.findViewById<TextView>(R.id.tvAgendar)
                        tvAgendar.setOnClickListener {
                            if(!tvHorario.text.equals("Selecione")){
                                dialogView.visibility = View.GONE

                                val builder = AlertDialog.Builder(act)
                                builder.setMessage("Confirma a coleta para a ${spinner.selectedItem} às ${tvHorario.text} horas?")
                                    .setCancelable(false)
                                    .setPositiveButton("Sim") { confirmDialog, id ->
                                        holder.dialog!!.dismiss()

                                        coleta.diaMarcado = spinner.selectedItem.toString()
                                        coleta.horaMarcada = tvHorario.text.toString()
                                        coleta.status = AGENDADA
                                        coleta.coletorName = usuario!!.nome

                                        coleta.saveInFirebase(act).addOnCompleteListener {
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
                                        confirmDialog.dismiss()
                                    }
                                val alert = builder.create()
                                alert.show()
                            } else{
                                alert("Selecione um horário!", 2, act)
                                animateInputError(dialogView)
                            }
                        }
                    } else{
                        holder.dialog!!.show()
                    }
                }
            }
            AGENDADA -> {
                holder.tvTituloColeta!!.text = "Você agendou a coleta da ${coleta.empresaColaboradora}"
                holder.tvSubtituloColeta!!.text = "Leve um recipiente de ${coleta.volumeRecipiente} litros"
                holder.imgColeta!!.setImageResource(R.drawable.icon_history)

                holder.viewItem.setOnClickListener{
                    animateButton(holder.viewItem)

                    if(holder.dialog == null){
                        val dialogBuilder = AlertDialog.Builder(act)
                        val dialogView = act.layoutInflater.inflate(R.layout.dialog_coleta_agendada, null)
                        dialogBuilder.setView(dialogView)
                        holder.dialog = dialogBuilder.create()
                        holder.dialog!!.getWindow()?.setBackgroundDrawableResource(R.drawable.transparent_bg)
                        holder.dialog!!.show()

                        val fragmentDialogMap2 = dialogView!!.findViewById<View>(R.id.fragmentDialogMap2)
                        fragmentDialogMap2.visibility = View.GONE

                        val llMapaVisibilityToggle = dialogView!!.findViewById<LinearLayout>(R.id.llMapaVisibilityToggle2)
                        val tvMapaVisibilityToggle = llMapaVisibilityToggle.findViewById<TextView>(R.id.tvMapaVisibilityToggle2)

                        llMapaVisibilityToggle.setOnClickListener {
                            if(fragmentDialogMap2.visibility == View.GONE){
                                fragmentDialogMap2.visibility = View.VISIBLE
                                tvMapaVisibilityToggle.text = "Ocultar o Mapa"
                            } else{
                                fragmentDialogMap2.visibility = View.GONE
                                tvMapaVisibilityToggle.text = "Encontrar a rota"
                            }
                        }

                        val tvDialogEmpresaName = dialogView!!.findViewById<TextView>(R.id.tvDialogAgendadaEmpresa)
                        tvDialogEmpresaName.text = "Da " + coleta.empresaColaboradora

                        dialogView!!.findViewById<ImageView>(R.id.imgCancelDialog).setOnClickListener {
                            holder.dialog!!.dismiss()
                        }

                        dialogView!!.findViewById<TextView>(R.id.tvNaoConcluirColeta).setOnClickListener {
                            holder.dialog!!.dismiss()
                        }

                        dialogView!!.findViewById<TextView>(R.id.tvConcluirColeta).setOnClickListener {
                            coleta.status = ATENDIDA
                            coleta.dataAtendida = Date()
                            coleta.desativar()
                            coleta.saveInFirebase(act).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    YoYo.with(Techniques.SlideOutRight)
                                        .duration(1000)
                                        .repeat(0).playOn(holder.viewItem)

                                    holder.dialog!!.dismiss()

                                    val h = Handler()
                                    val r = Runnable {
                                        if (coletas.contains(coleta)) coletas.remove(coleta)
                                        fragment.resetList(2)

                                        YoYo.with(Techniques.FadeIn)
                                            .duration(50)
                                            .repeat(0).playOn(holder.viewItem)
                                    }
                                    h.postDelayed(r, 1300)
                                    alert("A coleta foi finalizada ✔️", 2, act)
                                } else {
                                    alert("Coleta não foi finalizada \uD83D\uDE15", 2, act)
                                    alert("Verifique sua conexão com a internet!", 2, act)
                                }
                            }
                        }
                    } else{
                        holder.dialog!!.show()
                    }
                }
            }
            ATENDIDA -> {
                holder.tvTituloColeta!!.text = "Coleta na ${coleta.empresaColaboradora} concluida!"
                holder.tvSubtituloColeta!!.text = "Click para saber mais"
                holder.imgColeta!!.setImageResource(R.drawable.icon_check_01)

                holder.viewItem.setOnClickListener{
                    animateButton(holder.viewItem)

                    val dialogBuilder = AlertDialog.Builder(act)
                    val dialogView = act.layoutInflater.inflate(R.layout.dialog_coleta_atendida, null)
                    dialogBuilder.setView(dialogView)
                    val dialog = dialogBuilder.create()
                    dialog!!.getWindow()?.setBackgroundDrawableResource(R.drawable.transparent_bg)

                    val tvEmpresa = dialogView.findViewById<TextView>(R.id.tvColetaInfoEmpresa)
                    val tvData = dialogView.findViewById<TextView>(R.id.tvColetaInfoData)
                    val tvDia = dialogView.findViewById<TextView>(R.id.tvColetaInfoDia)
                    val tvHora = dialogView.findViewById<TextView>(R.id.tvColetaInfoHora)
                    val tvRecipiente = dialogView.findViewById<TextView>(R.id.tvColetaInfoRecipiente)
                    val imgMap = dialogView.findViewById<ImageView>(R.id.imgMap)

                    imgMap.setOnClickListener {
                        animateButton(it)
                    }

                    tvEmpresa.text = coleta.empresaColaboradora
                    val sdf = SimpleDateFormat("dd/MM/yyyy")
                    tvData.text = sdf.format(coleta.dataAtendida)
                    tvDia.text = coleta.diaMarcado
                    tvHora.text = coleta.horaMarcada
                    tvRecipiente.text = coleta.volumeRecipiente

                    val imgCancelDialog = dialogView.findViewById<ImageView>(R.id.imgCancelDialog)
                    imgCancelDialog.setOnClickListener {
                        dialog.dismiss()
                    }

                    dialog.show()
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
