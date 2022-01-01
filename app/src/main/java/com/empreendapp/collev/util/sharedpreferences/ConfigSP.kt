package com.empreendapp.collev.util.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.empreendapp.collev.util.sharedpreferences.DefaultsSP.Companion.CONFIG
import com.empreendapp.collev.util.sharedpreferences.DefaultsSP.Companion.PREF

class ConfigSP {
    companion object {
        val USER_INITIALIZED = CONFIG + "_USER_INITIALIZED"

        open fun setConfigSP(configType: ConfigType, configValue: String, ctx: Context) {
            val sp: SharedPreferences = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)

            when(configType){
                ConfigType.USER_INITIALIZED -> {
                    sp.edit().putString(USER_INITIALIZED, configValue).apply()
                }
            }
        }

        open fun getConfigSP(configType: ConfigType, ctx: Context): String? {
            val sp: SharedPreferences = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            var configValue: String? = null

            when(configType){
                ConfigType.USER_INITIALIZED -> {
                    configValue = sp.getString(USER_INITIALIZED, "")
                }
            }

            return configValue
        }
    }
}