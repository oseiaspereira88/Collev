package com.empreendapp.collev.ui.common.fragments

import android.app.AlertDialog
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.empreendapp.collev.model.Colaborador
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
import com.empreendapp.collev.listeners.UserChildEventListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot

class MaisPerfilFragment : Fragment(), OnMapReadyCallback {
    private var firebaseBD: FirebaseDatabase? = null
    private var database: DatabaseReference? = null
    private var ref: DatabaseReference? = null
    private var userChildEventListener: UserChildEventListener? = null
    private var editNomeEmpresa: EditText? = null
    private var spinnerRecipiente: Spinner? = null
    private var tvRecipiente: TextView? = null
    private var colaborador: Colaborador? = null
    private var coletor: Coletor? = null
    private var tipo: String? = null
    private var googleMap: GoogleMap?=null

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
                        if (tipo == "Colaborador") {
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
            arrayOf("5 litros", "10 litros", "15 litros", "20 litros", "25 litros", "50 litros")
        val adapter: ArrayAdapter<*> =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opcoesRecipientes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRecipiente!!.adapter = adapter

        rootView.findViewById<View>(R.id.tvConcluir)
            .setOnClickListener {
            editNomeEmpresa = rootView.findViewById<View>(R.id.edit_nome_empresa) as EditText
            if (!editNomeEmpresa!!.text.toString().isEmpty()) {
                if (tipo == "Coletor") {
                    saveColetorInBD()
                } else if (tipo == "Colaborador") {
                    saveColaboradorInBD()
                }
                NavHostFragment.findNavController(requireParentFragment())
                    .navigate(R.id.action_localizacaoPerfilFragment_to_mainActivity)
            } else {
                editNomeEmpresa!!.error = "Esse campo é obrigatório!"
            }
        }

        rootView.findViewById<View>(R.id.tvVoltar)
            .setOnClickListener {
            NavHostFragment.findNavController(requireParentFragment())
                .navigate(R.id.action_localizacaoPerfilFragment_to_categoriaPerfilFragment)
        }

        rootView.findViewById<TextView>(R.id.tvLocalizacaoSelect)
            .setOnClickListener {
                val dialogBuilder = AlertDialog.Builder(requireContext())
                val dialogView = this.layoutInflater.inflate(R.layout.dialog_input_map, null)
                dialogBuilder.setView(dialogView)
                val dialog = dialogBuilder.create()
                dialog!!.getWindow()?.setBackgroundDrawableResource(R.drawable.transparent_bg)
                dialog.show()

                val mapFragment = childFragmentManager
                    .findFragmentById(R.id.fragmentDialogMap) as SupportMapFragment
                mapFragment.getMapAsync(this)

                val imgCancelDialog = dialogView.findViewById<ImageView>(R.id.imgCancelDialog)
                imgCancelDialog.setOnClickListener {
                    dialog.cancel()
                }
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

        coletor!!.deleteNameSP(requireContext())
    }

    fun saveColaboradorInBD() {
        colaborador = Colaborador()
        colaborador!!.id = FirebaseConnection.getFirebaseAuth()!!.uid
        colaborador!!.endereco = "Rua Terezinha Campelo, 117"
        colaborador!!.nome_empresa = editNomeEmpresa!!.text.toString()
        colaborador!!.id_local = "generico"
        colaborador!!.recipiente = spinnerRecipiente!!.selectedItem.toString()

        // o sistema poderia sugerir localidades próximas automaticamente se baseando na proximidade dos establececimentos em local generico
        // com os que já estão alocados.
        colaborador!!.saveEnderecoInFirebase()
        colaborador!!.saveNomeEmpresaInFirebase()
        colaborador!!.saveIdLocalInFirebase()
        colaborador!!.saveRecipienteInFirebase()

        colaborador!!.deleteNameSP(requireContext())
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap=p0

        val latLng= LatLng(28.6139,77.2090)
        val markerOptions: MarkerOptions = MarkerOptions().position(latLng).title("New Delhi")

        // moving camera and zoom map
        val zoomLevel = 19.0f //This goes up to 21

        googleMap.let {
            it!!.addMarker(markerOptions)
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))
        }
    }
}
