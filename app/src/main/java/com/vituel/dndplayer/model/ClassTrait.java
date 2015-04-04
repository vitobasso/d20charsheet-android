package com.vituel.dndplayer.model;

/**
 * Created by Victor on 05/04/14.
 */
public class ClassTrait {

    private String name;
    private Effect effect;
    private int level;
    private String overridenTraitName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getOverridenTraitName() {
        return overridenTraitName;
    }

    public void setOverridenTraitName(String overridenTraitName) {
        this.overridenTraitName = overridenTraitName;
    }
}
