package com.vitobasso.d20charsheet.util.gui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.model.effect.EffectSource;
import com.vitobasso.d20charsheet.model.effect.Modifier;
import com.vitobasso.d20charsheet.util.business.ModifierHelper;
import com.vitobasso.d20charsheet.util.i18n.ModifierStringConverter;

import java.util.ArrayList;
import java.util.List;

import static com.vitobasso.d20charsheet.util.app.ActivityUtil.findView;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.populateTextView;

/**
 * Created by Victor on 06/09/14.
 */
public class EffectPopulator {

    private ModifierStringConverter modConv;
    private ModifierHelper modifierHelper;

    public EffectPopulator(Context context) {
        this.modConv = new ModifierStringConverter(context);
        this.modifierHelper = new ModifierHelper(context);
    }

    public void populate(EffectSource source, ViewGroup group){
        populateTextView(group, R.id.name, source.getName());

        TextView[] modViews = new TextView[6];
        modViews[0] = findView(group, R.id.mod1);
        modViews[1] = findView(group, R.id.mod2);
        modViews[2] = findView(group, R.id.mod3);
        modViews[3] = findView(group, R.id.mod4);
        modViews[4] = findView(group, R.id.mod5);
        modViews[5] = findView(group, R.id.mod6);

        //populate mods
        List<Modifier> modifiers = source.getEffect() != null ? source.getEffect().getModifiers() : new ArrayList<Modifier>();
        for (int i = 0; i < modifiers.size() && i < modViews.length; i++) {
            TextView modView = modViews[i];
            Modifier mod = modifiers.get(i);
            modView.setText(modConv.getShortString(mod));
            if (mod.isBonus()) {
                modView.setTextColor(modifierHelper.green);
            } else {
                modView.setTextColor(modifierHelper.red);
            }
        }

        //clear empty views
        for (int i = modifiers.size(); i < modViews.length; i++) {
            modViews[i].setText("");
        }

        //show necessary rows, hide excess ones
        View row2 = group.findViewById(R.id.row2);
        row2.setVisibility(modifiers.size() <= 3 ? View.GONE : View.VISIBLE);
        View row1 = group.findViewById(R.id.row1);
        row1.setVisibility(modifiers.isEmpty() ? View.GONE : View.VISIBLE);
    }

}
