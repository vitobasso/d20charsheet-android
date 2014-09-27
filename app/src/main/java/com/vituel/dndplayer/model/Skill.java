package com.vituel.dndplayer.model;

/**
 * Created by Victor on 25/02/14.
 */
public class Skill extends AbstractEntity {

    private ModifierTarget keyAbility;
    private boolean armorPenaltyApplies;

    public Skill(){

    }

    public Skill(String name) {
        this.name = name;
    }

    public ModifierTarget getKeyAbility() {
        return keyAbility;
    }

    public void setKeyAbility(ModifierTarget keyAbility) {
        this.keyAbility = keyAbility;
    }

    public boolean isArmorPenaltyApplies() {
        return armorPenaltyApplies;
    }

    public void setArmorPenaltyApplies(boolean armorPenaltyApplies) {
        this.armorPenaltyApplies = armorPenaltyApplies;
    }

}
