package com.vitobasso.d20charsheet.util.business;

import android.content.Context;

import com.vitobasso.d20charsheet.model.DiceRoll;
import com.vitobasso.d20charsheet.model.TempEffect;
import com.vitobasso.d20charsheet.model.character.CharBase;
import com.vitobasso.d20charsheet.model.effect.Condition;
import com.vitobasso.d20charsheet.model.effect.Modifier;
import com.vitobasso.d20charsheet.model.effect.ModifierTarget;

import java.util.Collection;

import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.DEX;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.FORT;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.MAX_DEX;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.REFL;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.SAVES;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.SPEED;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.WILL;

/**
 * Created by Victor on 24/04/14.
 */
public class ModifierHelper {

    private Context context;
    public final int black, green, red;
    private DistanceHelper distanceHelper;

    public ModifierHelper(Context context) {
        this.context = context;
        this.black = getColor(android.R.color.black);
        this.green = getColor(android.R.color.holo_green_light);
        this.red = getColor(android.R.color.holo_red_light);
        this.distanceHelper = new DistanceHelper(context);
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

    public String getAsString(Modifier modifier, String source) {
        ModifierTarget modTarget = modifier.getTarget();
        DiceRoll amount = modifier.getAmount();
        String prefix = getPrefix(source, modTarget, amount);
        return prefix + getAmountAsString(modTarget, amount);
    }

    private static String getPrefix(String source, ModifierTarget modTarget, DiceRoll amount) {
        String prefix = "";
        if (!"Base".equals(source)) {
            if (modTarget == MAX_DEX) {
                prefix = "≤";
            } else if (amount.isPositive()) {
                prefix = "+";
            }
        }
        return prefix;
    }

    private String getAmountAsString(ModifierTarget modTarget, DiceRoll amount) {
        if (modTarget == SPEED) {
            int fixedAmount = amount.toInt();
            return distanceHelper.getSpeedAsString(fixedAmount);
        } else {
            return amount.toString();
        }
    }

    public int getColor(ModifierTarget target, String variation, CharBase base) {

        //default color
        int color = black;

        //find color
        Collection<TempEffect> temps = base.getActiveTempEffects();
        externalLoop:
        for (TempEffect temp : temps) {
            for (Modifier mod : temp.getEffect().getModifiers()) {
                if (ModifierHelper.modifierApplies(mod, target, variation, base.getActiveConditions())) {
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

    private int getColor(int id) {
        return context.getResources().getColor(id);
    }

}
