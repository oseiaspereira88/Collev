package com.empreendapp.collev.ui.system

import android.content.ComponentName
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.empreendapp.collev.R
import android.content.Intent
import android.net.Uri
import android.telephony.PhoneNumberUtils


class ChatActivity : AppCompatActivity() {
    var imgBackQuemSomos: ImageView? = null
    var clZap: ConstraintLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        initViews()
        setAllListeners()
    }

    private fun initViews() {
        imgBackQuemSomos = findViewById(R.id.imgBackQuemSomos)
        clZap = findViewById(R.id.clZap)
    }

    private fun setAllListeners() {
        imgBackQuemSomos!!.setOnClickListener{
            finish()
        }

        clZap!!.setOnClickListener{
            val numeroSuportZap = "8494310065"
            val it = Intent(Intent.ACTION_VIEW)
            it.setComponent(ComponentName("com.whatsapp", "com.whatsapp.Conversation"))
            it.putExtra("jid", PhoneNumberUtils.stripSeparators("55"+numeroSuportZap) + "@s.whatsapp.net")
            startActivity(it)
        }
    }

    private fun ligarViaZap(){
        val numeroSuportZap = "084994310065"
        var uri = Uri.parse("tel:" + numeroSuportZap)
        var it = Intent(Intent.ACTION_DIAL, uri)
        startActivity(it)
    }
}