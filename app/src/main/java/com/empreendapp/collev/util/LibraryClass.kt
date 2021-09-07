package com.empreendapp.collev.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.FirebaseDatabase;

public class LibraryClass extends Application {
    private static FirebaseDatabase firebaseBD;
    public static String PREF = "com.empreendapp.collev.PREF";

    public static FirebaseDatabase getFirebaseDB(){
        if(firebaseBD==null){
            firebaseBD = FirebaseDatabase.getInstance();
        }
        return firebaseBD;
    }

    public static void saveSP(Context ctx, String key, String value){
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    public static String getSP(Context ctx, String key){
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String token = sp.getString(key,"");
        return token;
    }
}
