package com.vitobasso.d20charsheet.model.effect;

import android.content.Context;

import com.vitobasso.d20charsheet.model.DiceRoll;
import com.vitobasso.d20charsheet.util.LangUtil;
import com.vitobasso.d20charsheet.util.i18n.EnumI18n;

/**
 * Modifier based on an ability.
 * E.g.:
 * DEX to HIT
 * 1.5xSTR to DAMAGE
 * WIS to AC
 *
 * Unusual bonus refference:
 * http://www.giantitp.com/forums/showthread.php?125732-3-x-X-stat-to-Y-bonus
 *
 * Created by Victor on 05/04/2015.
 */
public class AbilityModifier extends Modifier {

    private ModifierSource ability;
    private Multiplier multiplier;

    public AbilityModifier() {
    }

    public AbilityModifier(ModifierSource ability, ModifierTarget target) {
        this.ability = ability;
        this.target = target;
        this.multiplier = Multiplier.ONE;
    }

    public AbilityModifier(ModifierSource ability, Multiplier multiplier, ModifierTarget target) {
        this.ability = ability;
        this.multiplier = multiplier;
        this.target = target;
    }

    public AbilityModifier(ModifierSource ability, ModifierTarget target, String variation) {
        this(ability, target);
        this.variation = variation;
    }

    public String getTargetAsString() {
        return variation == null ? target.toString() : variation;
    }

    public String getSourceAsString() {
        return multiplier.toLabel() + ability;
    }


    public String getTargetLabel(Context ctx) {
        EnumI18n i18n = new EnumI18n(ctx);
        if (variation == null || "".equals(variation)) {
            return i18n.get(target).toString();
        } else {
            return variation;
        }
    }

    public String getSourceLabel(Context ctx) {
        EnumI18n i18n = new EnumI18n(ctx);
        return multiplier.toLabel() + i18n.get(ability);
    }

    @Override
    public DiceRoll getAmount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAmount(DiceRoll amount) {
        throw new UnsupportedOperationException();
    }

    public ModifierSource getAbility() {
        return ability;
    }

    public void setAbility(ModifierSource ability) {
        this.ability = ability;
    }

    public Multiplier getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(Multiplier multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public int hashCode() {
        return LangUtil.hash(target, variation, ability);
    }

    @Override
    public String toString() {
        return getTargetAsString() + " " + getSourceAsString();
    }
}
