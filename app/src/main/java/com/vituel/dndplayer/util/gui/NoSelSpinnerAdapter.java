package com.vituel.dndplayer.util.gui;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import static com.vituel.dndplayer.util.font.FontUtil.MAIN_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.setFontRecursively;

/**
 * Adds a "no selection" item.
 *
 * Created by Victor on 08/03/14.
 */
public class NoSelSpinnerAdapter implements SpinnerAdapter {

    private Context context;
    private SpinnerAdapter base;
    private final String defaultValue;

    public NoSelSpinnerAdapter(Context context, SpinnerAdapter base) {
        this.context = context;
        this.base = base;
        this.defaultValue = "";
    }

    @Override
    public int getCount() {
        return base.getCount() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0 || position == -1) {
            return null;
        }
        return base.getItem(position - 1);
    }

    @Override
    public long getItemId(int position) {
        if (position == 0 || position == -1) {
            return -1;
        }
        return base.getItemId(position - 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == -1) {
            return -1;
        }
        return base.getItemViewType(position - 1);
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
        setFontRecursively(context, v, MAIN_FONT);
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
        setFontRecursively(context, v, MAIN_FONT);
        return v;
    }

    @Override
    public int getViewTypeCount() {
        return base.getViewTypeCount();
    }

    @Override
    public boolean hasStableIds() {
        return base.hasStableIds();
    }

    @Override
    public boolean isEmpty() {
        return base.isEmpty();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        base.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        base.unregisterDataSetObserver(observer);
    }

}