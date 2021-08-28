package com.empreendapp.collev.fragments

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.empreendapp.collev.model.Voluntario
import com.empreendapp.collev.model.Coletor
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import com.empreendapp.collev.R
import com.empreendapp.collev.util.LibraryClass
import com.empreendapp.collev.util.FirebaseConnection
import com.google.firebase.database.DatabaseReference
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.daimajia.androidanimations.library.YoYo
import com.daimajia.androidanimations.library.Techniques
import com.empreendapp.collev.listeners.UserChildEventListener
import com.empreendapp.collev.listeners.UserValueEventListener
import com.empreendapp.collev.model.Local
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class MaisPerfilFragment : Fragment() {
    private var firebaseBD: FirebaseDatabase? = null
    private var ref: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    private var userChildEventListener: UserChildEventListener? = null
    private var userValueEventListener: UserValueEventListener? = null
    private var editNomeEmpresa: EditText? = null
    private var spinnerRecipiente: Spinner? = null
    private var tvRecipiente: TextView? = null
    private var voluntario: Voluntario? = null
    private var coletor: Coletor? = null
    private var tipo: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val rootView =
            inflater.inflate(R.layout.fragment_mais_perfil, container, false) //->container, false
        initViews(rootView)
        return rootView
    }

    private fun initFirebase() {
        firebaseBD = LibraryClass.getFirebaseDB()
        auth = FirebaseConnection.getFirebaseAuth()
        userValueEventListener = UserValueEventListener()
    }

    fun initViews(rootView: View) {
        initFirebase()
        val ref = firebaseBD!!.getReference("users").orderByKey().equalTo(auth?.currentUser!!.uid)
        ref.addListenerForSingleValueEvent(userValueEventListener!!)
        tipo = userValueEventListener!!.user!!.tipo

        spinnerRecipiente = rootView.findViewById<View>(R.id.spinnerRecipiente) as Spinner
        tvRecipiente = rootView.findViewById<View>(R.id.tvRecipiente) as TextView
        val opcoesRecipientes =
            arrayOf("5 Litros", "10 Litros", "15 Litros", "20 Litros", "25 Litros", "50 Litros")
        val adapter: ArrayAdapter<*> =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opcoesRecipientes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRecipiente!!.adapter = adapter
        if (tipo === "Voluntário") {
            spinnerRecipiente!!.visibility = View.VISIBLE
            tvRecipiente!!.visibility = View.VISIBLE
        }
        rootView.findViewById<View>(R.id.tvConcluir).setOnClickListener {
            editNomeEmpresa = rootView.findViewById<View>(R.id.edit_nome_empresa) as EditText
            if (!editNomeEmpresa!!.text.toString().isEmpty()) {
                if (tipo === "Coletor") {
                    saveColetorInBD()
                } else if (tipo === "Voluntário") {
                    saveVoluntarioInBD()
                }
                //NavHostFragment.findNavController(requireParentFragment())
                  //  .navigate(R.id.action_localizacaoPerfilFragment_to_mainActivity)
            } else {
                editNomeEmpresa!!.error = "Esse campo é obrigatório!"
            }
        }
        rootView.findViewById<View>(R.id.tvVoltar).setOnClickListener {
            NavHostFragment.findNavController(requireParentFragment())
                .navigate(R.id.action_localizacaoPerfilFragment_to_categoriaPerfilFragment)
        }
    }

    fun saveColetorInBD() {
        coletor = Coletor()
        coletor!!.id = auth!!.currentUser!!.uid
        coletor!!.endereco = "Rua Terezinha Campelo, 117"
        coletor!!.nome_empresa = editNomeEmpresa!!.text.toString()
        val local = Local()
        local.id_localidade = 0
        local.nome = "Local-Generico"
        local.descricao = "Esse é um local provisório para o ADM gerenciar os novos usuários."
        coletor!!.localidade = local

        // o sistema poderia sugerir localidades próximas automaticamente se baseando na proximidade dos establececimentos em local generico
        // com os que já estão alocados.
        coletor!!.saveEnderecoInFirebase()
        coletor!!.saveNomeEmpresaInFirebase()
        coletor!!.saveLocalidadeInFirebase()
    }

    fun saveVoluntarioInBD() {
        voluntario = Voluntario()
        voluntario!!.id = auth!!.currentUser!!.uid
        voluntario!!.endereco = "Rua Terezinha Campelo, 117"
        voluntario!!.nome_empresa = editNomeEmpresa!!.text.toString()
        val local = Local()
        local.id_localidade = 0
        local.nome = "Local-Generico"
        local.descricao = "Esse é um local provisório para o ADM gerenciar os novos usuários."
        voluntario!!.localidade = local
        voluntario!!.recipiente = spinnerRecipiente!!.selectedItem.toString()

        // o sistema poderia sugerir localidades próximas automaticamente se baseando na proximidade dos establececimentos em local generico
        // com os que já estão alocados.
        voluntario!!.saveEnderecoInFirebase()
        voluntario!!.saveNomeEmpresaInFirebase()
        voluntario!!.saveLocalidadeInFirebase()
        voluntario!!.saveRecipienteInFirebase()
    }

    fun animateButton(view: View?) {
        YoYo.with(Techniques.Pulse)
            .duration(250)
            .repeat(0)
            .playOn(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        userChildEventListener?.let { ref?.removeEventListener(it) }
    }
}
