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


class VoluntarioFragment : Fragment() {
    var imgSolicitarColeta: ImageView? = null
    var clSolicitarColeta: ConstraintLayout? = null
    var tvDataColeta: TextView? = null
    var tvDiaColeta: TextView? = null
    var imgMapBG: ImageView? = null
    var cvCapa: CardView? = null
    var clColeta: ConstraintLayout? = null
    var isStateCreate: Boolean? = false;
    var isStateCreated: Boolean? = false;

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
            tvDataColeta = rootView.findViewById(R.id.tvDataColeta)
            tvDiaColeta = rootView.findViewById(R.id.tvDiaColeta)
            imgMapBG = rootView.findViewById(R.id.imgMapBG)
            cvCapa = rootView.findViewById(R.id.cvCapa)
            clColeta = rootView.findViewById(R.id.clColeta)

            //adding all listeners
            imgSolicitarColeta?.setOnClickListener(View.OnClickListener {
                toggleStateCreate()
            })
        }
    }

    private fun toggleStateCreate() {
        if (isStateCreate == false && isStateCreated == false) {
            YoYo.with(Techniques.Pulse).duration(400).repeat(0).playOn(clColeta)
            YoYo.with(Techniques.RotateIn).duration(800).repeat(0).playOn(clSolicitarColeta)
            cvCapa?.visibility = View.GONE
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

            YoYo.with(Techniques.Pulse).duration(400).repeat(0).playOn(clColeta)
            YoYo.with(Techniques.RotateOut).duration(400).repeat(0).playOn(clSolicitarColeta)
            clSolicitarColeta?.visibility = View.GONE
            isStateCreated = true
        }
    }
}