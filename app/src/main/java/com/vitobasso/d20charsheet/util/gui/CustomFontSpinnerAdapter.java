package com.vitobasso.d20charsheet.util.gui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;

import com.vitobasso.d20charsheet.util.font.FontUtil;

/**
 * Created by Victor on 04/10/2014.
 */
public class CustomFontSpinnerAdapter extends SpinnerAdapterWrapper {

    private FontUtil fontUtil;

    public CustomFontSpinnerAdapter(Context context, SpinnerAdapter base) {
        super(context, base);
        fontUtil = new FontUtil(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        fontUtil.setFontRecursively(view);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        fontUtil.setFontRecursively(view);
        return view;
    }
}
