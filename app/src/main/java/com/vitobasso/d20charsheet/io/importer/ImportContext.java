package com.vitobasso.d20charsheet.io.importer;

import com.vitobasso.d20charsheet.model.effect.Condition;

import java.util.Map;

/**
* Created by Victor on 15/06/2015.
*/
public class ImportContext {

    private Map<Condition, Condition> cachedConditions;
    private Map<String, String> skillNameMap;

    public Map<Condition, Condition> getCachedConditions() {
        return cachedConditions;
    }

    public void setCachedConditions(Map<Condition, Condition> cachedConditions) {
        this.cachedConditions = cachedConditions;
    }

    public Map<String, String> getSkillNameMap() {
        return skillNameMap;
    }

    public void setSkillNameMap(Map<String, String> skillNameMap) {
        this.skillNameMap = skillNameMap;
    }
}
