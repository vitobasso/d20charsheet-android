package com.vituel.dndplayer.model;

/**
 * Created by Victor on 25/02/14.
 */
public class Trait extends AbstractEffect {

    public enum Type {
        FEAT, RACIAL, CLASS, CAMPAIGN
    }

    private Type traitType;

    public Trait() {
        super(AbstractEffect.Type.TRAIT);
    }

    public Type getTraitType() {
        return traitType;
    }

    public void setTraitType(Type traitType) {
        this.traitType = traitType;
    }


}
