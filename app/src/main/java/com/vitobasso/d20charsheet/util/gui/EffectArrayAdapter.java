package com.vitobasso.d20charsheet.util.gui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.model.effect.EffectSource;
import com.vitobasso.d20charsheet.util.font.FontUtil;

import java.util.List;

/**
 * Created by Victor on 20/04/14.
 */
public class EffectArrayAdapter<T extends EffectSource> extends ArrayAdapter<T> {

    private FontUtil fontUtil;

    public EffectArrayAdapter(Context context, List<T> objects) {
        this(context, R.layout.effect_row, objects);
    }

    public EffectArrayAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, R.id.name, objects);
        fontUtil = new FontUtil(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewGroup group = (ViewGroup) super.getView(position, convertView, parent);
        EffectSource source = getItem(position);

        EffectPopulator populator = new EffectPopulator(getContext());
        populator.populate(source, group);

        fontUtil.setFontRecursively(group);
        return group;
    }

}
