package com.empreendapp.collev.util

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowManager
import com.empreendapp.collev.R

class DefaultLayout {
    companion object {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        fun setStatusBarBorderRadiusWhite(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = activity.window
                val background = activity.resources.getDrawable(R.drawable.pag_bg_01)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = activity.resources.getColor(android.R.color.transparent)
                window.navigationBarColor = activity.resources.getColor(android.R.color.transparent)
                window.setBackgroundDrawable(background)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }
}