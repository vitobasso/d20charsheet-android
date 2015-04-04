package com.vituel.dndplayer.model;

import com.vituel.dndplayer.util.AttackUtil;

import java.security.InvalidParameterException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static com.vituel.dndplayer.model.Attack.WeaponReference.MAIN_HAND;

/**
 * Persisted data inherent to the character. Values before calculation of bonus from items, traits and spells.
 * <p/>
 * Created by Victor on 27/02/14.
 */
public class CharBase extends AbstractEntity {

    private int age;
    private double height;
    private double weight;
    private String tendencyMoral;
    private String tendencyLoyality;
    private String divinity;
    private String gender;
    private String player;
    private String dungeonMaster;
    private String campaign;
    private String creationDate;

    private int hitPoints;
    private int strength;
    private int dexterity;
    private int constitution;
    private int intelligence;
    private int wisdom;
    private int charisma;

    private Race race;
    private List<ClassLevel> classLevels = new ArrayList<>();
    private List<Feat> feats = new ArrayList<>();
    private List<CharSkill> skills = new ArrayList<>(); //CharSkill objects store graduation values (not final bonus)

    private CharEquip equipment = new CharEquip();
    private Map<Item, Integer> bag = new TreeMap<>();
    private Map<TempEffect, Boolean> tempEffects = new TreeMap<>();
    private Set<Condition> activeConditions = new HashSet<>();

    private List<AttackRound> attacks = new ArrayList<>();

    private DamageTaken damageTaken = new DamageTaken();

    public int getExperienceLevel() {
        int sum = 0;
        for (ClassLevel classLevel : getClassLevels()) {
            sum += classLevel.getLevel();
        }
        return sum;
    }

    public int getBaseAttack() {
        int sum = 0;
        for (ClassLevel classLevel : getClassLevels()) {
            sum += classLevel.getBaseAttack();
        }
        return sum;
    }

    public WeaponProperties getWeapon(Attack.WeaponReference weaponReference) {
        switch (weaponReference){
            case MAIN_HAND:
                return getWeapon(equipment.getMainHand());
            case OFFHAND:
                return getWeapon(equipment.getOffhand());
            default:
                throw new InvalidParameterException();
        }
    }

    private WeaponProperties getWeapon(EquipSlot slot) {
        Item item = slot.getItem();
        if (item != null && item instanceof WeaponItem) {
            return ((WeaponItem) item).getWeaponProperties();
        } else {
            return AttackUtil.unnarmedStrike();
        }
        //TODO shield attack
    }


    public void createStandardAttacks() {
        //full attack
        String name = getWeapon(MAIN_HAND).getName();
        AttackRound fullAtk = new AttackRound(name);
        int[] bonuses = AttackUtil.fullAttackPenalties(getBaseAttack());
        for (int bonus : bonuses) {
            Attack attack = new Attack(bonus, MAIN_HAND);
            fullAtk.addAttack(attack);
        }

        getAttacks().add(fullAtk);
    }

    public Collection<Item> getEquipmentItems() {
        List<Item> result = new ArrayList<>();
        for (EquipSlot slot : getEquipment().listEquip()) {
            if (slot.getItem() != null) {
                result.add(slot.getItem());
            }
        }
        return result;
    }

    public Collection<TempEffect> getActiveTempEffects() {
        List<TempEffect> result = new ArrayList<>();
        for (TempEffect cond : getTempEffects().keySet()) {
            boolean isActive = getTempEffects().get(cond);
            if (isActive) {
                result.add(cond);
            }
        }
        return result;
    }

    public String getDescription(){
        return MessageFormat.format("{0}, $lvl {1} {2} {3}",
                name, getExperienceLevel(), getMulticlassString(), race.name);
    }

    private String getMulticlassString() {
        StringBuilder str = new StringBuilder();
        for (ClassLevel classLevel : classLevels) {
            str.append(classLevel.getClazz().getName());
            str.append("/");
        }
        str.deleteCharAt(str.length() - 1);
        return str.toString();
    }

    public Set<Condition> getActiveConditions() {
        return activeConditions;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getTendencyMoral() {
        return tendencyMoral;
    }

    public void setTendencyMoral(String tendencyMoral) {
        this.tendencyMoral = tendencyMoral;
    }

    public String getTendencyLoyality() {
        return tendencyLoyality;
    }

    public void setTendencyLoyality(String tendencyLoyality) {
        this.tendencyLoyality = tendencyLoyality;
    }

    public String getDivinity() {
        return divinity;
    }

    public void setDivinity(String divinity) {
        this.divinity = divinity;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getDungeonMaster() {
        return dungeonMaster;
    }

    public void setDungeonMaster(String dungeonMaster) {
        this.dungeonMaster = dungeonMaster;
    }

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
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

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public List<ClassLevel> getClassLevels() {
        return classLevels;
    }

    public void setClassLevels(List<ClassLevel> classLevels) {
        this.classLevels = classLevels;
    }

    public List<Feat> getFeats() {
        return feats;
    }

    public void setFeats(List<Feat> feats) {
        this.feats = feats;
    }

    public List<CharSkill> getSkills() {
        return skills;
    }

    public void setSkills(List<CharSkill> skills) {
        this.skills = skills;
    }

    public CharEquip getEquipment() {
        return equipment;
    }

    public void setEquipment(CharEquip equipment) {
        this.equipment = equipment;
    }

    public Map<Item, Integer> getBag() {
        return bag;
    }

    public void setBag(Map<Item, Integer> bag) {
        this.bag = bag;
    }

    public Map<TempEffect, Boolean> getTempEffects() {
        return tempEffects;
    }

    public void setTempEffects(Map<TempEffect, Boolean> tempEffects) {
        this.tempEffects = tempEffects;
    }

    public void setActiveConditions(Set<Condition> activeConditions) {
        this.activeConditions = activeConditions;
    }

    public List<AttackRound> getAttacks() {
        return attacks;
    }

    public void setAttacks(List<AttackRound> attacks) {
        this.attacks = attacks;
    }

    public DamageTaken getDamageTaken() {
        return damageTaken;
    }

    public void setDamageTaken(DamageTaken damageTaken) {
        this.damageTaken = damageTaken;
    }
}
