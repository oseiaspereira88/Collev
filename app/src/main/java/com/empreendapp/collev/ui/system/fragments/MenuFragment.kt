package com.empreendapp.collev.ui.system.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.empreendapp.collev.R
import com.empreendapp.collev.ui.system.LoginActivity
import com.empreendapp.collev.ui.system.ParceirosActivity
import com.empreendapp.collev.ui.system.PerfilActivity
import com.empreendapp.collev.util.DefaultFunctions.Companion.animateButton
import com.google.firebase.auth.FirebaseAuth

class MenuFragment : Fragment() {
    private var clOpPerfil: ConstraintLayout? = null
    private var clOpQuemSomos: ConstraintLayout? = null
    private var clOpConfiguracoes: ConstraintLayout? = null
    private var clOpChat: ConstraintLayout? = null
    private var clOpParceiros: ConstraintLayout? = null
    private var clOpSair: ConstraintLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_menu, container, false)
        intViews(rootView)
        return rootView
    }

    private fun intViews(rootView: View) {
        clOpPerfil = rootView.findViewById(R.id.clOpPerfil)
        clOpQuemSomos = rootView.findViewById(R.id.clOpQuemSomos)
        clOpConfiguracoes = rootView.findViewById(R.id.clOpConfiguracoes)
        clOpChat = rootView.findViewById(R.id.clOpChat)
        clOpParceiros = rootView.findViewById(R.id.clOpParceiros)
        clOpSair = rootView.findViewById(R.id.clOpSair)

        clOpPerfil!!.setOnClickListener{
            animateButton(it)
            startActivity(Intent(context, PerfilActivity::class.java))
        }

        clOpQuemSomos!!.setOnClickListener{
            animateButton(it)
            startActivity(Intent(context, QuemsomosActivity::class.java))
        }
        clOpConfiguracoes!!.setOnClickListener{
            animateButton(it)
        }
        clOpChat!!.setOnClickListener{
            animateButton(it)
        }
        clOpParceiros!!.setOnClickListener{
            animateButton(it)
            startActivity(Intent(context, ParceirosActivity::class.java))
        }

        clOpSair?.setOnClickListener{
            animateButton(it)

            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }
    }
}