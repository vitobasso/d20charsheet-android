package com.vituel.dndplayer.activity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.model.EffectSource;

import java.util.List;

import static com.vituel.dndplayer.util.font.FontUtil.MAIN_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.setFontRecursively;

/**
 * Created by Victor on 20/04/14.
 */
public class EffectArrayAdapter<T extends EffectSource> extends ArrayAdapter<T> {

    public EffectArrayAdapter(Context context, List<T> objects) {
        super(context, R.layout.effect_row, R.id.name, objects);
    }

    public EffectArrayAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, R.id.name, objects);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewGroup group = (ViewGroup) super.getView(position, convertView, parent);
        assert group != null;
        EffectSource source = getItem(position);

        EffectPopulator populator = new EffectPopulator(getContext());
        populator.populate(source, group);

        setFontRecursively(getContext(), group, MAIN_FONT);
        return group;
    }

}
