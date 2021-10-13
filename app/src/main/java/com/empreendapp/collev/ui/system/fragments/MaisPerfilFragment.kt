package com.empreendapp.collev.ui.system.fragments

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.empreendapp.collev.model.Voluntario
import com.empreendapp.collev.model.Coletor
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.widget.*
import com.empreendapp.collev.R
import com.empreendapp.collev.util.LibraryClass
import com.empreendapp.collev.util.FirebaseConnection
import com.google.firebase.database.DatabaseReference
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.daimajia.androidanimations.library.YoYo
import com.daimajia.androidanimations.library.Techniques
import com.empreendapp.collev.listeners.UserChildEventListener
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot

class MaisPerfilFragment : Fragment() {
    private var firebaseBD: FirebaseDatabase? = null
    private var database: DatabaseReference? = null
    private var ref: DatabaseReference? = null
    private var userChildEventListener: UserChildEventListener? = null
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
        val rootView = inflater.inflate(R.layout.fragment_mais_perfil, container, false)
        initFirebase()
        initViews(rootView)
        return rootView
    }

    private fun initFirebase() {
        firebaseBD = LibraryClass.firebaseDB
    }

    fun initViews(rootView: View) {
        val auth: FirebaseAuth? = FirebaseConnection.getFirebaseAuth()
        database = LibraryClass.firebaseDB?.reference

        if (auth != null) {
            database!!.child("users").child(auth.uid.toString()).get()
                .addOnCompleteListener(OnCompleteListener<DataSnapshot?> { task ->
                    if (task.isSuccessful) {
                        tipo = java.lang.String.valueOf(task.result?.child("tipo")?.getValue())
                        if (tipo == "Voluntário") {
                            spinnerRecipiente!!.visibility = View.VISIBLE
                            tvRecipiente!!.visibility = View.VISIBLE
                        }
                        Toast.makeText(context, "Tipo selecionado: " + tipo, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Firebase: Error getting data!", Toast.LENGTH_LONG).show()
                    }
                })
        }

        spinnerRecipiente = rootView.findViewById<View>(R.id.spinnerRecipiente) as Spinner
        tvRecipiente = rootView.findViewById<View>(R.id.tvRecipiente) as TextView
        val opcoesRecipientes =
            arrayOf("5 Litros", "10 Litros", "15 Litros", "20 Litros", "25 Litros", "50 Litros")
        val adapter: ArrayAdapter<*> =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opcoesRecipientes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRecipiente!!.adapter = adapter

        rootView.findViewById<View>(R.id.tvConcluir).setOnClickListener {
            editNomeEmpresa = rootView.findViewById<View>(R.id.edit_nome_empresa) as EditText
            if (!editNomeEmpresa!!.text.toString().isEmpty()) {
                if (tipo == "Coletor") {
                    saveColetorInBD()
                } else if (tipo == "Voluntário") {
                    saveVoluntarioInBD()
                }
                NavHostFragment.findNavController(requireParentFragment())
                    .navigate(R.id.action_localizacaoPerfilFragment_to_mainActivity)
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
        coletor!!.id = FirebaseConnection.getFirebaseAuth()?.uid
        coletor!!.endereco = "Rua Terezinha Campelo, 117"
        coletor!!.nome_empresa = editNomeEmpresa!!.text.toString()

        // o sistema poderia sugerir localidades próximas automaticamente se baseando na proximidade dos establececimentos em local generico
        // com os que já estão alocados.
        coletor!!.saveEnderecoInFirebase()
        coletor!!.saveNomeEmpresaInFirebase()
    }

    fun saveVoluntarioInBD() {
        voluntario = Voluntario()
        voluntario!!.id = FirebaseConnection.getFirebaseAuth()!!.uid
        voluntario!!.endereco = "Rua Terezinha Campelo, 117"
        voluntario!!.nome_empresa = editNomeEmpresa!!.text.toString()
        voluntario!!.id_local = "generico"
        voluntario!!.recipiente = spinnerRecipiente!!.selectedItem.toString()

        // o sistema poderia sugerir localidades próximas automaticamente se baseando na proximidade dos establececimentos em local generico
        // com os que já estão alocados.
        voluntario!!.saveEnderecoInFirebase()
        voluntario!!.saveNomeEmpresaInFirebase()
        voluntario!!.saveIdLocalInFirebase()
        voluntario!!.saveRecipienteInFirebase()
    }

    override fun onDestroy() {
        super.onDestroy()
        userChildEventListener?.let { ref?.removeEventListener(it) }
    }
}
