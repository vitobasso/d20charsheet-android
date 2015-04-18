package com.vituel.dndplayer.model.character;

import java.io.Serializable;

/**
 * Created by Victor on 26/02/14.
 */
public class DamageTaken implements Serializable {

    //on HP
    private int lethal;
    private int nonlethal;
    private int tempHp;

    //on abilities and level
    private int strength;
    private int dexterity;
    private int constitution;
    private int intelligence;
    private int wisdom;
    private int charisma;
    private int level;

    public int getLethal() {
        return lethal;
    }

    public int getNonlethal() {
        return nonlethal;
    }

    public int getTempHp() {
        return tempHp;
    }

    public int getStrength() {
        return strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getConstitution() {
        return constitution;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getWisdom() {
        return wisdom;
    }

    public int getCharisma() {
        return charisma;
    }

    public int getLevel() {
        return level;
    }

    public void setLethal(int lethal) {
        this.lethal = lethal;
    }

    public void setNonlethal(int nonlethal) {
        this.nonlethal = nonlethal;
    }

    public void setTempHp(int tempHp) {
        this.tempHp = tempHp;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public void setConstitution(int constitution) {
        this.constitution = constitution;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public void setWisdom(int wisdom) {
        this.wisdom = wisdom;
    }

    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
