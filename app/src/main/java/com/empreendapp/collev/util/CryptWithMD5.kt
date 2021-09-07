package com.empreendapp.collev.util

import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.Throws

object CryptWithMD5 {
    @Throws(NoSuchAlgorithmException::class)
    fun gerarMD5Hash(txt: String): String {
        val m = MessageDigest.getInstance("MD5")
        m.update(txt.toByteArray(), 0, txt.length)
        println("MD5: " + BigInteger(1, m.digest()).toString(16))
        return BigInteger(1, m.digest()).toString(16)
    }
}