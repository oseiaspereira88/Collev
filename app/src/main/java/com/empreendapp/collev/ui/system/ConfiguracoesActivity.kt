package com.empreendapp.collev.ui.system

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.empreendapp.collev.R

class ConfiguracoesActivity : AppCompatActivity() {
    private var imgBackConfiguracoes: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracoes)
        initViews()
    }

    private fun initViews() {
        imgBackConfiguracoes = findViewById(R.id.imgBackConfiguracoes)

        imgBackConfiguracoes!!.setOnClickListener{
            finish()
        }
    }
}