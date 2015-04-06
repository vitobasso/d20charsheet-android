package com.vituel.dndplayer.model;

import com.vituel.dndplayer.util.JavaUtil;

/**
 * Modifier based on an ability.
 * E.g.:
 * DEX to HIT
 * 1.5xSTR to DAMAGE
 * WIS to AC
 * <p/>
 * Created by Victor on 05/04/2015.
 */
public class AbilityModifier extends Modifier {

    public static enum Multiplier {
        HALF, ONE, ONE_AND_HALF, DOUBLE
    }

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
        return JavaUtil.hash(target, variation, ability);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(getVariation() == null ? getTarget().toString() : getVariation());
        str.append(" ");
        str.append(getAbility());
        return str.toString();
    }
}
