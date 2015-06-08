package com.vitobasso.d20charsheet.util.gui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;

import static com.vitobasso.d20charsheet.util.font.FontUtil.MAIN_FONT;
import static com.vitobasso.d20charsheet.util.font.FontUtil.setFontRecursively;

/**
 * Created by Victor on 04/10/2014.
 */
public class CustomFontSpinnerAdapter extends SpinnerAdapterWrapper {

    public CustomFontSpinnerAdapter(Context context, SpinnerAdapter base) {
        super(context, base);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        setFontRecursively(context, view, MAIN_FONT);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        setFontRecursively(context, view, MAIN_FONT);
        return view;
    }
}
