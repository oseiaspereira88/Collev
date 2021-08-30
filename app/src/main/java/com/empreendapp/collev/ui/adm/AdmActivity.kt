package com.empreendapp.collev.ui.adm

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import com.empreendapp.collev.adapters.ColetorFragmentPagerAdapter
import android.os.Bundle
import com.empreendapp.collev.R
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.cardview.widget.CardView
import android.view.KeyEvent
import android.view.View
import com.daimajia.androidanimations.library.YoYo
import com.daimajia.androidanimations.library.Techniques
import com.empreendapp.collev.adapters.AdmFragmentPagerAdapter
import com.empreendapp.collev.ui.system.InitPerfilActivity

class AdmActivity : AppCompatActivity() {
    private var pager: ViewPager? = null
    private var tvOptionTitle: TextView? = null
    private var tabLayout: TabLayout? = null
    private var pagerAdapter: AdmFragmentPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        InitPerfilActivity.setStatusBarBorderRadius(this)
        initViews()
    }

    private fun initViews() {
        pager = findViewById<View>(R.id.pager) as ViewPager
        tvOptionTitle = findViewById<View>(R.id.tvTabTitle) as TextView
        tabLayout = findViewById<View>(R.id.tab_layout) as TabLayout
        pagerAdapter = AdmFragmentPagerAdapter(supportFragmentManager)
        pager?.adapter = pagerAdapter
        tabLayout?.setupWithViewPager(pager)
        pager?.addOnPageChangeListener(onPageChangeListener)

        tabLayout?.getTabAt(0)?.setIcon(R.drawable.icon_home);
        tabLayout?.getTabAt(1)?.setIcon(R.drawable.icon_notifi);
        tabLayout?.getTabAt(2)?.setIcon(R.drawable.icon_menu04);
    }

    private val onPageChangeListener: OnPageChangeListener
        private get() = object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> tvOptionTitle!!.text = "Gerencia de Usuários" //solicitações de coletas
                    1 -> tvOptionTitle!!.text = "Notificações"
                    2 -> tvOptionTitle!!.text = "Menu"
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        }

    fun openProfileDialogCreator() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Selecione uma das Opções")
        val view = View.inflate(this, R.layout.perfil_option_model, null)
        val option1 = findViewById<View>(R.id.cv_option_coletor) as CardView
        val option2 = findViewById<View>(R.id.cv_option_voluntario) as CardView
        val onClick = View.OnClickListener { v ->
            val cv = v as CardView
            //cv.setBackground();
        }
        builder.setView(view)

        // Set up the buttons
        builder.setPositiveButton("Confirmar") { dialog, which ->
            //proxima questão ou se for a última questão, finaliza o perfil;
        }
        builder.setNegativeButton("Cancelar") { dialog, which -> dialog.cancel() }
        builder.show()
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

    fun animateButton(view: View?) {
        YoYo.with(Techniques.RotateIn)
                .duration(700)
                .repeat(0)
                .playOn(view)
    }
}