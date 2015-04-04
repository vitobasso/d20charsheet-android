package com.vituel.dndplayer.model;

import java.util.ArrayList;
import java.util.List;

import static com.vituel.dndplayer.model.ModifierTarget.CRIT_MULT;
import static com.vituel.dndplayer.model.ModifierTarget.CRIT_RANGE;
import static com.vituel.dndplayer.model.ModifierTarget.DAMAGE;

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

    public WeaponProperties(DiceRoll damage, Critical critical) {
        this();
        this.setDamage(damage);
        this.setCritical(critical);
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
