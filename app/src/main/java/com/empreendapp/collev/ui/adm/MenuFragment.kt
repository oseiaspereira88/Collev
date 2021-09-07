package com.empreendapp.collev.ui.adm

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.empreendapp.collev.R
import com.empreendapp.collev.ui.system.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.makeramen.roundedimageview.RoundedImageView

class MenuFragment : Fragment() {
    private var imgSair : RoundedImageView? = null

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
        imgSair = rootView.findViewById<RoundedImageView>(R.id.imgSair)

        imgSair?.setOnClickListener(View.OnClickListener { v ->
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        })
    }
}