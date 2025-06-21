package com.empreendapp.collev.ui.common

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import com.empreendapp.collev.adapters.pagers.ColetorFragmentPagerAdapter
import android.os.Bundle
import com.empreendapp.collev.R
import android.view.KeyEvent
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.viewpager.widget.ViewPager.*
import com.empreendapp.collev.adapters.pagers.AdmFragmentPagerAdapter
import com.empreendapp.collev.adapters.pagers.ColaboradorFragmentPagerAdapter
import com.empreendapp.collev.util.DefaultLayout.Companion.setStatusBarBorderRadiusWhite
import com.empreendapp.collev.util.LibraryClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class MainActivity : AppCompatActivity()  {
    private var pager: ViewPager? = null
    private var database: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    private var tvTitle: TextView? = null
    private var tabLayout: TabLayout? = null
    private var pageProgressBar: ProgressBar? = null
    private var colaboradorAdapter: ColaboradorFragmentPagerAdapter? = null
    private var coletorAdapter: ColetorFragmentPagerAdapter? = null
    private var admAdapter: AdmFragmentPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setStatusBarBorderRadiusWhite(this)
        initFirebase()
        initViews()
        // Abertura da funcionalidade de corridas (exemplo)
        startActivity(android.content.Intent(this, com.empreendapp.collev.ui.passageiro.SolicitarCorridaActivity::class.java))
    }

    private fun initFirebase() {
        database = LibraryClass.firebaseDB?.reference
        auth = FirebaseAuth.getInstance()
    }

    private fun initViews() {
        pager = findViewById<View>(R.id.pager) as ViewPager
        tvTitle = findViewById<View>(R.id.tvTabTitle) as TextView
        tabLayout = findViewById<View>(R.id.tab_layout) as TabLayout
        pageProgressBar = findViewById<View>(R.id.pageProgressBar) as ProgressBar

        if(auth?.currentUser != null){
            getUserTypeAndPage()
        }
    }

    private fun getUserTypeAndPage(){
        database!!.child("users").child(auth?.uid.toString()).get()
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    costruirPaginacao(java.lang.String.valueOf(task.result?.child("tipo")?.getValue()))
                    pageProgressBar?.visibility = View.GONE
                } else if(task.isCanceled){
                    costruirPaginacao("undefined")
                    pageProgressBar?.clearAnimation()
                }
            }
    }

    private fun costruirPaginacao(userType: String){
        when(userType){
            "Colaborador" -> {
                colaboradorAdapter = ColaboradorFragmentPagerAdapter(supportFragmentManager)
                pager?.adapter = colaboradorAdapter
                tabLayout?.setupWithViewPager(pager)
                pager?.addOnPageChangeListener(onPageChangeListener)

                tabLayout?.getTabAt(0)?.setIcon(R.drawable.icon_home)
                tabLayout?.getTabAt(1)?.setIcon(R.drawable.icon_notifi)
                tabLayout?.getTabAt(2)?.setIcon(R.drawable.icon_menu_01)
            }
            "Coletor" -> {
                coletorAdapter = ColetorFragmentPagerAdapter(supportFragmentManager)
                pager?.adapter = coletorAdapter
                tabLayout?.setupWithViewPager(pager)
                pager?.addOnPageChangeListener(onPageChangeListener)

                tabLayout?.getTabAt(0)?.setIcon(R.drawable.icon_home)
                tabLayout?.getTabAt(1)?.setIcon(R.drawable.icon_notifi)
                tabLayout?.getTabAt(2)?.setIcon(R.drawable.icon_menu_01)
            }
            "Administrador" -> {
                admAdapter = AdmFragmentPagerAdapter(supportFragmentManager)
                pager?.adapter = admAdapter
                tabLayout?.setupWithViewPager(pager)
                pager?.addOnPageChangeListener(onPageChangeListener)

                tabLayout?.getTabAt(0)?.setIcon(R.drawable.icon_home)
                tabLayout?.getTabAt(1)?.setIcon(R.drawable.icon_notifi)
                tabLayout?.getTabAt(2)?.setIcon(R.drawable.icon_menu_01)
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
                    0 -> tvTitle!!.text = "Coletas" //solicitações de coletas
                    1 -> tvTitle!!.text = "Notificações"
                    2 -> tvTitle!!.text = "Menu"
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        }

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
}