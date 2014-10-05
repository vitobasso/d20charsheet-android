package com.vituel.dndplayer.util.gui;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;

/**
 * Adds a "no selection" item.
 *
 * Created by Victor on 08/03/14.
 */
public class SpinnerAdapterWrapper implements SpinnerAdapter {

    protected Context context;
    protected SpinnerAdapter base;

    public SpinnerAdapterWrapper(Context context, SpinnerAdapter base) {
        this.context = context;
        this.base = base;
    }

    @Override
    public int getCount() {
        return base.getCount();
    }

    @Override
    public Object getItem(int position) {
        return base.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return base.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return base.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return base.getView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return base.getView(position, convertView, parent);
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