package com.vituel.dndplayer.activity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.model.AbstractEffect;
import com.vituel.dndplayer.model.Modifier;
import com.vituel.dndplayer.util.i18n.ModifierStringConverter;

import static com.vituel.dndplayer.util.ActivityUtil.findView;
import static com.vituel.dndplayer.util.ActivityUtil.populateTextView;

/**
 * Created by Victor on 06/09/14.
 */
public class EffectPopulator {

    private Context context;
    private ModifierStringConverter modConv;

    public EffectPopulator(Context context) {
        this.context = context;
        this.modConv = new ModifierStringConverter(context);
    }

    public void populate(AbstractEffect effect, ViewGroup group){
        populateTextView(group, R.id.name, effect.getName());

        TextView[] modViews = new TextView[6];
        modViews[0] = findView(group, R.id.mod1);
        modViews[1] = findView(group, R.id.mod2);
        modViews[2] = findView(group, R.id.mod3);
        modViews[3] = findView(group, R.id.mod4);
        modViews[4] = findView(group, R.id.mod5);
        modViews[5] = findView(group, R.id.mod6);

        //populate mods
        for (int i = 0; i < effect.getModifiers().size() && i < modViews.length; i++) {
            TextView modView = modViews[i];
            Modifier mod = effect.getModifiers().get(i);
            modView.setText(modConv.getShortString(mod));
            if (mod.isBonus()) {
                modView.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
            } else {
                modView.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            }
        }

        //clear empty views
        for (int i = effect.getModifiers().size(); i < modViews.length; i++) {
            modViews[i].setText("");
        }

        //show necessary rows, hide excess ones
        View row2 = group.findViewById(R.id.row2);
        row2.setVisibility(effect.getModifiers().size() <= 3 ? View.GONE : View.VISIBLE);
        View row1 = group.findViewById(R.id.row1);
        row1.setVisibility(effect.getModifiers().isEmpty() ? View.GONE : View.VISIBLE);
    }

}
