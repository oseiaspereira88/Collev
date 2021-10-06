package com.empreendapp.collev.ui.system

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.RadioGroup
import android.widget.RadioButton
import android.os.Bundle
import com.empreendapp.collev.R
import android.content.Intent
import android.os.Handler
import android.view.View
import android.widget.ImageView
import com.daimajia.androidanimations.library.YoYo
import com.daimajia.androidanimations.library.Techniques
import com.empreendapp.collev.util.DefaultLayout.Companion.setStatusBarBorderRadiusWhite

class IntroActivity : AppCompatActivity() {
    //IntroActivityBinding binding;
    var imgLogo: ImageView? = null
    var tvBoasVindas: TextView? = null
    var rgSlide: RadioGroup? = null
    var bSlide1: RadioButton? = null
    var bSlide2: RadioButton? = null
    var bSlide3: RadioButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        setStatusBarBorderRadiusWhite(this)
        initViews()
        showIntroAnimate()
    }

    private fun initViews() {
        imgLogo = findViewById<View>(R.id.imgLogo) as ImageView
        tvBoasVindas = findViewById<View>(R.id.tvBoasVindas) as TextView
        rgSlide = findViewById<View>(R.id.rgSlide) as RadioGroup
        bSlide1 = findViewById<View>(R.id.bSlide1) as RadioButton
        bSlide2 = findViewById<View>(R.id.bSlide2) as RadioButton
        bSlide3 = findViewById<View>(R.id.bSlide3) as RadioButton

        val onClickListener = View.OnClickListener { v ->
            val rButton = v as RadioButton
            rgSlide!!.clearCheck()
            rButton.isChecked = true
        }

        bSlide1!!.setOnClickListener(onClickListener)
        bSlide2!!.setOnClickListener(onClickListener)
        bSlide3!!.setOnClickListener(onClickListener)
    }

    private fun showIntroAnimate() {
        imgLogo!!.visibility = View.VISIBLE

        //play animate logo
        YoYo.with(Techniques.FadeIn).duration(1600).repeat(0).playOn(imgLogo)

        //contar tempo (700ms)
        val handler = Handler()
        val r = Runnable {
            tvBoasVindas!!.visibility = View.VISIBLE

            //play animate logo
            YoYo.with(Techniques.FadeIn).duration(800).repeat(0).playOn(tvBoasVindas)
            val handler2 = Handler()
            val r2 = Runnable {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            handler2.postDelayed(r2, 1600)
        }
        handler.postDelayed(r, 2000)
    }
}