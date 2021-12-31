package com.empreendapp.collev.util

import android.widget.EditText
import kotlinx.android.synthetic.main.activity_login.*

class InputValidate {
    companion object{
        fun validateEmail(edit: EditText): Boolean {
            var isValided = true
            if (edit.text.isEmpty() || edit.text.length < 10 ||
                !edit.text.contains('@') || !edit.text.contains('.')
            ) {
                edit?.error = "Digite um email válido!"
                isValided = false
            }
            return isValided
        }

         fun validateSenha(edit: EditText): Boolean {
            var isValided = true
            if (edit.text.isEmpty() || edit.text.length < 6) {
                edit?.error = "Digite uma senha válida!"
                isValided = false
            }
            return isValided
        }
    }
}