package com.vituel.dndplayer.model;

/**
 * Created by Victor on 30/03/14.
 */
public enum Size {

    FINE, DIMINUTIVE, TINY, SMALL, MEDIUM, LARGE, HUGE, GARGANTUAN, COLOSSAL;

    public int getIndex() {
        return ordinal() - 4;
    }

    public static Size fromIndex(int i) {
        return values()[i + 4];
    }

    public int getCombatModifier() {
        switch (this) {
            case FINE:
                return 8;
            case DIMINUTIVE:
                return 4;
            case TINY:
                return 2;
            case SMALL:
                return 1;
            case MEDIUM:
                return 0;
            case LARGE:
                return -1;
            case HUGE:
                return -2;
            case GARGANTUAN:
                return -4;
            case COLOSSAL:
                return -8;
            default:
                throw new AssertionError();
        }
    }

    public int getStrCheckModifier(){
        return getIndex() * 4;
    }

}
