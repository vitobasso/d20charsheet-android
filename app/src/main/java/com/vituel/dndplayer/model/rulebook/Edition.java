package com.vituel.dndplayer.model.rulebook;

import com.vituel.dndplayer.model.AbstractEntity;

/**
 * Created by Victor on 12/04/2015.
 */
public class Edition extends AbstractEntity {

    private RuleSystem system;
    private boolean isCore;

    public RuleSystem getSystem() {
        return system;
    }

    public void setSystem(RuleSystem system) {
        this.system = system;
    }

    public boolean isCore() {
        return isCore;
    }

    public void setCore(boolean isCore) {
        this.isCore = isCore;
    }
}
