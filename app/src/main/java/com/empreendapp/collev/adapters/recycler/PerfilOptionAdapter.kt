package com.empreendapp.collev.adapters.recycler

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import android.view.ViewGroup
import android.widget.LinearLayout
import com.empreendapp.collev.R

class PerfilOptionAdapter(var ctx: Context, var tvOptionTitle: TextView) : PagerAdapter() {
    var optionsTitles = arrayOf("Selecione o perfil de sua empresa", "Selecione o nome e localização", "Selecione o tamanho do recipiente")
    override fun getCount(): Int {
        return optionsTitles.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return true
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val ll = LinearLayout(ctx)
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        ll.layoutParams = lp
        val pagOption = View.inflate(ctx, R.layout.perfil_option_model, null)
        ll.addView(pagOption)
        container.addView(ll)
        return ll
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }
}