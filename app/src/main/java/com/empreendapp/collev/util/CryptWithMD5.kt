package com.empreendapp.collev.util;

import java.security.*;
import java.math.*;

public class CryptWithMD5 {
    public static String gerarMD5Hash(String txt) throws NoSuchAlgorithmException {
        MessageDigest m=MessageDigest.getInstance("MD5");
        m.update(txt.getBytes(),0,txt.length());
        System.out.println("MD5: "+new BigInteger(1,m.digest()).toString(16));
        return new BigInteger(1,m.digest()).toString(16);
    }
}
