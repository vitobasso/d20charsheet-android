package com.vituel.dndplayer.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor on 20/03/14.
 */
public abstract class AbstractEffect extends AbstractEntity implements Comparable<AbstractEffect> {

    public enum Type {

        CLASS, //class traits
        RACE, //race traits
        TRAIT, //feats and special traits
        EQUIP_MAGIC, //equiped items magic bonuses
        //equiped armor (ac, max dex, arcane failure)
        //equiped shield (ac, damage)
        TEMPORARY; //spell effect,
        //combat modifiers (prone, flanking, etc)
        //usable item effect
        //activation effect from equiped item, trait, class or race

    }


    private Type type;
    private List<Modifier> modifiers = new ArrayList<>();

    protected AbstractEffect(Type type) {
        if (type == null) {
            throw new InvalidParameterException();
        }
        this.setType(type);
    }

    protected AbstractEffect(Type type, String name) {
        this(type);
        this.name = name;
    }

    public void addModifier(Modifier modifier){
        getModifiers().add(modifier);
    }

    public Type getType() {
        return type;
    }

    public List<Modifier> getModifiers() {
        return modifiers;
    }


    public void setType(Type type) {
        this.type = type;
    }

    public void setModifiers(List<Modifier> modifiers) {
        this.modifiers = modifiers;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object another) {
        if (another instanceof AbstractEffect) {
            AbstractEffect anotherEffect = ((AbstractEffect) another);
            return ((Object) this).getClass().equals(another.getClass())
                    && getType() == anotherEffect.getType()
                    && name.equals(anotherEffect.name);
        }
        return false;
    }

    @Override
    public int compareTo(AbstractEffect another) {
        int typeCompare = getType().compareTo(another.getType());
        if (typeCompare != 0) {
            return typeCompare;
        } else {
            return name.compareTo(another.name);
        }
    }
}
