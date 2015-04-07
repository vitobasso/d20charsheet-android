package com.vituel.dndplayer.model;

/**
* Created by Victor on 06/04/2015.
*/
public enum Multiplier {

    HALF, ONE, ONE_AND_HALF, DOUBLE;

    public String toLabel() {
        switch (this) {
            case HALF: return "1/2×";
            case ONE: return "";
            case ONE_AND_HALF: return "1.5×";
            case DOUBLE: return "2×";
            default: return null;
        }
    }

    public int multiply(int value) {
        return (int) (toValue() * value);
    }

    private float toValue() {
        switch (this) {
            case HALF: return .5f;
            case ONE: return 1;
            case ONE_AND_HALF: return 1.5f;
            case DOUBLE: return 2;
            default: return Float.NaN;
        }
    }

}
