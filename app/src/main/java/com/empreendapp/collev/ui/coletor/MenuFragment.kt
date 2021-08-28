package com.empreendapp.collev.ui.coletor

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.empreendapp.collev.R
import com.empreendapp.collev.adapters.NotificacoesAdapter
import com.empreendapp.collev.model.Coleta
import com.empreendapp.collev.ui.system.InitPerfilActivity
import com.empreendapp.collev.ui.system.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.makeramen.roundedimageview.RoundedImageView

class MenuFragment : Fragment() {
    private var imgSair : RoundedImageView? = null
    private var cvOpConta : CardView? = null

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

        imgSair?.setOnClickListener({
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        })

        cvOpConta?.setOnClickListener({
            startActivity(Intent(context, InitPerfilActivity::class.java))
        })
    }
}