package com.vituel.dndplayer.activity.summary;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.model.AbstractEffect;
import com.vituel.dndplayer.model.Character;
import com.vituel.dndplayer.model.DiceRoll;
import com.vituel.dndplayer.model.Modifier;
import com.vituel.dndplayer.model.ModifierTarget;
import com.vituel.dndplayer.model.ModifierType;
import com.vituel.dndplayer.util.AppCommons;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.vituel.dndplayer.util.ActivityUtil.findView;
import static com.vituel.dndplayer.util.ActivityUtil.inflate;

/**
 * Created by Victor on 26/04/14.
 */
public class BreakdownDialogInflater {

    Activity activity;
    com.vituel.dndplayer.model.Character character;
    public final int black, green, red;

    private ModifierTarget target;
    private String variation;

    public BreakdownDialogInflater(Activity activity, Character character, ModifierTarget target, String variation) {
        this.activity = activity;
        this.character = character;
        this.target = target;
        this.variation = variation;

        AppCommons commons = new AppCommons(activity);
        this.black = commons.black;
        this.green = commons.green;
        this.red = commons.red;
    }

    public int appendRows(ViewGroup parentView, Collection<? extends AbstractEffect> effects, String source) {
        int count = 0;
        for (AbstractEffect effect : effects) {
            if (effect != null) {
                count += appendRows(parentView, effect, source);
            }
        }
        return count;
    }

    public int appendRows(ViewGroup parentView, Collection<? extends AbstractEffect> effects, boolean isColored) {
        int count = 0;
        for (AbstractEffect effect : effects) {
            if (effect != null) {
                count += appendRows(parentView, effect, isColored);
            }
        }
        return count;
    }

    public int appendRows(ViewGroup parentView, AbstractEffect effect, String source) {
        int count = 0;
        if (effect != null) {
            for (Modifier modifier : effect.getModifiers()) {
                if (modifierApplies(modifier)) {
                    count++;
                    appendRow(parentView, modifier.getAmount(), source, modifier.getType(), black);
                }
            }
        }
        return count;
    }

    public int appendRows(ViewGroup parentView, AbstractEffect effect, boolean isColored) {
        int count = 0;
        if (effect != null) {
            for (Modifier modifier : effect.getModifiers()) {
                if (modifierApplies(modifier)) {
                    count++;
                    int color = isColored ? modifier.isBonus() ? green : red : black;
                    appendRow(parentView, modifier.getAmount(), effect.getName(), modifier.getType(), color);
                }
            }
        }
        return count;
    }

    public int appendRows(ViewGroup parentView, Map<Modifier, String> modifiers) {
        int count = 0;
        if (modifiers != null) {
            for (Modifier modifier : modifiers.keySet()) {
                if (modifierApplies(modifier)) {
                    count++;
                    appendRow(parentView, modifier.getAmount(), modifiers.get(modifier), black);
                }
            }
        }
        return count;
    }

    public int appendRows(ViewGroup parentView, List<Modifier> modifiers, String source) {
        int count = 0;
        if (modifiers != null) {
            for (Modifier modifier : modifiers) {
                if (modifierApplies(modifier)) {
                    count++;
                    appendRow(parentView, modifier.getAmount(), source, modifier.getType(), black);
                }
            }
        }
        return count;
    }

    public boolean modifierApplies(Modifier modifier) {
        return AppCommons.modifierApplies(modifier, target, variation, character.getBase().getActiveConditions());
    }

    public ViewGroup  appendRow(ViewGroup parentView, DiceRoll value, String source, int colorRes) {
        return appendRow(parentView, value.toString(), source, colorRes);
    }

    public ViewGroup  appendRow(ViewGroup parentView, DiceRoll value, String source, ModifierType type, int color) {
        if (type != null) {
            source = String.format("%s (%s)", source, type.toString());
        }
        return appendRow(parentView, value.toString(), source, color);
    }

    public ViewGroup appendRow(ViewGroup parentView, int value, String source, int color) {
        return appendRow(parentView, "" + value, source, color);
    }

    public ViewGroup appendRow(ViewGroup parentView, String value, String source, int color) {
        ViewGroup row = inflate(activity, parentView, R.layout.summary_main_breakdown_row);
        TextView valueView = findView(row, R.id.value);
        valueView.setText(value);
        TextView sourceView = findView(row, R.id.source);
        sourceView.setText(source);

        valueView.setTextColor(color);
        sourceView.setTextColor(color);

        return row;
    }

}
