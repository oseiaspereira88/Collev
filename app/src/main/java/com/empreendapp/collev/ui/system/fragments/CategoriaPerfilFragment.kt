package com.empreendapp.collev.ui.system.fragments

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import com.empreendapp.collev.R
import com.empreendapp.collev.util.LibraryClass
import com.empreendapp.collev.util.FirebaseConnection
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.daimajia.androidanimations.library.YoYo
import com.daimajia.androidanimations.library.Techniques
import com.empreendapp.collev.model.User

class CategoriaPerfilFragment : Fragment() {
    private var firebaseBD: FirebaseDatabase? = null
    private var auth: FirebaseAuth? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val rootView =
            inflater.inflate(R.layout.fragment_categoria_perfil, null) //->container, false
        initViews(rootView)
        return rootView
    }

    private fun initFirebase() {
        firebaseBD = LibraryClass.firebaseDB
        auth = FirebaseConnection.getFirebaseAuth()
    }

    fun initViews(rootView: View) {
        val cv_option_coletor = rootView.findViewById<View>(R.id.cv_option_coletor) as CardView
        val cv_option_colaborador = rootView.findViewById<View>(R.id.cv_option_colaborador) as CardView
        initFirebase()
        val user = User()
        user.id = auth!!.currentUser!!.uid
        cv_option_coletor.setOnClickListener { v ->
            animateButton(v)
            user.tipo = "Coletor"

            user.saveTipoInFirebase()

            //chamada do LocalizacaoPerfilFragment
            NavHostFragment.findNavController(requireParentFragment())
                .navigate(R.id.action_categoriaPerfilFragment_to_localizacaoPerfilFragment)
        }
        cv_option_colaborador.setOnClickListener { v ->
            animateButton(v)
            user.tipo = "Colaborador"
            user.saveTipoInFirebase()

            //chamada do LocalizacaoPerfilFragment
            NavHostFragment.findNavController(requireParentFragment())
                .navigate(R.id.action_categoriaPerfilFragment_to_localizacaoPerfilFragment)
        }
    }

    fun animateButton(view: View?) {
        YoYo.with(Techniques.Pulse)
            .duration(250)
            .repeat(0)
            .playOn(view)
    }
}