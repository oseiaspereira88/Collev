package com.empreendapp.collev.ui.colaborador

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
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.empreendapp.collev.util.ColetaStatus.Companion.AGENDADA
import com.empreendapp.collev.util.ColetaStatus.Companion.ATENDIDA
import com.empreendapp.collev.util.ColetaStatus.Companion.SOLICITADA
import com.empreendapp.collev.util.DefaultFunctions.Companion.alert
import com.empreendapp.collev.util.DefaultFunctions.Companion.animateInputError
import com.empreendapp.collev.util.DefaultFunctions.Companion.animateTutorialPulse
import com.empreendapp.collev.util.FirebaseConnection
import com.empreendapp.collev.util.LibraryClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


open class ColaboradorFragment : Fragment() {
    private val ID_COLETOR_PRINCIPAL = "cvzFUzdnKgSaB28TsACocqBMzdg2"
    private val COLABORADOR_PREF = "com.empreendapp.collev.COLABORADOR_PREF"
    private var imgSolicitarColeta: ImageView? = null
    private var clSolicitarColeta: ConstraintLayout? = null
    private var clEtapas: ConstraintLayout? = null
    private var tvInfoCreate: TextView? = null
    private var tvTituloColeta: TextView? = null
    private var tvSubtituloColeta: TextView? = null
    private var cvColetaCapa: CardView? = null
    private var tvCapaInfo: TextView? = null
    private var clColeta: ConstraintLayout? = null
    private var clSemanaLista: ConstraintLayout? = null
    private var clTime: ConstraintLayout? = null

    private var llEtapaAgendada: LinearLayout? = null
    private var tvEtapaAgendada: TextView? = null
    private var tvEtapaAgendadaDescricao: TextView? = null
    private var tvEtapaAgendadaEmpty: TextView? = null

    private var itensSemana: ArrayList<ImageView>? = null
    private var diasPossiveis: ArrayList<Int>? = null
    private var isStateCreate: Boolean = false
    private var isCanceled: Boolean = false
    private lateinit var imgCancelarSolicitacao: ImageView
    private lateinit var imgSelectTime: ImageView
    private lateinit var tvPeriodoIn: TextView
    private lateinit var tvPeriodoOut: TextView
    private var database: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    private var coletas: ArrayList<Coleta>? = null
    private var nColetas: Int = 0
    private var isPausedActionButton: Boolean = false

    //variáveis do tutorial
    private var imgClickSelectTimeAgendaTutorial: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_colaborador, container, false)
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
            imgSolicitarColeta = rootView.findViewById(R.id.imgSolicitarColeta1)
            clSolicitarColeta = rootView.findViewById(R.id.clSolicitarColeta)
            clEtapas = rootView.findViewById(R.id.clEtapas)
            tvInfoCreate = rootView.findViewById(R.id.tvSolicitacaoColetaTitle)
            tvTituloColeta = rootView.findViewById(R.id.tvTituloColeta)
            tvSubtituloColeta = rootView.findViewById(R.id.tvSubtituloColeta)
            cvColetaCapa = rootView.findViewById(R.id.cvColetaCapa)
            tvCapaInfo = rootView.findViewById(R.id.tvCapaInfo)
            clColeta = rootView.findViewById(R.id.clColeta)
            clSemanaLista = rootView.findViewById(R.id.clSemanaLista)
            clTime = rootView.findViewById(R.id.clTime)
            imgCancelarSolicitacao = rootView.findViewById(R.id.imgCancelarSolicitacao)
            imgSelectTime = rootView.findViewById(R.id.imgSelectTime)
            tvPeriodoIn = rootView.findViewById(R.id.tvPeriodoIn)
            tvPeriodoOut = rootView.findViewById(R.id.tvPeriodoOut)

            llEtapaAgendada = rootView.findViewById(R.id.llEtapaAgendada)
            tvEtapaAgendada = rootView.findViewById(R.id.tvEtapaAgendada)
            tvEtapaAgendadaDescricao = rootView.findViewById(R.id.tvEtapaAgendadaDescricao)
            tvEtapaAgendadaEmpty = rootView.findViewById(R.id.tvEtapaAgendadaEmpty)

            //Variaveis do Tutorial
            imgClickSelectTimeAgendaTutorial = rootView.findViewById(R.id.imgClickSelectTimeAgendaTutorial)

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

            diasPossiveis = ArrayList()

            imgResources.forEachIndexed { i, id ->
                itensSemana?.add(rootView.findViewById(id))
                itensSemana?.last()?.setOnClickListener {
                    //cleanSelecaoSemana()
                    val item: ImageView = it as ImageView
                    if (diasPossiveis!!.contains(i)) {
                        item.setImageResource(R.drawable.border_cycle_orange)
                        diasPossiveis!!.remove(i)
                    } else {
                        item.setImageResource(R.drawable.button_cycle_orange)
                        diasPossiveis!!.add(i)
                    }
                }
            }

            //adding all listeners
            imgSolicitarColeta?.setOnClickListener {
                toggleStateCreate()
            }

            //clear all listeners
            imgCancelarSolicitacao?.setOnClickListener {
                var sp: SharedPreferences =
                    requireContext().getSharedPreferences(COLABORADOR_PREF, MODE_PRIVATE)
                if (sp.contains("ColetaSolicitada")) {
                    confirmDeleteColetaSolicitada(sp)
                } else if (isStateCreate){
                    isCanceled = true
                    toggleStateCreate()
                }
            }

            //TimePicker
            imgSelectTime?.setOnClickListener {
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
                                tvPeriodoOut.text = SimpleDateFormat("HH:mm").format(cal1.time)
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

                        tvPeriodoIn.text = SimpleDateFormat("HH:mm").format(cal.time)
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
        //getUserById()

        var sp: SharedPreferences = requireContext()
            .getSharedPreferences(COLABORADOR_PREF, MODE_PRIVATE)

        if (sp.contains("ColetaSolicitada")) {
            //exibir coleta solicitada
            var solicitadaId = sp.getString("ColetaSolicitada", "")
            database!!.child("coletas").child(solicitadaId!!)
                .get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (it.result.exists()){
                            //montar coleta nas views
                            it.result.getValue(Coleta::class.java)!!.let{ coleta ->
                                if(coleta.status!! == AGENDADA){
                                    llEtapaAgendada!!.visibility = View.VISIBLE
                                    tvEtapaAgendadaEmpty!!.visibility = View.GONE
                                    tvEtapaAgendadaDescricao!!.text = "${coleta.diaMarcado}, às ${coleta.horaMarcada} horas"
                                } else if (coleta.status!! == ATENDIDA){
                                    sp.edit().clear().apply()

                                    val handler = Handler()
                                    val r = Runnable {
                                        cancelarFormulario()
                                    }

                                    handler.postDelayed(r, 900)
                                }
                            }
                        } else{
                            sp.edit().clear().apply()

                            val handler = Handler()
                            val r = Runnable {
                                cancelarFormulario()
                            }
                        }
                    }
                }
            verColeta()
        }
    }

    private fun validate(): Boolean {
        var isValided = true

        if (diasPossiveis!!.isEmpty()) {
            alert("Selecione pelo menos um dia da semana", 2, requireContext())
            isValided = false
        }

        var horaF = tvPeriodoOut.text.toString().replace(":", "")
        var horaI = tvPeriodoIn.text.toString().replace(":", "")

        if (horaF.toInt() <= horaI.toInt()) {
            //alert período invalido!
            alert("Selecione um período válido", 2, requireContext())
            tvPeriodoIn.text = "00:00"
            tvPeriodoOut.text = "00:00"
            animateInputError(clColeta!!)
            animateTutorialPulse(imgClickSelectTimeAgendaTutorial!!)

            isValided = false
        }

        return isValided
    }

    private fun toggleStateCreate() {
        if (isCanceled) {
            YoYo.with(Techniques.RotateIn).duration(800).repeat(0).playOn(clSolicitarColeta)
            cvColetaCapa?.visibility = View.VISIBLE
            clSolicitarColeta?.visibility = View.VISIBLE
            YoYo.with(Techniques.Pulse).duration(400).repeat(0).playOn(clColeta)
            YoYo.with(Techniques.Pulse).duration(400).repeat(0).playOn(tvCapaInfo)
            clEtapas?.visibility = View.GONE

            val handler = Handler()
            val r = Runnable {
                imgSolicitarColeta?.setImageResource(R.drawable.icon_plus)
                imgSolicitarColeta?.layoutParams?.width = 116
                imgSolicitarColeta?.layoutParams?.height = 116
                tvPeriodoIn.text = "00:00"
                tvPeriodoOut.text = "00:00"
            }
            handler.postDelayed(r, 600)
            isStateCreate = false
            isCanceled = false

        } else if (!isStateCreate) {
            YoYo.with(Techniques.RotateIn).duration(800).repeat(0).playOn(clSolicitarColeta)
            cvColetaCapa?.visibility = View.GONE
            clTime!!.visibility = View.VISIBLE
            clSemanaLista!!.visibility = View.VISIBLE
            YoYo.with(Techniques.Pulse).duration(400).repeat(0).playOn(clColeta)
            animateTutorialPulse(imgClickSelectTimeAgendaTutorial!!)

            val handler = Handler()
            val r = Runnable {
                imgSolicitarColeta?.setImageResource(R.drawable.icon_check_white)
                imgSolicitarColeta?.layoutParams?.width = 90
                imgSolicitarColeta?.layoutParams?.height = 90
            }

            handler.postDelayed(r, 600)
            isStateCreate = true

        } else if (isStateCreate) {

            // varifica os campos e caso validados as opções, cria nova coleta com status "Solicitada"
            if (validate() && !isPausedActionButton) {
                pauseActionButton()
                //Checar se já existe coleta ativa e com id_solicitante igual ao id do usuário solicitante
                LibraryClass.firebaseDB!!.reference?.child("coletas")
                    .get().addOnCompleteListener {
                        if (it.isSuccessful) {
                            nColetas = it.result.childrenCount.toInt()
                            checkColetaSolicitadaInBdElseSave()
                            Log.i("INFO", "coletas -> it.result = : " + it.result.toString())
                        } else {
                            alert("Verifique sua conexão!", 2, requireContext())
                            animateInputError(clColeta!!)
                        }
                    }
            } else if (isPausedActionButton) {
                alert("Já existe uma requisição recente!", 2, requireContext())
            }
        }
    }

    fun checkColetaSolicitadaInBdElseSave() {
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
                        coleta.ativo_solicitante = it.result.child("ativo_solicitante").value.toString()

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
                    animateInputError(clColeta!!)
                }
            }
    }

    fun setColetaAndSave() {
        // preenche a coleta solicitada, salva no firebase e salva o SheredPreference
        var coleta = Coleta()
        coleta.solicitante = auth!!.currentUser!!.uid
        coleta.coletor = ID_COLETOR_PRINCIPAL //depois observar regra de negócio sobre multiplos coletores
        coleta.status = SOLICITADA
        coleta.periodoIn = tvPeriodoIn.text.toString()
        coleta.periodoOut = tvPeriodoOut.text.toString()
        coleta.diasPossiveis = diasPossiveis
        coleta.ativar()
        coleta.generateIdAndSave(requireContext(), COLABORADOR_PREF, this)

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
        cvColetaCapa!!.visibility = View.GONE
        clEtapas!!.visibility = View.VISIBLE
        clSolicitarColeta?.visibility = View.GONE
        clTime!!.visibility = View.GONE
        clSemanaLista!!.visibility = View.GONE

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
                            alert("A solicitação foi cancelada!", 2, requireContext())
                        } else {
                            alert("Não foi possível cancelar a solicitação!", 2, requireContext())
                            animateInputError(clColeta!!)
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
    }
}
