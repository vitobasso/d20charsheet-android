package com.vitobasso.d20charsheet.activity.summary;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.model.character.CharSummary;
import com.vitobasso.d20charsheet.model.effect.EffectSource;
import com.vitobasso.d20charsheet.model.effect.Modifier;
import com.vitobasso.d20charsheet.model.effect.ModifierTarget;
import com.vitobasso.d20charsheet.model.effect.ModifierType;
import com.vitobasso.d20charsheet.util.business.ModifierHelper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.vitobasso.d20charsheet.util.app.ActivityUtil.inflate;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.populateTextView;

/**
 * Created by Victor on 26/04/14.
 */
public class BreakdownDialogInflater {

    private Activity activity;
    private CharSummary charSummary;
    private ModifierHelper modifierHelper;
    public final int black, green, red;

    private ModifierTarget target;
    private String variation;

    public BreakdownDialogInflater(Activity activity, CharSummary charSummary, ModifierTarget target, String variation) {
        this.activity = activity;
        this.charSummary = charSummary;
        this.target = target;
        this.variation = variation;

        this.modifierHelper = new ModifierHelper(activity);
        this.black = modifierHelper.black;
        this.green = modifierHelper.green;
        this.red = modifierHelper.red;
    }

    public int appendRows(ViewGroup parentView, Collection<? extends EffectSource> sources, String sourceName) {
        int count = 0;
        for (EffectSource source : sources) {
            if (source != null) {
                count += appendRows(parentView, source, sourceName);
            }
        }
        return count;
    }

    public int appendRows(ViewGroup parentView, Collection<? extends EffectSource> effects) {
        return appendRows(parentView, effects, false);
    }

    public int appendRowsColored(ViewGroup parentView, Collection<? extends EffectSource> effects) {
        return appendRows(parentView, effects, true);
    }

    private int appendRows(ViewGroup parentView, Collection<? extends EffectSource> effects, boolean isColored) {
        int count = 0;
        for (EffectSource source : effects) {
            if (source != null) {
                count += appendRows(parentView, source, isColored);
            }
        }
        return count;
    }

    private int appendRows(ViewGroup parentView, EffectSource source, String sourceName) {
        int count = 0;
        if (source != null) {
            for (Modifier modifier : source.getEffect().getModifiers()) {
                if (modifierApplies(modifier)) {
                    count++;
                    appendRow(parentView, modifier, sourceName, modifier.getType(), black);
                }
            }
        }
        return count;
    }

    public int appendRows(ViewGroup parentView, EffectSource source) {
        return appendRows(parentView, source, false);
    }

    private int appendRows(ViewGroup parentView, EffectSource source, boolean isColored) {
        int count = 0;
        if (source != null && source.getEffect() != null) {
            for (Modifier modifier : source.getEffect().getModifiers()) {
                if (modifierApplies(modifier)) {
                    count++;
                    int color = isColored ? modifier.isBonus() ? green : red : black;
                    appendRow(parentView, modifier, source.getName(), modifier.getType(), color);
                }
            }
        }
        return count;
    }

    public int appendRows(ViewGroup parentView, Map<Modifier, String> modifierSources) {
        int count = 0;
        if (modifierSources != null) {
            for (Modifier modifier : modifierSources.keySet()) {
                if (modifierApplies(modifier)) {
                    count++;
                    appendRow(parentView, modifier, modifierSources.get(modifier), black);
                }
            }
        }
        return count;
    }

    public int appendRows(ViewGroup parentView, List<Modifier> modifiers, String sourceName) {
        int count = 0;
        if (modifiers != null) {
            for (Modifier modifier : modifiers) {
                if (modifierApplies(modifier)) {
                    count++;
                    appendRow(parentView, modifier, sourceName, modifier.getType(), black);
                }
            }
        }
        return count;
    }

    private ViewGroup appendRow(ViewGroup parentView, Modifier modifier, String sourceName, ModifierType type, int color) {
        if (type != null) {
            sourceName = String.format("%s (%s)", sourceName, type.toString());
        }
        String value = modifierHelper.getAsString(modifier, sourceName);
        return appendRow(parentView, value, sourceName, color);
    }

    private ViewGroup appendRow(ViewGroup parentView, Modifier modifier, String sourceName, int colorRes) {
        String value = modifierHelper.getAsString(modifier, sourceName);
        return appendRow(parentView, value, sourceName, colorRes);
    }

    public ViewGroup appendRow(ViewGroup parentView, int value, String sourceName, int color) {
        return appendRow(parentView, "" + value, sourceName, color);
    }

    private ViewGroup appendRow(ViewGroup parentView, String value, String sourceName, int color) {
        ViewGroup row = inflate(activity, parentView, R.layout.summary_breakdown_row);

        TextView valueView = populateTextView(row, R.id.value, value);
        TextView sourceView = populateTextView(row, R.id.source, sourceName);

        valueView.setTextColor(color);
        sourceView.setTextColor(color);

        return row;
    }

    private boolean modifierApplies(Modifier modifier) {
        return ModifierHelper.modifierApplies(modifier, target, variation, charSummary.getBase().getActiveConditions());
    }

}
