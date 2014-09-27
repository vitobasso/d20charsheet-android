package com.vituel.dndplayer.model;

/**
 * Created by Victor on 05/04/14.
 */
public class ClassTrait extends Trait {

    private int level;
    private String overridenTraitName;

    public ClassTrait() {
        setTraitType(Type.CLASS);
    }

    public ClassTrait(Trait trait) {
        this();

        //trait fields
        this.setTraitType(trait.getTraitType());

        //effect fields
        this.setType(trait.getType());
        this.setModifiers(trait.getModifiers());

        //entity fields
        this.id = trait.getId();
        this.name = trait.getName();
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
