package com.empreendapp.collev.ui.voluntario

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.empreendapp.collev.R
import com.empreendapp.collev.model.Coleta
import android.app.TimePickerDialog
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.empreendapp.collev.util.DefaultFunctions.Companion.alert
import com.empreendapp.collev.util.FirebaseConnection
import com.empreendapp.collev.util.LibraryClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


open class VoluntarioFragment : Fragment() {
    private val ID_COLETOR_PRINCIPAL = "cvzFUzdnKgSaB28TsACocqBMzdg2"
    private val VOLUNTARIO_PREF = "com.empreendapp.collev.VOLUNTORIO_PREF"
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
    private var isStateCreate: Boolean = false
    private var isStateCreated: Boolean = false
    private var isCanceled: Boolean = false
    private lateinit var imgCancelForm: ImageView
    private lateinit var time: ImageView
    private lateinit var horaInicial: TextView
    private lateinit var horaFinal: TextView
    private var database: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    private var coletas: ArrayList<Coleta>? = null
    private var nColetas: Int = 0
    private var isPausedActionButton: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_voluntario, container, false)
        initFirebase()
        initViews(rootView)
        checkColetaSolicitada()
        return rootView
    }

    private fun initFirebase() {
        database = LibraryClass.firebaseDB?.reference
        auth = FirebaseConnection.getFirebaseAuth()!!
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

            imgResources.forEachIndexed { i, id ->
                itensSemana?.add(rootView.findViewById(id))
                itensSemana?.last()?.setOnClickListener {
                    cleanSelecaoSemana()
                    val item: ImageView = it as ImageView
                    if (itemSemanaSelectedPosition == i) {
                        item.setImageResource(R.drawable.button_cycle_orange)
                        itemSemanaSelectedPosition = null
                    } else {
                        item.setImageResource(R.drawable.button_cycle_blue)
                        itemSemanaSelectedPosition = i
                    }
                }
            }

            //adding all listeners
            imgSolicitarColeta?.setOnClickListener {
                toggleStateCreate()
            }

            //clear all listeners
            imgCancelForm.setOnClickListener {
                var sp: SharedPreferences =
                    requireContext().getSharedPreferences(VOLUNTARIO_PREF, MODE_PRIVATE)
                if (sp.contains("ColetaSolicitada")) {
                    confirmDeleteColetaSolicitada(sp)
                }
            }

            //TimePicker
            time.setOnClickListener {
                val cal = Calendar.getInstance()
                val inicialTimeSetListener =
                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        cal.set(Calendar.MINUTE, minute)

                        val cal1 = Calendar.getInstance()
                        val finalTimeSetListener1 =
                            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                                cal1.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                cal1.set(Calendar.MINUTE, minute)
                                horaFinal.text = SimpleDateFormat("HH:mm").format(cal1.time)
                                validate()
                            }
                        var tpFinalDialog = TimePickerDialog(
                            context,
                            finalTimeSetListener1,
                            cal1.get(Calendar.HOUR),
                            cal1.get(Calendar.MINUTE),
                            true
                        )
                        tpFinalDialog.setTitle("Até qual horário?")
                        tpFinalDialog.show()

                        horaInicial.text = SimpleDateFormat("HH:mm").format(cal.time)
                    }
                var tpInicialDialog = TimePickerDialog(
                    context,
                    inicialTimeSetListener,
                    cal.get(Calendar.HOUR),
                    cal.get(Calendar.MINUTE),
                    true
                )
                tpInicialDialog.setTitle("A partir de qual horário?")
                tpInicialDialog.show()
            }
        }
    }

    private fun checkColetaSolicitada() {
        var user = auth!!.currentUser

        var sp: SharedPreferences = requireContext()
            .getSharedPreferences(VOLUNTARIO_PREF, MODE_PRIVATE)

        if (sp.contains("ColetaSolicitada")) {
            //exibir coleta solicitada
            var solicitadaId = sp.getString("ColetaSolicitada", "")
            database!!.child("coletas").child(solicitadaId!!)
                .get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        //montar coleta nas views
                        horaInicial.text = it.result?.child("periodoIn")?.getValue().toString()
                        horaFinal.text = it.result?.child("periodoOut")?.getValue().toString()
                    }
                }
            verColeta()
            isStateCreated = true
        }
    }

    private fun cleanSelecaoSemana() {
        itensSemana?.forEach { item ->
            item.setImageResource(R.drawable.button_cycle_orange)
        }
    }

    private fun validate(): Boolean {
        var isValided = true

        if (itemSemanaSelectedPosition == null) {
            alert("Selecione pelo menos um dia da semana", 2, requireContext())
            isValided = false
        }

        var horaF = horaFinal.text.toString().replace(":", "")
        var horaI = horaInicial.text.toString().replace(":", "")

        if (horaF.toInt() <= horaI.toInt()) {
            //alert período invalido!
            alert("Selecione um período válido", 2, requireContext())
            horaInicial.text = "00:00"
            horaFinal.text = "00:00"
            isValided = false
        }

        return isValided
    }

    private fun toggleStateCreate() {
        if (isCanceled == true) {
            YoYo.with(Techniques.RotateIn).duration(800).repeat(0).playOn(clSolicitarColeta)
            cvCapa?.visibility = View.VISIBLE
            YoYo.with(Techniques.Pulse).duration(400).repeat(0).playOn(clColeta)
            YoYo.with(Techniques.Pulse).duration(400).repeat(0).playOn(tvCapaInfo)
            tvInfoCreate?.visibility = View.GONE
            clChecks?.visibility = View.GONE
            tvSpacer?.visibility = View.GONE

            val handler = Handler()
            val r = Runnable {
                imgSolicitarColeta?.setImageResource(R.drawable.icon_plus)
                imgSolicitarColeta?.layoutParams?.width = 116
                imgSolicitarColeta?.layoutParams?.height = 116
                horaInicial.text = "00:00"
                horaFinal.text = "00:00"
            }
            handler.postDelayed(r, 600)
            isStateCreate = false
            isStateCreated = false
            isCanceled = false

        } else if (!isStateCreate && !isStateCreated) {
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

        } else if (isStateCreate && !isStateCreated) {

            // varifica os campos e caso validados as opções, cria nova coleta com status "Solicitada"
            if (validate() && !isPausedActionButton) {
                pauseActionButton()
                //Checar se já existe coleta ativa e com id_solicitante igual ao id do usuário solicitante
                LibraryClass.firebaseDB!!.reference?.child("coletas")
                    .get().addOnCompleteListener {
                        if (it.isSuccessful) {
                            nColetas = it.result.childrenCount.toInt()
                            verificarColetaSolicitada()
                            Log.i("INFO", "coletas -> it.result = : " + it.result.toString())
                        } else {
                            alert("Verifique sua conexão!", 2, requireContext())
                        }
                    }
            } else if (isPausedActionButton) {
                alert("Já existe uma requisição recente!", 2, requireContext())
            }
        }
    }

    fun verificarColetaSolicitada() {
        coletas = ArrayList()
        LibraryClass.firebaseDB!!.reference?.child("coletas").orderByKey()
            .get().addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.i("INFO", "coletas.orderByKey -> it.result = : " + it.result.toString())

                    if (it.result.exists()) {
                        var coleta = Coleta()
                        coleta.id = it.result.key
                        coleta.solicitante = it.result.child("solicitante").value.toString()
                        coleta.coletor = it.result.child("coletor").value.toString()
                        coleta.status = it.result.child("status").value.toString()
                        coleta.periodoIn = it.result.child("periodoIn").value.toString()
                        coleta.periodoOut = it.result.child("periodoOut").value.toString()
                        coleta.ativo = it.result.child("ativo").value as Boolean?
                        coleta.ativo_solicitante =
                            it.result.child("ativo_solicitante").value.toString()

                        coletas!!.add(coleta)

                        if (coletas!!.size == nColetas) {
                            var existeColetaSolicitada = false
                            coletas!!.forEach { coleta ->
                                if (coleta.ativo_solicitante == "true_" + auth!!.currentUser!!.uid) {
                                    existeColetaSolicitada = true
                                }
                            }
                            if (!existeColetaSolicitada) {
                                setColetaAndSave()
                            }
                        }
                    } else {
                        setColetaAndSave()
                    }
                } else {
                    alert("Verifique sua conexão!", 2, requireContext())
                }
            }
    }

    fun setColetaAndSave() {
        // preenche a coleta solicitada, salva no firebase e salva o SheredPreference
        var coleta = Coleta()
        coleta!!.solicitante = auth!!.currentUser!!.uid
        coleta!!.coletor = ID_COLETOR_PRINCIPAL
        coleta!!.status = "solicitada"
        coleta!!.periodoIn = horaInicial.text.toString()
        coleta!!.periodoOut = horaFinal.text.toString()
        coleta!!.ativar().generateIdAndSave(requireContext(), VOLUNTARIO_PREF, this)
        pauseActionButton()
    }

    private fun pauseActionButton() {
        isPausedActionButton = true
        val h = Handler()
        val r = Runnable {
            isPausedActionButton = false
        }
        h.postDelayed(r, 6000)
    }

    fun verColeta() {
        //animate transitation
        cvCapa?.visibility = View.GONE
        tvSpacer?.visibility = View.GONE
        clChecks?.visibility = View.VISIBLE
        tvInfoCreate?.visibility = View.GONE
        YoYo.with(Techniques.Pulse).duration(400).repeat(0).playOn(clColeta)
        YoYo.with(Techniques.RotateOut).duration(400).repeat(0).playOn(clSolicitarColeta)
    }

    private fun confirmDeleteColetaSolicitada(sp: SharedPreferences) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Deseja realmente cancelar a coleta?")
            .setCancelable(false)
            .setPositiveButton("Sim") { dialog, id ->
                var solicitadaId = sp.getString("ColetaSolicitada", "")
                database!!.child("coletas").child(solicitadaId!!)
                    .removeValue().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            cancelarFormulario()
                            sp.edit().clear().apply()
                        } else {
                            alert("Não foi possível cancelar a solicitação!", 2, requireContext())
                        }
                    }
            }
            .setNegativeButton("Não") { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }

    private fun cancelarFormulario() {
        isCanceled = true
        toggleStateCreate()
        alert("A solicitação foi cancelada!", 2, requireContext())
    }
}