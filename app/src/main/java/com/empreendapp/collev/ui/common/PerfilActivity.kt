package com.empreendapp.collev.ui.common

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.EditText
import com.empreendapp.collev.R
import com.empreendapp.collev.model.User
import com.empreendapp.collev.util.DefaultFunctions.Companion.alertSnack
import com.empreendapp.collev.util.DefaultFunctions.Companion.animateButton
import com.empreendapp.collev.util.FirebaseConnection
import com.empreendapp.collev.util.LibraryClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_perfil.*
import android.provider.MediaStore
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.os.Environment
import android.graphics.Bitmap
import android.util.Log
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import android.app.ProgressDialog
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso


class PerfilActivity : AppCompatActivity(), OnMapReadyCallback {
    private val PICK_IMAGE_CAMERA = 1
    private val PICK_IMAGE_GALLERY = 2
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private var imgFilePath: Uri? = null
    private var database: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    private var usuario: User? = null
    private var isNameEditing = false
    private var isEmpresaEditing = false
    private var isPasswordEditing = false

    //Google Maps Vars
    private var googleMap: GoogleMap? = null
    private var isUsingCurrentLocalization = false
    private var isUsingAccountLocalization = true
    private var currentLatLng: LatLng? = null
    private var selectedLatLng: LatLng? = null
    private var dialogMapView: View? = null
    private var dialogMapBuilder: AlertDialog.Builder? = null
    private var dialogMap: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        initFirebase()

        usuario = User()
        usuario!!.getCurrentUser(database!!, auth!!)!!
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    usuario!!.id = auth!!.uid

                    initViews()
                    initListeners()
                }
            }
    }

    private fun initFirebase() {
        database = LibraryClass.firebaseDB?.reference
        auth = FirebaseConnection.getFirebaseAuth()!!
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.getReference()
    }

    fun initViews() {
        tvPerfilNome.text = usuario!!.nome
        tvPerfilEmail.text = usuario!!.email
        tvPerfilEmpresa.text = usuario!!.nome_empresa

        if (usuario!!.profile_image_id != null) {
            getImgInStorage()
        }
    }

    fun getImgInStorage() {
        val ref = storageReference!!
            .child("profiles/${auth!!.uid}/${usuario!!.profile_image_id}")

        ref.downloadUrl.addOnCompleteListener {
            if (it.isSuccessful) {
                Picasso.get()
                    .load(it.result)
                    .placeholder(R.drawable.icon_user)
                    .error(R.drawable.ic_info)
                    .into(imgPerfil)
            } else {
                alertSnack("Falha ao carregar a URL", 2, clPerfil)
            }
        }
    }

    private fun initListeners() {
        imgBackPerfil!!.setOnClickListener {
            animateButton(it)
            if (isPasswordEditing) {
                toggleEditPassword()
            } else if (isEmpresaEditing) {
                toggleEditEmpresa()
            } else if (isNameEditing) {
                toggleEditName()
            } else {
                finish()
            }
        }

        clCamera!!.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            val dialogView = this.layoutInflater.inflate(R.layout.dialog_camera_or_gallery, null)
            dialogBuilder.setView(dialogView)
            val dialog = dialogBuilder.create()
            dialog!!.getWindow()?.setBackgroundDrawableResource(R.drawable.transparent_bg)
            dialog.show()

            val imgCancelDialog = dialogView.findViewById<ImageView>(R.id.imgCancelDialog)
            imgCancelDialog.setOnClickListener {
                dialog.dismiss()
            }

            val imgDialogCamera = dialogView.findViewById<ImageView>(R.id.imgDialogCamera)
            imgDialogCamera.setOnClickListener {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED
                ) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 3)
                } else {
                    dialog.dismiss()
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, PICK_IMAGE_CAMERA)
                }
            }

            val imgDialogGallery = dialogView.findViewById<ImageView>(R.id.imgDialogGallery)
            imgDialogGallery.setOnClickListener {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    == PackageManager.PERMISSION_DENIED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        3
                    )
                } else {
                    dialog.dismiss()
                    val pickPhoto =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY)
                }
            }
        }

        imgPerfilEditNome.setOnClickListener {
            if (!isNameEditing) {
                toggleEditName()
            } else {
                if (editNameValidate(editPerfilNome)) {
                    usuario!!.id = auth!!.uid
                    usuario!!.nome = editPerfilNome.text.toString()
                    usuario!!.saveNomeInFirebase().addOnCompleteListener {
                        if (it.isSuccessful) {
                            tvPerfilNome.text = editPerfilNome.text.toString()
                            alertSnack("Novo nome salvo!", 1, clPerfil)
                            toggleEditName()
                        } else {
                            alertSnack("Falha na alteração!", 1, clPerfil)
                        }
                    }
                }
            }
        }

        imgPerfilEditEmpresa.setOnClickListener {
            if (!isEmpresaEditing) {
                toggleEditEmpresa()
            } else {
                if (editNameValidate(editPerfilEmpresa)) {
                    usuario!!.id = auth!!.uid
                    usuario!!.nome_empresa = editPerfilEmpresa.text.toString()
                    usuario!!.saveNomeEmpresaInFirebase().addOnCompleteListener {
                        if (it.isSuccessful) {
                            tvPerfilEmpresa.text = editPerfilEmpresa.text.toString()
                            alertSnack("Novo nome salvo!", 1, clPerfil)
                            toggleEditEmpresa()
                        } else {
                            alertSnack("Falha na alteração!", 1, clPerfil)
                        }
                    }
                }
            }
        }

        imgPerfilEditMap.setOnClickListener {
            if(dialogMapView == null){
                dialogMapBuilder = AlertDialog.Builder(this)
                dialogMapView = this.layoutInflater.inflate(R.layout.dialog_input_map, null)
                dialogMapBuilder!!.setView(dialogMapView)
                dialogMap = dialogMapBuilder!!.create()
                dialogMap!!.getWindow()?.setBackgroundDrawableResource(R.drawable.transparent_bg)

                var mapFragment = supportFragmentManager
                    .findFragmentById(R.id.fragmentDialogMap) as SupportMapFragment
                mapFragment.getMapAsync(this)

                val imgCancelDialog = dialogMapView!!.findViewById<ImageView>(R.id.imgCancelDialog)
                imgCancelDialog.setOnClickListener {
                    dialogMap!!.cancel()
                }

                val tvDialogUseCurrentLocaization =
                    dialogMapView!!.findViewById<TextView>(R.id.tvDialogMapUseCurrentLocaization)
                tvDialogUseCurrentLocaization.setOnClickListener {
                    isUsingCurrentLocalization = true

                    if (currentLatLng != null) {
                        markerHere(currentLatLng!!)
                        animateHere(currentLatLng!!, 19.0f)
                    }

                    animateButton(it)
                }

                val tvDialogMapConcluir = dialogMapView!!.findViewById<TextView>(R.id.tvDialogMapConcluir)
                tvDialogMapConcluir.setOnClickListener {
                    if (isUsingCurrentLocalization) {
                        usuario!!.lat = currentLatLng!!.latitude
                        usuario!!.lng = currentLatLng!!.longitude
                    } else if (!isUsingAccountLocalization) {
                        usuario!!.lat = selectedLatLng!!.latitude
                        usuario!!.lng = selectedLatLng!!.longitude
                    }

                    if (isUsingCurrentLocalization || (!isUsingCurrentLocalization && !isUsingAccountLocalization)) {
                        usuario!!.saveLatInFirebase()
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    usuario!!.saveLngInFirebase()
                                        .addOnCompleteListener {
                                            if (it.isSuccessful) {
                                                alertSnack("Nova localização salva!", 1, clPerfil)
                                            } else {
                                                alertSnack("Não foi possivel salvar a nova localização!", 1, clPerfil)
                                            }
                                        }
                                } else {
                                    alertSnack("Não foi possivel salvar a nova localização!", 1, clPerfil)
                                }
                            }
                    }

                    dialogMap!!.cancel()
                }

                val tvDialogMapSkip = dialogMapView!!.findViewById<TextView>(R.id.tvDialogMapSkip)
                tvDialogMapSkip.setOnClickListener {
                    dialogMap!!.cancel()
                }
            }

            dialogMap!!.show()
        }

        imgPerfilEditPassword.setOnClickListener {
            if (!isPasswordEditing) {
                toggleEditPassword()
            } else {
                if (editPasswordValidate(editPerfilPassword)) {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("Deseja realmente alterar sua senha?")
                        .setCancelable(false)
                        .setPositiveButton("Sim") { confirmDialog, id ->
                            auth!!.currentUser!!.updatePassword(editPerfilPassword.text.toString())
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        alertSnack("Senha alterada!", 1, clPerfil)
                                        toggleEditPassword()
                                    } else {
                                        if (it.exception is FirebaseAuthRecentLoginRequiredException) {
                                            alertSnack(
                                                "Essa ação necessita de um login recente!\nRefaça seu login e tente novamente.",
                                                2,
                                                clPerfil
                                            )

                                            val h = Handler()
                                            val r = Runnable {
                                                auth!!.signOut()
                                                startActivity(
                                                    Intent(
                                                        this,
                                                        LoginActivity::class.java
                                                    )
                                                )
                                                this.finish()
                                            }
                                            h.postDelayed(r, 4600)

                                        } else {
                                            alertSnack(
                                                "Não foi possível alterar sua senha!\nVerifique sua conexão.",
                                                1,
                                                clPerfil
                                            )
                                            toggleEditPassword()
                                        }
                                    }
                                }
                        }
                        .setNegativeButton("Não") { confirmDialog, id ->
                            confirmDialog.cancel()
                            toggleEditPassword()
                        }
                    val alert = builder.create()
                    alert.show()
                }
            }
        }
    }

    private fun toggleEditName() {
        if (isNameEditing) {
            editPerfilNome.setText("")
            tilEditNome.visibility = View.INVISIBLE
            llPerfilNome.visibility = View.VISIBLE
            imgPerfilEditNome.setImageResource(R.drawable.icon_edit)
            animateButton(imgPerfilEditNome)
            isNameEditing = false
        } else {
            editPerfilNome.setText(usuario!!.nome)
            tilEditNome.visibility = View.VISIBLE
            llPerfilNome.visibility = View.INVISIBLE
            imgPerfilEditNome.setImageResource(R.drawable.icon_check_01)
            animateButton(imgPerfilEditNome)
            isNameEditing = true
        }
    }

    private fun toggleEditEmpresa() {
        if (isEmpresaEditing) {
            editPerfilEmpresa.setText("")
            tilEditEmpresa.visibility = View.INVISIBLE
            llPerfilEmpresa.visibility = View.VISIBLE
            imgPerfilEditEmpresa.setImageResource(R.drawable.icon_edit)
            animateButton(imgPerfilEditEmpresa)
            isEmpresaEditing = false
        } else {
            editPerfilEmpresa.setText(usuario!!.nome_empresa)
            tilEditEmpresa.visibility = View.VISIBLE
            llPerfilEmpresa.visibility = View.INVISIBLE
            imgPerfilEditEmpresa.setImageResource(R.drawable.icon_check_01)
            animateButton(imgPerfilEditEmpresa)
            isEmpresaEditing = true
        }
    }

    private fun toggleEditPassword() {
        if (isPasswordEditing) {
            editPerfilPassword.setText("")
            tilEditPassword.visibility = View.INVISIBLE
            llPerfilPassword.visibility = View.VISIBLE
            imgPerfilEditPassword.setImageResource(R.drawable.icon_edit)
            animateButton(imgPerfilEditPassword)
            isPasswordEditing = false
        } else {
            editPerfilPassword.setText("")
            tilEditPassword.visibility = View.VISIBLE
            llPerfilPassword.visibility = View.INVISIBLE
            imgPerfilEditPassword.setImageResource(R.drawable.icon_check_01)
            animateButton(imgPerfilEditPassword)
            isPasswordEditing = true
        }
    }

    private fun editNameValidate(edit: EditText): Boolean {
        var isValided = true
        if (edit.text.isEmpty() || edit.text.length < 6) {
            edit!!.error = "Digite um nome válido!"
            isValided = false
        }
        return isValided
    }

    private fun editPasswordValidate(edit: EditText): Boolean {
        var isValided = true
        if (edit.text.isEmpty() || edit.text.length < 6) {
            edit!!.error = "Digite uma senha válida!"
            isValided = false
        }
        return isValided
    }

    override fun onBackPressed() {
        imgBackPerfil.callOnClick()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PICK_IMAGE_CAMERA -> if (resultCode == RESULT_OK) {
                imgFilePath = data!!.data

                val bitmap: Bitmap = data.getExtras()!!.get("data") as Bitmap
                val bytes = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes)

                Log.e("Activity", "Pick from Camera::>>> ")

                val timeStamp: String =
                    SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val destination = File(
                    Environment.getExternalStorageDirectory().toString() + "/" +
                            getString(R.string.app_name), "IMG_$timeStamp.jpg"
                )
                val fo: FileOutputStream
                try {
                    destination.createNewFile()
                    fo = FileOutputStream(destination)
                    fo.write(bytes.toByteArray())
                    fo.close()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                val imgPath = destination.getAbsolutePath()
                imgPerfil.setImageBitmap(bitmap)
                uploadImage()
            }
            PICK_IMAGE_GALLERY -> if (resultCode == RESULT_OK) {
                imgFilePath = data!!.data
                imgPerfil.setImageURI(imgFilePath)
                uploadImage()
            }
        }
    }

    private fun uploadImage() {
        if (imgFilePath != null) {

            // Code for showing progressDialog while uploading
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Carregando...")
            progressDialog.show()

            val imgID = UUID.randomUUID().toString()

            // Defining the child of storageReference
            val ref: StorageReference = storageReference!!.child(
                "profiles/${auth!!.uid}/${imgID}"
            )

            // adding listeners on upload
            // or failure of image
            ref.putFile(imgFilePath!!)
                .addOnSuccessListener { // Image uploaded successfully
                    // Dismiss dialog
                    progressDialog.dismiss()
                    alertSnack("Imagem carregada!", 1, clPerfil)
                    usuario!!.profile_image_id = imgID
                    usuario!!.saveImageProfileIdInFirebase().addOnCompleteListener {
                        if (it.isSuccessful) alertSnack("Perfil salvo!", 1, clPerfil)
                    }
                }
                .addOnFailureListener { e -> // Error, Image not uploaded
                    progressDialog.dismiss()
                    alertSnack("Failed " + e.message, 1, clPerfil)
                }
                .addOnProgressListener { taskSnapshot ->

                    // Progress Listener for loading
                    // percentage on the dialog box
                    val progress =
                        (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                    progressDialog.setMessage("Carregado: " + progress.toInt() + "%")
                }
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0

        if (isUsingAccountLocalization) {
            if (usuario!!.lat != null && usuario!!.lng != null) {
                markerHere(usuario!!.getLatLng()!!)
                animateHere(usuario!!.getLatLng()!!, 19.0f)
            } else {
                isUsingCurrentLocalization = true
            }
        }

        googleMap?.let {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat
                    .requestPermissions(
                        this,
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
                if (isUsingCurrentLocalization) {
                    currentLatLng = LatLng(arg0.latitude, arg0.longitude)

                    markerHere(currentLatLng!!)
                    animateHere(currentLatLng!!, 19.0f)
                }
            }

            it.setOnMapClickListener { point ->
                markerHere(point)
                animateHere(point, 19.0f)

                isUsingCurrentLocalization = false
                isUsingAccountLocalization = false
                selectedLatLng = point
            }
        }
    }

    private fun markerHere(point: LatLng) {
        googleMap!!.clear()

        googleMap!!.addMarker(
            MarkerOptions()
                .position(point)
                .title("Marcar aqui?")
        )
    }

    private fun animateHere(point: LatLng, zoomLevel: Float) {
        googleMap!!.animateCamera(
            CameraUpdateFactory
                .newLatLngZoom(point, zoomLevel)
        )
    }
}