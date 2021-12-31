package com.empreendapp.collev.ui.common.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.empreendapp.collev.R
import com.empreendapp.collev.model.User
import com.empreendapp.collev.ui.common.*
import com.empreendapp.collev.util.DefaultFunctions.Companion.animateButton
import com.empreendapp.collev.util.FirebaseConnection
import com.empreendapp.collev.util.LibraryClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.fragment_menu.*

class MenuFragment : Fragment() {
    private var clOpPerfil: ConstraintLayout? = null
    private var clOpQuemSomos: ConstraintLayout? = null
    private var clOpConfiguracoes: ConstraintLayout? = null
    private var clOpChat: ConstraintLayout? = null
    private var clOpParceiros: ConstraintLayout? = null
    private var clOpSair: ConstraintLayout? = null
    private var database: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    private var usuario: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_menu, container, false)
        initFirebase()

        usuario = User()
        usuario!!.getCurrentUser(database!!, auth!!)!!.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                initViews(rootView)
            }
        }

        return rootView
    }

    private fun initFirebase() {
        database = LibraryClass.firebaseDB?.reference
        auth = FirebaseConnection.getFirebaseAuth()!!
    }

    private fun initViews(rootView: View) {
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
            startActivity(Intent(context, QuemSomosActivity::class.java))
        }
        clOpConfiguracoes!!.setOnClickListener{
            animateButton(it)
            startActivity(Intent(context, ConfiguracoesActivity::class.java))
        }
        clOpChat!!.setOnClickListener{
            animateButton(it)
            startActivity(Intent(context, ChatActivity::class.java))
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

        tvNomeUsuarioMenuItem.text = usuario!!.nome
    }
}