package com.empreendapp.collev.ui.voluntario

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.empreendapp.collev.R
import com.empreendapp.collev.model.Coleta
import com.google.firebase.auth.FirebaseAuth.getInstance
import android.app.TimePickerDialog;
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


open class VoluntarioFragment : Fragment() {
    private var imgSolicitarColeta: ImageView? = null
    private var clSolicitarColeta: ConstraintLayout? = null
    private var clChecks: ConstraintLayout? = null
    private var tvInfoCreate: TextView? = null
    private var tvDataColeta: TextView? = null
    private var tvDiaColeta: TextView? = null
    private var imgMapBG: ImageView? = null
    private var cvCapa: CardView? = null
    private var tvCapaInfo: TextView? = null
    private var clColeta: ConstraintLayout? = null
    private var tvSpacer: TextView? = null
    private var itensSemana: ArrayList<ImageView>? = null
    private var itemSemanaSelectedPosition: Int? = null
    private var isStateCreate: Boolean? = false
    private var isStateCreated: Boolean? = false
    private var isCanceled: Boolean? = false
    private lateinit var imgCancelForm: ImageView
    private lateinit var mContext: Context
    private lateinit var time: ImageView
    private lateinit var horaInicial: TextView
    private lateinit var horaFinal: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_voluntario, container, false)
        initViews(rootView);

        return rootView
    }

    private fun initViews(rootView: View?) {
        if (rootView != null) {
            imgSolicitarColeta = rootView.findViewById(R.id.imgSolicitarColeta)
            clSolicitarColeta = rootView.findViewById(R.id.clSolicitarColeta)
            clChecks = rootView.findViewById(R.id.clChecks)
            tvInfoCreate = rootView.findViewById(R.id.tvInfoCreate)
            tvDataColeta = rootView.findViewById(R.id.tvColetaEtapa)
            tvDiaColeta = rootView.findViewById(R.id.tvColetaRecipiente)
            imgMapBG = rootView.findViewById(R.id.imgMapBG)
            cvCapa = rootView.findViewById(R.id.cvCapa)
            tvCapaInfo = rootView.findViewById(R.id.tvCapaInfo)
            clColeta = rootView.findViewById(R.id.clColeta)
            tvSpacer = rootView.findViewById(R.id.tvSpacer)
            imgCancelForm = rootView.findViewById(R.id.imgCancelForm)
            time = rootView.findViewById(R.id.image_time)
            horaInicial = rootView.findViewById(R.id.text_horas_inicial)
            horaFinal = rootView.findViewById(R.id.text_horas_final)

            itensSemana = ArrayList()
            val imgResources = arrayOf(
                R.id.imgS1,
                R.id.imgS2,
                R.id.imgS3,
                R.id.imgS4,
                R.id.imgS5,
                R.id.imgS6,
                R.id.imgS7
            )

            imgResources.forEachIndexed() { i, id ->
                itensSemana?.add(rootView.findViewById(id))
                itensSemana?.last()?.setOnClickListener(View.OnClickListener {
                    cleanSelecaoSemana()
                    val item: ImageView = it as ImageView
                    if (itemSemanaSelectedPosition == i) {
                        item.setImageResource(R.drawable.button_cycle_orange)
                        itemSemanaSelectedPosition = null
                    } else {
                        item.setImageResource(R.drawable.button_cycle_blue)
                        itemSemanaSelectedPosition = i
                    }
                })
            }

            //adding all listeners
            imgSolicitarColeta?.setOnClickListener(View.OnClickListener {
                toggleStateCreate()
            })
            //clear all listeners
            imgCancelForm.setOnClickListener {
                cancelarFormulario()
            }
            //TimePicker
            time.setOnClickListener {
                val cal = Calendar.getInstance()
                val timeSetListener =
                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        cal.set(Calendar.MINUTE, minute)

                        val cal1 = Calendar.getInstance()
                        val timeSetListener1 =
                            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                                cal1.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                cal1.set(Calendar.MINUTE, minute)
                                horaFinal.text = SimpleDateFormat("HH:mm").format(cal1.time)
                            }
                        TimePickerDialog(
                            context,
                            timeSetListener1,
                            cal1.get(Calendar.HOUR),
                            cal1.get(Calendar.MINUTE),
                            true
                        ).show()


                        horaInicial.text = SimpleDateFormat("HH:mm").format(cal.time)
                    }
                TimePickerDialog(
                    context,
                    timeSetListener,
                    cal.get(Calendar.HOUR),
                    cal.get(Calendar.MINUTE),
                    true
                ).show()

            }
        }
    }


    private fun cleanSelecaoSemana() {
        itensSemana?.forEach { item ->
            item.setImageResource(R.drawable.button_cycle_orange)
        }
    }

    private fun isCamposValided(): Boolean {
        if (itemSemanaSelectedPosition != null) {
            return true
        }
        return false
    }

    private fun toggleStateCreate() {
        if (isCanceled == true) {
            YoYo.with(Techniques.RotateIn).duration(800).repeat(0).playOn(clSolicitarColeta)
            cvCapa?.visibility = View.VISIBLE
            YoYo.with(Techniques.Pulse).duration(400).repeat(0).playOn(clColeta)
            YoYo.with(Techniques.Pulse).duration(400).repeat(0).playOn(tvCapaInfo)
            tvInfoCreate?.visibility = View.GONE
            tvSpacer?.visibility = View.GONE

            val handler = Handler()
            val r = Runnable {
                imgSolicitarColeta?.setImageResource(R.drawable.icon_plus)
                imgSolicitarColeta?.layoutParams?.width = 116
                imgSolicitarColeta?.layoutParams?.height = 116
            }
            handler.postDelayed(r, 600)
            isStateCreate = false
            isStateCreated = false
            isCanceled = false

        } else if (isStateCreate == false && isStateCreated == false) {
            YoYo.with(Techniques.RotateIn).duration(800).repeat(0).playOn(clSolicitarColeta)
            cvCapa?.visibility = View.GONE
            YoYo.with(Techniques.Pulse).duration(400).repeat(0).playOn(clColeta)
            tvInfoCreate?.visibility = View.VISIBLE
            tvSpacer?.visibility = View.INVISIBLE

            val handler = Handler()
            val r = Runnable {
                imgSolicitarColeta?.setImageResource(R.drawable.icon_check_white)
                imgSolicitarColeta?.layoutParams?.width = 66
                imgSolicitarColeta?.layoutParams?.height = 66
            }
            handler.postDelayed(r, 600)
            isStateCreate = true

        } else if (isStateCreate == true) {
            // varifica os campos e caso validados as opções, cria nova coleta com status "Solicitada"
            if (isCamposValided()) {
                var coleta = Coleta()
                coleta?.id_solicitante = getInstance()?.currentUser?.uid;
                //coleta?.id_solicitante = getInstance()?.currentUser?.uid;
                // pegar id_coletor do usuário voluntário

                tvSpacer?.visibility = View.GONE
                clChecks?.visibility = View.VISIBLE
                tvInfoCreate?.visibility = View.GONE
                YoYo.with(Techniques.Pulse).duration(400).repeat(0).playOn(clColeta)
                YoYo.with(Techniques.RotateOut).duration(400).repeat(0).playOn(clSolicitarColeta)
                isStateCreated = true
            }
        }
    }

    private fun cancelarFormulario() {
        isCanceled = true
        toggleStateCreate()
        Toast.makeText(context, "A solicitação foi cancelada!", Toast.LENGTH_SHORT)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }


}