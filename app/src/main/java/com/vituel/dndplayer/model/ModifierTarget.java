package com.vituel.dndplayer.model;

import static com.vituel.dndplayer.model.Condition.Predicate;
import static com.vituel.dndplayer.model.Condition.Predicate.*;

/**
 * Created by Victor on 26/02/14.
 */
public enum ModifierTarget {

    //hit points
    HP,

    //attributes
    STR, DEX, CON, INT, WIS, CHA,

    //defense
    AC,
    SAVES, //all 3 saves
    FORT, REFL, WILL,

    //attack
    HIT, DAMAGE, DAMAGE_MULT, CRIT_RANGE, CRIT_MULT,

    //skills
    SKILL,

    //other
    SIZE,
    SPEED, SPEED_MULT,
    INIT, //initiative
    CONCEAL, //concealment
    DR, DR_TYPE, //damage reduction
    MR, //magic resistance
    IMMUNE, //immunity to poison, sleep, fire, etc (the object comes as "condition")
    MAX_DEX; //dex bonus limit

    public Predicate getDefaultConditionPredicate() {
        switch (this) {
            case AC:
            case SAVES:
            case FORT:
            case REFL:
            case WILL:
            case IMMUNE:
            case MR:
            case DR:
            case CONCEAL:
                return AGAINST;
            case SKILL:
                return RELATED_TO;
            case HIT:
            case DAMAGE:
            case DAMAGE_MULT:
            case CRIT_MULT:
            case CRIT_RANGE:
                return USING;
            default:
                return WHEN;
        }
    }

    /**
     * http://dnd.steinhour.net/General_DnD/3E_Charts_and_Tables/3E_Magic_Item_Notes.html
     * http://www.d20srd.org/srd/theBasics.htm#modifierTypes
     * http://www.enworld.org/forum/showthread.php?89830-Complete-List-of-Bonus-Types
     */
    public ModifierType[] getAllowedModifiers() {
        switch (this) {
            case STR:
            case DEX:
            case CON:
            case INT:
            case WIS:
            case CHA:
                return new ModifierType[]{
                        ModifierType.ALCHEMICAL, ModifierType.ENHANCEMENT, ModifierType.RACIAL, ModifierType.MORALE, ModifierType.INHERENT
                };
            case AC:
                return new ModifierType[]{
                        ModifierType.ALCHEMICAL, ModifierType.ENHANCEMENT, ModifierType.SIZE, ModifierType.ARMOR, ModifierType.SHIELD, ModifierType.NATURAL_ARMOR,
                        ModifierType.DEFLECTION, ModifierType.DODGE, ModifierType.MORALE, ModifierType.INSIGHT, ModifierType.LUCK, ModifierType.PROFANE, ModifierType.SACRED
                };
            case SAVES:
            case FORT:
            case REFL:
            case WILL:
                return new ModifierType[]{
                        ModifierType.ALCHEMICAL, ModifierType.RACIAL, ModifierType.MORALE, ModifierType.COMPETENCE, ModifierType.RESISTANCE, ModifierType.INSIGHT, ModifierType.LUCK, ModifierType.PROFANE, ModifierType.SACRED
                };
            case HIT:
                return new ModifierType[]{
                        ModifierType.ALCHEMICAL, ModifierType.ENHANCEMENT, ModifierType.SIZE, ModifierType.RACIAL, ModifierType.MORALE, ModifierType.COMPETENCE, ModifierType.CIRCUMSTANCE,
                        ModifierType.INSIGHT, ModifierType.LUCK
                };
            case DAMAGE:
                return new ModifierType[]{
                        ModifierType.ALCHEMICAL, ModifierType.ENHANCEMENT, ModifierType.MORALE, ModifierType.LUCK
                };
            case SKILL:
                return new ModifierType[]{
                        ModifierType.ALCHEMICAL, ModifierType.ENHANCEMENT, ModifierType.SIZE, ModifierType.RACIAL, ModifierType.COMPETENCE, ModifierType.CIRCUMSTANCE,
                        ModifierType.INSIGHT, ModifierType.LUCK, ModifierType.PROFANE, ModifierType.SACRED
                };
            case SPEED:
                return new ModifierType[]{
                        ModifierType.ALCHEMICAL, ModifierType.ENHANCEMENT
                };
            default:
                return new ModifierType[]{
                        ModifierType.ALCHEMICAL, ModifierType.CIRCUMSTANCE, ModifierType.COMPETENCE, ModifierType.INHERENT, ModifierType.INSIGHT, ModifierType.LUCK, ModifierType.RACIAL, ModifierType.PROFANE, ModifierType.SACRED
                };
        }
    }

}
