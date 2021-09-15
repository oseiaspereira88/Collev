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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.getInstance


class VoluntarioFragment : Fragment() {
    var imgSolicitarColeta: ImageView? = null
    var clSolicitarColeta: ConstraintLayout? = null
    var clChecks: ConstraintLayout? = null
    var tvInfoCreate: TextView? = null
    var tvDataColeta: TextView? = null
    var tvDiaColeta: TextView? = null
    var imgMapBG: ImageView? = null
    var cvCapa: CardView? = null
    var clColeta: ConstraintLayout? = null
    var tvSpacer: TextView? = null
    var itensSemana: ArrayList<ImageView>? = null
    var itemSemanaSelectedPosition: Int? = null
    var isStateCreate: Boolean? = false
    var isStateCreated: Boolean? = false

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
            tvDataColeta = rootView.findViewById(R.id.tvDataColeta)
            tvDiaColeta = rootView.findViewById(R.id.tvDiaColeta)
            imgMapBG = rootView.findViewById(R.id.imgMapBG)
            cvCapa = rootView.findViewById(R.id.cvCapa)
            clColeta = rootView.findViewById(R.id.clColeta)
            tvSpacer = rootView.findViewById(R.id.tvSpacer)

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
                    if(itemSemanaSelectedPosition == i){
                        item.setImageResource(R.drawable.button_cycle_orange)
                        itemSemanaSelectedPosition = null
                    } else{
                        item.setImageResource(R.drawable.button_cycle_blue)
                        itemSemanaSelectedPosition = i
                    }
                })
            }

            //adding all listeners
            imgSolicitarColeta?.setOnClickListener(View.OnClickListener {
                toggleStateCreate()
            })
        }
    }

    private fun cleanSelecaoSemana() {
        itensSemana?.forEach { item ->
            item.setImageResource(R.drawable.button_cycle_orange)
        }
    }

    private fun isCamposValided(): Boolean{
        if(itemSemanaSelectedPosition != null){
            return true
        }
        return false
    }

    private fun toggleStateCreate() {
        if (isStateCreate == false && isStateCreated == false) {
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
            handler.postDelayed(r, 700)
            isStateCreate = true

        } else if (isStateCreate == true) {
            // varifica os campos e caso validados as opções, cria nova coleta com status "Solicitada"
            if(isCamposValided()){
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
}