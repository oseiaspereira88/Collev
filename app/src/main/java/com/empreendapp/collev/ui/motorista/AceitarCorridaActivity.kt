package com.empreendapp.collev.ui.motorista

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.empreendapp.collev.R

/**
 * Activity simples para que o motorista visualize e aceite corridas.
 */
class AceitarCorridaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aceitar_corrida)
    }
}
