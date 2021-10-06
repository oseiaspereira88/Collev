package com.empreendapp.collev.ui.system.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.empreendapp.collev.R
import com.empreendapp.collev.ui.system.LoginActivity
import com.empreendapp.collev.ui.system.PerfilActivity
import com.google.firebase.auth.FirebaseAuth
import com.makeramen.roundedimageview.RoundedImageView

class MenuFragment : Fragment() {
    private var imgPerfil : RoundedImageView? = null
    private var imgSair : RoundedImageView? = null

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
        imgPerfil = rootView.findViewById(R.id.imgPerfil)
        imgSair = rootView.findViewById(R.id.imgSair)

        imgPerfil?.setOnClickListener{
            startActivity(Intent(context, PerfilActivity::class.java))
        }

        imgSair?.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }
    }
}