package com.vituel.dndplayer.model.rulebook;

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

}
