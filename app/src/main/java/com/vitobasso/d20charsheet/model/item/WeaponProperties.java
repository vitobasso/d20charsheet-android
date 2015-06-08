package com.vitobasso.d20charsheet.model.item;

import com.vitobasso.d20charsheet.model.AbstractEntity;
import com.vitobasso.d20charsheet.model.Critical;
import com.vitobasso.d20charsheet.model.DiceRoll;
import com.vitobasso.d20charsheet.model.effect.Modifier;

import java.util.ArrayList;
import java.util.List;

import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.CRIT_MULT;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.CRIT_RANGE;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.DAMAGE;

/**
 * Created by Victor on 01/05/14.
 */
public class WeaponProperties extends AbstractEntity {

    private DiceRoll damage;
    private Critical critical;

    public WeaponProperties() {
        super();
        this.setDamage(new DiceRoll());
        this.setCritical(new Critical(1, 2));
    }

    public DiceRoll getDamage() {
        return damage;
    }

    public Critical getCritical() {
        return critical;
    }

    public void setDamage(DiceRoll damage) {
        this.damage = damage;
    }

    public void setCritical(Critical critical) {
        this.critical = critical;
    }

    public List<Modifier> getAsModifiers() {
        Modifier dmg = new Modifier(DAMAGE, getDamage());
        Modifier critMult = new Modifier(CRIT_MULT, getCritical().getMultiplier());
        Modifier critRange = new Modifier(CRIT_RANGE, getCritical().getRange());

        List<Modifier> modifiers = new ArrayList<>();
        modifiers.add(dmg);
        modifiers.add(critRange);
        modifiers.add(critMult);
        return modifiers;
    }

}
