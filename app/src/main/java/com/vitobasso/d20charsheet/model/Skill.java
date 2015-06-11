package com.vitobasso.d20charsheet.model;

import com.vitobasso.d20charsheet.model.effect.ModifierSource;
import com.vitobasso.d20charsheet.model.rulebook.Rule;

/**
 * Created by Victor on 25/02/14.
 */
public class Skill extends Rule {

    private ModifierSource keyAbility;
    private boolean armorPenaltyApplies;

    public Skill(){
    }

    //used by jackson
    public Skill(long id) {
        super(id);
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
