package com.empreendapp.collev.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.empreendapp.collev.R;

public class PerfilOptionAdapter extends PagerAdapter {
    String optionsTitles [] = {"Selecione o perfil de sua empresa", "Selecione o nome e localização", "Selecione o tamanho do recipiente"};
    TextView tvOptionTitle;
    Context ctx;

    public PerfilOptionAdapter(Context ctx, TextView tvOptionTitle){
        this.ctx = ctx;
        this.tvOptionTitle = tvOptionTitle;
    }

    @Override
    public int getCount() {
        return optionsTitles.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return true;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LinearLayout ll = new LinearLayout(ctx);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setLayoutParams(lp);

        View pagOption = View.inflate(ctx, R.layout.perfil_option_model, null);
        ll.addView(pagOption);

        container.addView(ll);

        return ll;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {

        container.removeView((View) view);
    }
}
