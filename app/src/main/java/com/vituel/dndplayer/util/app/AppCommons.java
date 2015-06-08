package com.vituel.dndplayer.util.app;

import android.content.Context;

import com.vituel.dndplayer.model.DiceRoll;
import com.vituel.dndplayer.model.TempEffect;
import com.vituel.dndplayer.model.character.CharBase;
import com.vituel.dndplayer.model.effect.Condition;
import com.vituel.dndplayer.model.effect.Modifier;
import com.vituel.dndplayer.model.effect.ModifierTarget;

import java.util.Collection;

import static com.vituel.dndplayer.model.effect.ModifierTarget.DEX;
import static com.vituel.dndplayer.model.effect.ModifierTarget.FORT;
import static com.vituel.dndplayer.model.effect.ModifierTarget.MAX_DEX;
import static com.vituel.dndplayer.model.effect.ModifierTarget.REFL;
import static com.vituel.dndplayer.model.effect.ModifierTarget.SAVES;
import static com.vituel.dndplayer.model.effect.ModifierTarget.WILL;

/**
 * Created by Victor on 24/04/14.
 */
public class AppCommons {

    private Context ctx;
    public final int black, green, red;

    public AppCommons(Context ctx) {
        this.ctx = ctx;
        this.black = ctx.getResources().getColor(android.R.color.black);
        this.green = ctx.getResources().getColor(android.R.color.holo_green_light);
        this.red = ctx.getResources().getColor(android.R.color.holo_red_light);
    }

    public static boolean modifierApplies(Modifier modifier, ModifierTarget target, String variation,
                                          Collection<Condition> activeConditions) {

        ModifierTarget modTarget = modifier.getTarget();
        String modVariation = modifier.getVariation();
        Condition modCondition = modifier.getCondition();
        DiceRoll amount = modifier.getAmount();

        boolean satisfiesCondition = modCondition == null || activeConditions.contains(modCondition);
        boolean nonZero = !amount.isFixed() || amount.toInt() != 0;
        boolean targetMatches = modTarget == target;
        boolean variationMatches = variation == null || (modVariation != null && modVariation.equalsIgnoreCase(variation));
        boolean maxDexApplies = target == DEX && modTarget == MAX_DEX;
        boolean aggregateTarget = (target == FORT || target == REFL || target == WILL) && modTarget == SAVES;

        return ((targetMatches && variationMatches) || maxDexApplies || aggregateTarget) && satisfiesCondition && nonZero;
    }

    public static String modifierString(Modifier modifier, String source) {

        ModifierTarget modTarget = modifier.getTarget();
        DiceRoll amount = modifier.getAmount();

        String prefix = "";
        if (!"Base".equals(source)) {
            if (modTarget == MAX_DEX) {
                prefix = "<";
            } else if (amount.isPositive()) {
                prefix = "+";
            }
        }

        return prefix + amount.toString();
    }

    public int getValueColor(CharBase base, ModifierTarget target, String variation) {

        //default color
        int color = black;

        //find color
        Collection<TempEffect> temps = base.getActiveTempEffects();
        externalLoop:
        for (TempEffect temp : temps) {
            for (Modifier mod : temp.getEffect().getModifiers()) {
                if (AppCommons.modifierApplies(mod, target, variation, base.getActiveConditions())) {
                    color = green;
                    if (!mod.isBonus()) {
                        color = red;
                        break externalLoop;
                    }
                }
            }
        }

        return color;
    }

}
