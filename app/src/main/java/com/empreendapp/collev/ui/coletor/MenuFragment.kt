package com.empreendapp.collev.ui.coletor

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.empreendapp.collev.R
import com.empreendapp.collev.ui.adm.AdmActivity
import com.empreendapp.collev.ui.system.InitPerfilActivity
import com.empreendapp.collev.ui.system.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.makeramen.roundedimageview.RoundedImageView

class MenuFragment : Fragment() {
    private var imgSair : RoundedImageView? = null
    private var cvOpConta : CardView? = null
    private var cvOpConfiguracoes : CardView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_menu, container, false)
        intViews(rootView);
        return rootView
    }

    private fun intViews(rootView: View) {
        imgSair = rootView.findViewById(R.id.imgSair)
        cvOpConta = rootView.findViewById(R.id.cvOpConta)
        cvOpConfiguracoes = rootView.findViewById(R.id.cvOpConfiguracoes)

        imgSair?.setOnClickListener({
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        })

        cvOpConta?.setOnClickListener({
            startActivity(Intent(context, InitPerfilActivity::class.java))
        })

        cvOpConfiguracoes?.setOnClickListener({
            startActivity(Intent(context, AdmActivity::class.java))
        })
    }
}