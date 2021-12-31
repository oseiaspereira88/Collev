package com.empreendapp.collev.ui.common.dialogs

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.empreendapp.collev.R

class ColetaDialog: DialogFragment() {
    var daysList: ArrayList<String>? = null
    var adapter: ArrayAdapter<String>? = null
    var itemId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.transparent_bg)
        return inflater.inflate(R.layout.dialog_coleta_solicitada, container, false)
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.86).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    fun mount(itemId: String, daysList: Array<String>, act: Activity){
        adapter = ArrayAdapter(act, android.R.layout.simple_spinner_item, daysList)
        adapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dialog!!.findViewById<Spinner>(R.id.spDias)!!.adapter = adapter
    }

}