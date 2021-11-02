package com.empreendapp.collev.ui.system

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.empreendapp.collev.R

class QuemsomosActivity : AppCompatActivity() {
    var imgBackQuemSomos: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quemsomos)
        initViews()
    }

    private fun initViews() {
        imgBackQuemSomos = findViewById(R.id.imgBackQuemSomos)

        imgBackQuemSomos!!.setOnClickListener{
            finish()
        }
    }
}