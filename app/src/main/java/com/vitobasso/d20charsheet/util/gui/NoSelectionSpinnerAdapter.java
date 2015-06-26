package com.vitobasso.d20charsheet.util.gui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.vitobasso.d20charsheet.util.font.FontUtil;


/**
 * Adds a "no selection" item.
 *
 * Created by Victor on 08/03/14.
 */
public class NoSelectionSpinnerAdapter extends SpinnerAdapterWrapper {

    private final String defaultValue;
    private FontUtil fontUtil;

    public NoSelectionSpinnerAdapter(Context context, SpinnerAdapter base) {
        super(context, base);
        this.defaultValue = "";
        this.fontUtil = new FontUtil(context);
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0 || position == -1) {
            return null;
        }
        return super.getItem(position - 1);
    }

    @Override
    public long getItemId(int position) {
        if (position == 0 || position == -1) {
            return -1;
        }
        return super.getItemId(position - 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == -1) {
            return -1;
        }
        return super.getItemViewType(position - 1);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView v;
        if (position == 0) {
            v = (TextView) base.getView(position, convertView, parent);
            v.setText(defaultValue);
        } else {
            v = (TextView) base.getView(position - 1, convertView, parent);
        }
        fontUtil.setFontRecursively(v);
        return v;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView v;
        if (position == 0) {
            v = (TextView) base.getDropDownView(position, convertView, parent);
            v.setText(defaultValue);
        } else {
            v = (TextView) base.getDropDownView(position - 1, convertView, parent);
        }
        fontUtil.setFontRecursively(v);
        return v;
    }

}