package com.vituel.dndplayer.model.effect;

/**
 * Created by Victor on 21/03/14.
 */
public enum ModifierType {

    ENHANCEMENT,
    MORALE,
    COMPETENCE,
    CIRCUMSTANCE,
    ARMOR,
    SHIELD,
    NATURAL_ARMOR,
    DEFLECTION,
    DODGE,
    SIZE,
    RACIAL,
    LUCK,
    ALCHEMICAL,
    INSIGHT,
    RESISTANCE,
    INHERENT,
    PROFANE,
    SACRED;
    //EPIC,
    //DIVINE,
    //PERFECTION,

    public boolean stacks() {
        switch (this) {
            case CIRCUMSTANCE:
            case DODGE:
                return true;
            default:
                return false;
        }
    }

}
