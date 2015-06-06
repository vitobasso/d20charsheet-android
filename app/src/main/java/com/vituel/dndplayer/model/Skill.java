package com.vituel.dndplayer.model;

import com.vituel.dndplayer.model.effect.ModifierSource;
import com.vituel.dndplayer.model.rulebook.Rule;

/**
 * Created by Victor on 25/02/14.
 */
public class Skill extends Rule {

    private ModifierSource keyAbility;
    private boolean armorPenaltyApplies;

    public Skill(){

    }

    public Skill(String name) {
        this.name = name;
    }

    public ModifierSource getKeyAbility() {
        return keyAbility;
    }

    public void setKeyAbility(ModifierSource keyAbility) {
        this.keyAbility = keyAbility;
    }

    public boolean isArmorPenaltyApplies() {
        return armorPenaltyApplies;
    }

    public void setArmorPenaltyApplies(boolean armorPenaltyApplies) {
        this.armorPenaltyApplies = armorPenaltyApplies;
    }

}
