package com.empreendapp.collev.ui.voluntario

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.empreendapp.collev.R

class VoluntarioFragment : Fragment() {
    var imgSolicitarColeta: ImageView? = null
    var cvCapa: CardView? = null

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
            cvCapa = rootView.findViewById(R.id.cvCapa)

            //adding all listeners
            imgSolicitarColeta?.setOnClickListener(View.OnClickListener {
                cvCapa?.visibility = View.GONE
            })
        }
    }
}