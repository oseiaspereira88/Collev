package com.empreendapp.collev.ui.common.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import com.google.firebase.auth.FirebaseAuth
import com.empreendapp.collev.model.Colaborador
import com.empreendapp.collev.model.Coletor
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import com.empreendapp.collev.R
import com.empreendapp.collev.util.LibraryClass
import com.empreendapp.collev.util.FirebaseConnection
import com.google.firebase.database.DatabaseReference
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.widget.Toast
import com.empreendapp.collev.util.DefaultFunctions
import com.empreendapp.collev.util.DefaultFunctions.Companion.animateButton


class MaisPerfilFragment : Fragment(), OnMapReadyCallback {
    private var googleMap: GoogleMap? = null
    private var database: DatabaseReference? = null
    private var editNomeEmpresa: EditText? = null
    private var spinnerRecipiente: Spinner? = null
    private var tvRecipiente: TextView? = null
    private var colaborador: Colaborador? = null
    private var coletor: Coletor? = null
    private var tipo: String? = null
    private var auth: FirebaseAuth? = null
    private var isUsingCurrentLocalization = true
    private var currentLatLng: LatLng? = null
    private var selectedLatLng: LatLng? = null

    // Google Map Vars
    private var dialogMapBuilder: AlertDialog.Builder? = null
    private var dialogMapView: View? = null
    private var dialogMap: AlertDialog? = null

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
        auth = FirebaseConnection.getFirebaseAuth()
        database = LibraryClass.firebaseDB?.reference
    }

    fun initViews(rootView: View) {
        if (auth != null) {
            database!!.child("users").child(auth!!.uid.toString()).get()
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        tipo = java.lang.String.valueOf(task.result?.child("tipo")?.getValue())
                        if (tipo == "Colaborador") {
                            spinnerRecipiente!!.visibility = View.VISIBLE
                            tvRecipiente!!.visibility = View.VISIBLE
                        }
                        Toast.makeText(context, "Tipo selecionado: " + tipo, Toast.LENGTH_LONG)
                            .show()
                    } else {
                        Toast.makeText(context, "Firebase: Error getting data!", Toast.LENGTH_LONG)
                            .show()
                    }
                }
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
                val manager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    DefaultFunctions.showNoGpsDialog(requireContext())
                } else {
                    if (dialogMapView == null) {
                        dialogMapBuilder = AlertDialog.Builder(requireContext())
                        dialogMapView = this.layoutInflater.inflate(R.layout.dialog_input_map, null)
                        dialogMapBuilder!!.setView(dialogMapView)
                        dialogMap = dialogMapBuilder!!.create()
                        dialogMap!!.getWindow()
                            ?.setBackgroundDrawableResource(R.drawable.transparent_bg)

                        val mapFragment = childFragmentManager
                            .findFragmentById(R.id.fragmentDialogMap) as SupportMapFragment
                        mapFragment.getMapAsync(this)

                        val imgCancelDialog =
                            dialogMapView!!.findViewById<ImageView>(R.id.imgCancelDialog)
                        imgCancelDialog.setOnClickListener {
                            dialogMap!!.cancel()
                        }

                        val tvDialogMapUseCurrentLocaization =
                            dialogMapView!!.findViewById<TextView>(R.id.tvDialogMapUseCurrentLocaization)
                        tvDialogMapUseCurrentLocaization.setOnClickListener {
                            isUsingCurrentLocalization = true

                            if (currentLatLng != null) {
                                markerHere(currentLatLng!!)
                                animateHere(currentLatLng!!, 19.0f)
                            }

                            animateButton(it)
                        }

                        val tvDialogMapConcluir =
                            dialogMapView!!.findViewById<TextView>(R.id.tvDialogMapConcluir)
                        tvDialogMapConcluir.setOnClickListener {
                            dialogMap!!.cancel()
                        }

                        val tvDialogMapSkip =
                            dialogMapView!!.findViewById<TextView>(R.id.tvDialogMapSkip)
                        tvDialogMapSkip.setOnClickListener {
                            dialogMap!!.cancel()
                        }
                    }

                    dialogMap!!.show()
                }
            }
    }

    fun saveColetorInBD() {
        coletor = Coletor()
        coletor!!.id = FirebaseConnection.getFirebaseAuth()?.uid
        coletor!!.endereco = "Rua Terezinha Campelo, 117"
        coletor!!.nome_empresa = editNomeEmpresa!!.text.toString()
        coletor!!.has_profile_initialized = true

        if (isUsingCurrentLocalization) {
            coletor!!.lat = currentLatLng?.latitude
            coletor!!.lng = currentLatLng?.longitude
        } else {
            coletor!!.lat = selectedLatLng?.latitude
            coletor!!.lng = selectedLatLng?.longitude
        }

        // o sistema poderia sugerir localidades próximas automaticamente se baseando na proximidade dos establececimentos em local generico
        // com os que já estão alocados.
        coletor!!.saveEnderecoInFirebase()
        coletor!!.saveNomeEmpresaInFirebase()
        coletor!!.setHasProfileInitialized()
        coletor!!.saveLatInFirebase()
        coletor!!.saveLngInFirebase()

        coletor!!.deleteNameSP(requireContext())
    }

    fun saveColaboradorInBD() {
        colaborador = Colaborador()
        colaborador!!.id = FirebaseConnection.getFirebaseAuth()!!.uid
        colaborador!!.endereco = "Rua Terezinha Campelo, 117"
        colaborador!!.nome_empresa = editNomeEmpresa!!.text.toString()
        colaborador!!.id_local = "generico"
        colaborador!!.recipiente = spinnerRecipiente!!.selectedItem.toString()
        colaborador!!.has_profile_initialized = true

        if (isUsingCurrentLocalization) {
            colaborador!!.lat = currentLatLng?.latitude
            colaborador!!.lng = currentLatLng?.longitude
        } else {
            colaborador!!.lat = selectedLatLng?.latitude
            colaborador!!.lng = selectedLatLng?.longitude
        }

        // o sistema poderia sugerir localidades próximas automaticamente se baseando na proximidade dos establececimentos em local generico
        // com os que já estão alocados.
        colaborador!!.saveEnderecoInFirebase()
        colaborador!!.saveNomeEmpresaInFirebase()
        colaborador!!.saveIdLocalInFirebase()
        colaborador!!.saveRecipienteInFirebase()
        colaborador!!.setHasProfileInitialized()
        colaborador!!.saveLatInFirebase()
        colaborador!!.saveLngInFirebase()

        colaborador!!.deleteNameSP(requireContext())
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0

        googleMap?.let {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat
                    .requestPermissions(
                        requireActivity(),
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ),
                        5
                    )
                return
            }

            it.isMyLocationEnabled = true
            it.setOnMyLocationChangeListener { arg0 ->
                if(isUsingCurrentLocalization){
                    currentLatLng = LatLng(arg0.latitude, arg0.longitude)

                    markerHere(currentLatLng!!)
                    animateHere(currentLatLng!!, 19.0f)
                }
            }

            it.setOnMapClickListener { point ->
                markerHere(point)
                animateHere(point, 19.0f)
                isUsingCurrentLocalization = false
                selectedLatLng = point
            }
        }
    }

    private fun markerHere(point: LatLng){
        googleMap!!.clear()

        googleMap!!.addMarker(
            MarkerOptions()
                .position(point)
                .title("Marcar aqui?")
        )
    }

    private fun animateHere(point: LatLng, zoomLevel: Float){
        googleMap!!.animateCamera(
            CameraUpdateFactory
                .newLatLngZoom(point, zoomLevel)
        )
    }

}
