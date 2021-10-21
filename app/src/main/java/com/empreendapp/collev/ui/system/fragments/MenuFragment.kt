package com.empreendapp.collev.ui.system.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.empreendapp.collev.R
import com.empreendapp.collev.ui.system.LoginActivity
import com.empreendapp.collev.ui.system.PerfilActivity
import com.empreendapp.collev.util.DefaultFunctions.Companion.animateButton
import com.google.firebase.auth.FirebaseAuth
import com.makeramen.roundedimageview.RoundedImageView

class MenuFragment : Fragment() {
    private var cvOpPerfil: CardView? = null
    private var cvOpTutorial: CardView? = null
    private var cvOpConfiguracoes: CardView? = null
    private var cvOpChat: CardView? = null
    private var cvOpQuemSomos: CardView? = null
    private var cvOpSair: CardView? = null

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
        cvOpPerfil = rootView.findViewById(R.id.cvOpPerfil)
        cvOpTutorial = rootView.findViewById(R.id.cvOpTutorial)
        cvOpConfiguracoes = rootView.findViewById(R.id.cvOpConfiguracoes)
        cvOpChat = rootView.findViewById(R.id.cvOpChat)
        cvOpQuemSomos = rootView.findViewById(R.id.cvOpQuemSomos)
        cvOpSair = rootView.findViewById(R.id.cvOpSair)

        cvOpPerfil!!.setOnClickListener{
            animateButton(it)
            startActivity(Intent(context, PerfilActivity::class.java))
        }

        cvOpTutorial!!.setOnClickListener{
            animateButton(it)
        }
        cvOpConfiguracoes!!.setOnClickListener{
            animateButton(it)
        }
        cvOpChat!!.setOnClickListener{
            animateButton(it)
        }
        cvOpQuemSomos!!.setOnClickListener{
            animateButton(it)
        }

        cvOpSair?.setOnClickListener{
            animateButton(it)

            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }
    }
}