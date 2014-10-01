package com.vituel.dndplayer.util.gui;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vituel.dndplayer.util.i18n.EnumI18n;

/**
 * Created by Victor on 29/09/2014.
 */
public class EnumI18nSpinnerAdapter extends ArrayAdapter<Enum> {

    private EnumI18n i18n;
    private Enum[] items;

    public EnumI18nSpinnerAdapter(Context context, int resource, Enum[] objects) {
        super(context, resource, objects);
        i18n = new EnumI18n(context);
        this.items = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        return setupView(position, view);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        return setupView(position, view);
    }

    private View setupView(int position, TextView view) {
        Enum item = items[position];
        CharSequence str = i18n.get(item);
        if(str == null){
            str = item.toString();
            Log.w(getClass().getSimpleName(), "Couldn't convert string: " + item.toString());
        }
        view.setText(str);
        return view;
    }


}
