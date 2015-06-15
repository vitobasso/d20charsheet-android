package com.vitobasso.d20charsheet.model.character;

import com.vitobasso.d20charsheet.model.AbstractEntity;
import com.vitobasso.d20charsheet.model.CharEntity;
import com.vitobasso.d20charsheet.model.effect.Modifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor on 19/03/14.
 */
public class AttackRound extends AbstractEntity implements CharEntity {

    private List<Attack> attacks = new ArrayList<>();

    /**
     * Called by reflection on AbstractEditActivity
     */
    public AttackRound() {
    }

    public AttackRound(String name) {
        this.name = name;
    }

    public void applyModifier(Modifier modifier) {
        for (Attack attack : getAttacks()) {
            attack.applyModifier(modifier);
        }
    }

    public List<Attack> getAttacks() {
        return attacks;
    }

    public void setAttacks(List<Attack> attacks) {
        this.attacks = attacks;
    }

    public void addAttack(Attack attack) {
        this.getAttacks().add(attack);
    }

    public List<Modifier> getBaseModifiers() {
        List<Modifier> modifiers = new ArrayList<>();
        for (Attack attack : getAttacks()) {
            modifiers.addAll(attack.getWeapon().getAsModifiers());
        }
        return modifiers;
    }

}
