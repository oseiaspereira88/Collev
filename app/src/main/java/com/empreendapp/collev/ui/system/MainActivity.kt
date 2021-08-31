package com.empreendapp.collev.ui.system

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import com.empreendapp.collev.adapters.ColetorFragmentPagerAdapter
import android.os.Bundle
import com.empreendapp.collev.R
import androidx.cardview.widget.CardView
import android.view.KeyEvent
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.viewpager.widget.ViewPager.*
import com.daimajia.androidanimations.library.YoYo
import com.daimajia.androidanimations.library.Techniques
import com.empreendapp.collev.adapters.AdmFragmentPagerAdapter
import com.empreendapp.collev.adapters.VoluntarioFragmentPagerAdapter
import com.empreendapp.collev.util.LibraryClass
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private var pager: ViewPager? = null
    private var database: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    private var tvOptionTitle: TextView? = null
    private var tabLayout: TabLayout? = null
    private var pageProgressBar: ProgressBar? = null
    private var voluntarioAdapter: VoluntarioFragmentPagerAdapter? = null
    private var coletorAdapter: ColetorFragmentPagerAdapter? = null
    private var admAdapter: AdmFragmentPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        InitPerfilActivity.setStatusBarBorderRadius(this)
        initFirebase()
        initViews()
    }

    private fun initFirebase() {
        database = LibraryClass.getFirebaseDB().reference
        auth = FirebaseAuth.getInstance()
    }

    private fun initViews() {
        pager = findViewById<View>(R.id.pager) as ViewPager
        tvOptionTitle = findViewById<View>(R.id.tvTabTitle) as TextView
        tabLayout = findViewById<View>(R.id.tab_layout) as TabLayout
        pageProgressBar = findViewById<View>(R.id.pageProgressBar) as ProgressBar

        if(auth?.currentUser != null){
            getUserTypeAndPage()
        }
    }

    private fun getUserTypeAndPage(){
        database!!.child("users").child(auth?.uid.toString()).get()
            .addOnCompleteListener(OnCompleteListener<DataSnapshot?> { task ->
                if (task.isSuccessful) {
                    costruirPaginacao(java.lang.String.valueOf(task.result?.child("tipo")?.getValue()))
                    pageProgressBar?.visibility = View.GONE
                } else if(task.isCanceled){
                    costruirPaginacao("undefined")
                    pageProgressBar?.clearAnimation()
                }
            })
    }

    private fun costruirPaginacao(userType: String){
        when(userType){
            "Voluntário" -> {
                voluntarioAdapter = VoluntarioFragmentPagerAdapter(supportFragmentManager)
                pager?.adapter = voluntarioAdapter
                tabLayout?.setupWithViewPager(pager)
                pager?.addOnPageChangeListener(onPageChangeListener)

                tabLayout?.getTabAt(0)?.setIcon(R.drawable.icon_home)
                tabLayout?.getTabAt(1)?.setIcon(R.drawable.icon_notifi)
                tabLayout?.getTabAt(2)?.setIcon(R.drawable.icon_menu04)
            }
            "Coletor" -> {
                coletorAdapter = ColetorFragmentPagerAdapter(supportFragmentManager)
                pager?.adapter = coletorAdapter
                tabLayout?.setupWithViewPager(pager)
                pager?.addOnPageChangeListener(onPageChangeListener)

                tabLayout?.getTabAt(0)?.setIcon(R.drawable.icon_home)
                tabLayout?.getTabAt(1)?.setIcon(R.drawable.icon_notifi)
                tabLayout?.getTabAt(2)?.setIcon(R.drawable.icon_menu04)
            }
            "Administrador" -> {
                admAdapter = AdmFragmentPagerAdapter(supportFragmentManager)
                pager?.adapter = admAdapter
                tabLayout?.setupWithViewPager(pager)
                pager?.addOnPageChangeListener(onPageChangeListener)

                tabLayout?.getTabAt(0)?.setIcon(R.drawable.icon_home)
                tabLayout?.getTabAt(1)?.setIcon(R.drawable.icon_notifi)
                tabLayout?.getTabAt(2)?.setIcon(R.drawable.icon_menu04)
            }
            else -> {
                Toast.makeText(this, "Error: Tipo de Usuário não identificado!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private val onPageChangeListener: OnPageChangeListener
        private get() = object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> tvOptionTitle!!.text = "Coletas" //solicitações de coletas
                    1 -> tvOptionTitle!!.text = "Notificações"
                    2 -> tvOptionTitle!!.text = "Menu"
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        }

//    fun openProfileDialogCreator() {
//        val builder = AlertDialog.Builder(this)
//        builder.setTitle("Selecione uma das Opções")
//        val view = View.inflate(this, R.layout.perfil_option_model, null)
//        val option1 = findViewById<View>(R.id.cv_option_coletor) as CardView
//        val option2 = findViewById<View>(R.id.cv_option_voluntario) as CardView
//        val onClick = View.OnClickListener { v ->
//            val cv = v as CardView
//            //cv.setBackground();
//        }
//        builder.setView(view)
//
//        // Set up the buttons
//        builder.setPositiveButton("Confirmar") { dialog, which ->
//            //proxima questão ou se for a última questão, finaliza o perfil;
//        }
//        builder.setNegativeButton("Cancelar") { dialog, which -> dialog.cancel() }
//        builder.show()
//    }
//

    // Código botão Voltar
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            checkExit()
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    private fun checkExit() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Deseja realmente sair?")
                .setCancelable(false)
                .setPositiveButton("Sim") { dialog, id -> finish() }
                .setNegativeButton("Não") { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }

    fun animateButton(view: View?) {
        YoYo.with(Techniques.RotateIn)
                .duration(700)
                .repeat(0)
                .playOn(view)
    }
}