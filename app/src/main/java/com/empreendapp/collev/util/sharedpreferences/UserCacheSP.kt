package com.empreendapp.collev.util.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.empreendapp.collev.model.User
import com.empreendapp.collev.util.sharedpreferences.DefaultsSP.Companion.PREF
import com.empreendapp.collev.util.sharedpreferences.DefaultsSP.Companion.USER_CACHE

class UserCacheSP {
    companion object {
        val CACHE_NOME = USER_CACHE + "_NOME"
        val CACHE_EMAIL = USER_CACHE + "_EMAIL"
        val CACHE_EMPRESA_NOME = USER_CACHE + "_EMPRESA_NOME"
        val CACHE_PROFILE_IMAGE_ID = USER_CACHE + "_PROFILE_IMAGE_ID"
        val CACHE_INITIALIZED_PROFILE = USER_CACHE + "_INITIALIZED_PROFILE"

        open fun saveCache(user: User, ctx: Context) {
            val sp: SharedPreferences = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            sp.edit().putString(CACHE_NOME, user.nome).apply()
            sp.edit().putString(CACHE_EMAIL, user.email).apply()
            sp.edit().putString(CACHE_EMPRESA_NOME, user.nome_empresa).apply()
            sp.edit().putString(CACHE_PROFILE_IMAGE_ID, user.profile_image_id).apply()
            sp.edit().putBoolean(CACHE_INITIALIZED_PROFILE, user.has_profile_initialized!!).apply()
        }
    }

    open fun getCache(userCacheType: UserCacheType, ctx: Context): String? {
        val sp: SharedPreferences = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        var configValue: String? = null

        when (userCacheType) {
            UserCacheType.NOME -> configValue = sp.getString(CACHE_NOME, "")
            UserCacheType.EMAIL -> configValue = sp.getString(CACHE_EMAIL, "")
            UserCacheType.EMPRESA_NOME -> configValue = sp.getString(CACHE_EMPRESA_NOME, "")
            UserCacheType.PROFILE_IMAGE_ID -> configValue = sp.getString(CACHE_PROFILE_IMAGE_ID, "")
            UserCacheType.INITIALIZED_PROFILE -> configValue = sp.getString(CACHE_INITIALIZED_PROFILE, "")
        }

        return configValue
    }
}