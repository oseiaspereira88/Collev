package com.empreendapp.collev.ui.system

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.empreendapp.collev.R

class ParceirosActivity : AppCompatActivity() {
    private var imgBackParceiros: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parceiros)
        initViews()
    }

    private fun initViews() {
        imgBackParceiros = findViewById(R.id.imgBackParceiros)

        imgBackParceiros!!.setOnClickListener{
            finish()
        }
    }
}