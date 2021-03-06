package com.vitobasso.d20charsheet.model.character;

import com.vitobasso.d20charsheet.model.AbstractEntity;
import com.vitobasso.d20charsheet.model.CharEntity;
import com.vitobasso.d20charsheet.model.ClassLevel;
import com.vitobasso.d20charsheet.model.Feat;
import com.vitobasso.d20charsheet.model.Race;
import com.vitobasso.d20charsheet.model.Skill;
import com.vitobasso.d20charsheet.model.TempEffect;
import com.vitobasso.d20charsheet.model.effect.AbilityModifier;
import com.vitobasso.d20charsheet.model.effect.Condition;
import com.vitobasso.d20charsheet.model.effect.ModifierSource;
import com.vitobasso.d20charsheet.model.item.EquipSlot;
import com.vitobasso.d20charsheet.model.item.Item;
import com.vitobasso.d20charsheet.model.item.WeaponItem;
import com.vitobasso.d20charsheet.model.item.WeaponProperties;
import com.vitobasso.d20charsheet.model.rulebook.Book;
import com.vitobasso.d20charsheet.util.business.AttackHelper;

import java.security.InvalidParameterException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.vitobasso.d20charsheet.model.character.Attack.WeaponReference.MAIN_HAND;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.AC;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.DAMAGE;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.FORT;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.HIT;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.INIT;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.REFL;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.SKILL;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.WILL;

/**
 * Persisted data inherent to the character. Values before calculation of bonus from items, traits and spells.
 * <p/>
 * Created by Victor on 27/02/14.
 */
public class CharBase extends AbstractEntity implements CharEntity {

    private int age;
    private double height, weight;
    private String tendencyMoral, tendencyLoyality;
    private String divinity;
    private String gender;
    private String player, dungeonMaster, campaign, creationDate;

    private int hitPoints;
    private int strength, dexterity, constitution, intelligence, wisdom, charisma;

    private Race race;
    private List<ClassLevel> classLevels = new ArrayList<>();
    private List<Feat> feats = new ArrayList<>();
    private List<CharSkill> skills = new ArrayList<>(); //CharSkill objects store graduation values (not final bonus)
    private CharEquip equipment = new CharEquip();
    private List<AttackRound> attacks = new ArrayList<>();
    private List<AbilityModifier> abilityMods = new ArrayList<>();
    private DamageTaken damageTaken = new DamageTaken();
    private List<CharTempEffect> tempEffects = new ArrayList<>();
    private Set<Condition> activeConditions = new HashSet<>();
    private Set<Book> activeBooks = new HashSet<>();

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
            return AttackHelper.unnarmedStrike();
        }
        //TODO shield attack
    }

    public void setStandardAbilityMods() {
        List<AbilityModifier> modifiers = new ArrayList<>();
        modifiers.add(new AbilityModifier(ModifierSource.DEX, AC));
        modifiers.add(new AbilityModifier(ModifierSource.CON, FORT));
        modifiers.add(new AbilityModifier(ModifierSource.DEX, REFL));
        modifiers.add(new AbilityModifier(ModifierSource.WIS, WILL));
        modifiers.add(new AbilityModifier(ModifierSource.STR, HIT));
        modifiers.add(new AbilityModifier(ModifierSource.STR, DAMAGE));
        modifiers.add(new AbilityModifier(ModifierSource.DEX, INIT));
        setAbilityMods(modifiers);
    }

    public void createAbilityModsForNewSkills() {
        for (CharSkill charSkill : getSkills()) {
            Skill skill = charSkill.getSkill();
            if(!hasAbilityModForSkill(skill)){
                AbilityModifier skillMod = new AbilityModifier(skill.getKeyAbility(), SKILL, skill.getName());
                addAbilityMod(skillMod);
            }
        }
    }

    private boolean hasAbilityModForSkill(Skill skill) {
        List<AbilityModifier> allMods = getAbilityMods();
        for (AbilityModifier mod : allMods) {
            if (mod.getTarget() == SKILL && skill.getName().equals(mod.getVariation())) {
                return true;
            }
        }
        return false;
    }

    public void createStandardAttacks() {
        //full attack
        AttackRound fullAtk = new AttackRound();
        int[] bonuses = AttackHelper.fullAttackPenalties(getBaseAttack());
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
        for (CharTempEffect tempEffect : getTempEffects()) {
            if (tempEffect.isActive()) {
                result.add(tempEffect.getTempEffect());
            }
        }
        return result;
    }

    public String getDescription(){
        return MessageFormat.format("{0}, $lvl {1} {2} {3}",
                name, getExperienceLevel(), getMulticlassString(), race.getName());
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

    public List<CharTempEffect> getTempEffects() {
        return tempEffects;
    }

    public void setTempEffects(List<CharTempEffect> tempEffects) {
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

    public List<AbilityModifier> getAbilityMods() {
        return abilityMods;
    }

    public void setAbilityMods(List<AbilityModifier> abilityMods) {
        this.abilityMods = abilityMods;
    }

    public void addAbilityMod(AbilityModifier abilityMod) {
        List<AbilityModifier> mods = getAbilityMods();
        if (mods == null) {
            mods = new ArrayList<>();
        }
        mods.add(abilityMod);
    }

    public DamageTaken getDamageTaken() {
        return damageTaken;
    }

    public void setDamageTaken(DamageTaken damageTaken) {
        this.damageTaken = damageTaken;
    }

    public Set<Book> getActiveBooks() {
        return activeBooks;
    }

    public void setActiveBooks(Set<Book> activeBooks) {
        this.activeBooks = activeBooks;
    }

}
