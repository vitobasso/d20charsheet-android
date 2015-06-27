package com.vitobasso.d20charsheet.model.character;

import com.vitobasso.d20charsheet.model.Size;
import com.vitobasso.d20charsheet.model.effect.Condition;
import com.vitobasso.d20charsheet.model.effect.ModifierSource;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Values shown in the Summary. Including bonuses from items, traits and spells.
 * <p/>
 * Created by Victor on 25/02/14.
 */
public class CharSummary {

    private CharBase base;
    private int hitPoints;
    private int strength, dexterity, constitution, intelligence, wisdom, charisma;
    private int maxDexMod;
    private int armorClass;
    private int damageReduction;
    private int magicResistance;
    private int concealment;
    private int fortitude, reflex, will;
    private int speed;
    private int initiative;
    private Size size;
    private Map<String, CharSkill> skills; //CharSkill objects store final bonus values (not graduation)
    private List<AttackRound> attacks;
    private Set<Condition> referencedConditions;

    public int getCurrentHitPoints() {
        DamageTaken dmg = getBase().getDamageTaken();
        return getHitPoints() - dmg.getLethal() - dmg.getNonlethal();
    }

    public int getCurrentStrength() {
        return getStrength() - getBase().getDamageTaken().getStrength();
    }

    public int getCurrentDexterity() {
        return getDexterity() - getBase().getDamageTaken().getDexterity();
    }

    public int getCurrentConstitution() {
        return getConstitution() - getBase().getDamageTaken().getConstitution();
    }

    public int getCurrentIntelligence() {
        return getIntelligence() - getBase().getDamageTaken().getIntelligence();
    }

    public int getCurrentWisdom() {
        return getWisdom() - getBase().getDamageTaken().getWisdom();
    }

    public int getCurrentCharisma() {
        return getCharisma() - getBase().getDamageTaken().getCharisma();
    }

    private int getAbilityModifier(int attributeValue) {
        return (int) Math.floor((attributeValue - 10) / 2f);
    }

    public int getAbilityModifier(ModifierSource ability) {
        switch (ability) {
            case STR:
                return getAbilityModifier(getCurrentStrength());
            case DEX:
                int dexMod = getAbilityModifier(getCurrentDexterity());
                return Math.min(dexMod, getMaxDexMod());
            case CON:
                return getAbilityModifier(getCurrentConstitution());
            case INT:
                return getAbilityModifier(getCurrentIntelligence());
            case WIS:
                return getAbilityModifier(getCurrentWisdom());
            case CHA:
                return getAbilityModifier(getCurrentCharisma());
            default:
                throw new InvalidParameterException(ability + " is not an ability");
        }
    }

    public CharBase getBase() {
        return base;
    }

    public Set<Condition> getReferencedConditions() {
        return referencedConditions;
    }

    public void setReferencedConditions(Set<Condition> referencedConditions) {
        this.referencedConditions = referencedConditions;
    }

    public void addReferencedCondition(Condition condition) {
        referencedConditions.add(condition);
    }

    public void setBase(CharBase base) {
        this.base = base;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getConstitution() {
        return constitution;
    }

    public void setConstitution(int constitution) {
        this.constitution = constitution;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getWisdom() {
        return wisdom;
    }

    public void setWisdom(int wisdom) {
        this.wisdom = wisdom;
    }

    public int getCharisma() {
        return charisma;
    }

    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }

    public int getMaxDexMod() {
        return maxDexMod;
    }

    public void setMaxDexMod(int maxDexMod) {
        this.maxDexMod = maxDexMod;
    }

    public int getArmorClass() {
        return armorClass;
    }

    public void setArmorClass(int armorClass) {
        this.armorClass = armorClass;
    }

    public int getDamageReduction() {
        return damageReduction;
    }

    public void setDamageReduction(int damageReduction) {
        this.damageReduction = damageReduction;
    }

    public int getMagicResistance() {
        return magicResistance;
    }

    public void setMagicResistance(int magicResistance) {
        this.magicResistance = magicResistance;
    }

    public int getConcealment() {
        return concealment;
    }

    public void setConcealment(int concealment) {
        this.concealment = concealment;
    }

    public int getFortitude() {
        return fortitude;
    }

    public void setFortitude(int fortitude) {
        this.fortitude = fortitude;
    }

    public int getReflex() {
        return reflex;
    }

    public void setReflex(int reflex) {
        this.reflex = reflex;
    }

    public int getWill() {
        return will;
    }

    public void setWill(int will) {
        this.will = will;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getInitiative() {
        return initiative;
    }

    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Map<String, CharSkill> getSkills() {
        return skills;
    }

    public void setSkills(Map<String, CharSkill> skills) {
        this.skills = skills;
    }

    public List<AttackRound> getAttacks() {
        return attacks;
    }

    public void setAttacks(List<AttackRound> attacks) {
        this.attacks = attacks;
    }

    @Override
    public String toString() {
        return getBase().getName();
    }
}
