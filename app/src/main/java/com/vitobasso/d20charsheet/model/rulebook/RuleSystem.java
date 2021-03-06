package com.vitobasso.d20charsheet.model.rulebook;

import java.security.InvalidParameterException;

/**
 * Created by Victor on 12/04/2015.
 */
public enum RuleSystem {

    DND_3_0, DND_3_5;

    public static RuleSystem fromString(String str) {
        switch (str) {
            case "DnD 3.5":
                return DND_3_5;
            case "DnD 3.0":
                return DND_3_0;
            default:
                throw new InvalidParameterException("Invalid RuleSystem: " + str);
        }
    }

    public String toLabel() {
        switch (this) {
            case DND_3_5:
                return "3.5";
            case DND_3_0:
                return "3.0";
            default:
                throw new IllegalStateException("Invalid RuleSystem: " + this);
        }
    }
}
