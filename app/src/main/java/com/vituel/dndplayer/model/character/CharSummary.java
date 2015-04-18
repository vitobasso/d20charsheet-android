package com.vituel.dndplayer.model.character;

import android.content.Context;
import android.util.Log;

import com.esotericsoftware.kryo.Kryo;
import com.vituel.dndplayer.R;
import com.vituel.dndplayer.dao.SkillDao;
import com.vituel.dndplayer.model.ClassLevel;
import com.vituel.dndplayer.model.DiceRoll;
import com.vituel.dndplayer.model.Size;
import com.vituel.dndplayer.model.Skill;
import com.vituel.dndplayer.model.effect.AbilityModifier;
import com.vituel.dndplayer.model.effect.Condition;
import com.vituel.dndplayer.model.effect.Effect;
import com.vituel.dndplayer.model.effect.EffectSource;
import com.vituel.dndplayer.model.effect.Modifier;
import com.vituel.dndplayer.model.effect.ModifierSource;
import com.vituel.dndplayer.model.effect.ModifierTarget;
import com.vituel.dndplayer.model.item.WeaponProperties;
import com.vituel.dndplayer.util.i18n.ModifierStringConverter;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.vituel.dndplayer.model.Size.MEDIUM;
import static com.vituel.dndplayer.model.effect.ModifierTarget.AC;
import static com.vituel.dndplayer.model.effect.ModifierTarget.CHA;
import static com.vituel.dndplayer.model.effect.ModifierTarget.CON;
import static com.vituel.dndplayer.model.effect.ModifierTarget.DEX;
import static com.vituel.dndplayer.model.effect.ModifierTarget.HIT;
import static com.vituel.dndplayer.model.effect.ModifierTarget.HP;
import static com.vituel.dndplayer.model.effect.ModifierTarget.INT;
import static com.vituel.dndplayer.model.effect.ModifierTarget.SKILL;
import static com.vituel.dndplayer.model.effect.ModifierTarget.STR;
import static com.vituel.dndplayer.model.effect.ModifierTarget.WIS;

/**
 * Values shown in the Summary. Including bonuses from items, traits and spells.
 * <p/>
 * Created by Victor on 25/02/14.
 */
public class CharSummary {

    private Context context;
    private ModifierStringConverter modStr;

    private CharBase base;
    private int hitPoints;
    private int strength;
    private int dexterity;
    private int constitution;
    private int intelligence;
    private int wisdom;
    private int charisma;
    private int maxDexMod;
    private int armorClass;
    private int damageReduction;
    private int magicResistance;
    private int concealment;
    private int fortitude;
    private int reflex;
    private int will;
    private int speed;
    private int initiative;
    private Size size;
    private Map<String, CharSkill> skills; //CharSkill objects store final bonus values (not graduation)
    private List<AttackRound> attacks;
    private Set<Condition> referencedConditions;

    public CharSummary(Context context, CharBase base) {
        this.context = context;
        this.modStr = new ModifierStringConverter(context);
        this.setBase(base);

        //init
        initBasic();
        initAttacks();

        //effects from race, class, feats, equipment and effects
        applyModifiers(getBaseModifiers());
        if (base.getRace() != null) {
            apply(base.getRace());
            applyEffects(base.getRace().getTraits());
        }
        for (ClassLevel classLevel : base.getClassLevels()) {
            apply(classLevel);
            applyEffects(classLevel.getTraits());
        }
        applyEffects(base.getFeats());
        applyEffects(base.getEquipmentItems());
        applyEffects(base.getActiveTempEffects());

        //effects from other attributes
        applyModifiers(getAbilityModifiers().keySet());
        applyModifiers(getSizeModifiers());

        //tempHP
        setHitPoints(getHitPoints() + base.getDamageTaken().getTempHp()); //breakdown is managed in DamageGuiManager, separately from other effects
    }

    private void initBasic() {
        setAttacks(new ArrayList<AttackRound>());
        setSize(MEDIUM);
        setSkills(new HashMap<String, CharSkill>());
        setMaxDexMod(Integer.MAX_VALUE);
        setReferencedConditions(new HashSet<Condition>());
    }

    /**
     * Uses the same model classes (Attack, WeaponProperties) as CharBase but clones them to
     * allow independent application of modifiers.
     */
    private void initAttacks() {
        Kryo cloner = new Kryo();
        setAttacks(cloner.copy(getBase().getAttacks())); //cloned objects will be modified by magic bonuses, etc
        for (AttackRound attackRound : getAttacks()) {
            for (Attack attack : attackRound.getAttacks()) {
                WeaponProperties weaponBase = base.getWeapon(attack.getWeaponReference());
                attack.setWeapon(cloner.copy(weaponBase));
            }
        }
    }

    public List<Modifier> getBaseModifiers() {
        List<Modifier> modifiers = new ArrayList<>();

        //hp and ac
        modifiers.add(new Modifier(HP, getBase().getHitPoints()));
        modifiers.add(new Modifier(AC, 10));

        //abilities
        modifiers.add(new Modifier(STR, getBase().getStrength()));
        modifiers.add(new Modifier(DEX, getBase().getDexterity()));
        modifiers.add(new Modifier(CON, getBase().getConstitution()));
        modifiers.add(new Modifier(INT, getBase().getIntelligence()));
        modifiers.add(new Modifier(WIS, getBase().getWisdom()));
        modifiers.add(new Modifier(CHA, getBase().getCharisma()));

        //skills
        for (CharSkill skillGrad : getBase().getSkills()) {
            modifiers.add(new Modifier(SKILL, skillGrad.getSkill().getName(), skillGrad.getScore()));
        }

        return modifiers;
    }

    public Map<Modifier, String> getAbilityModifiers() {
        Map<Modifier, String> result = new HashMap<>();

        //hp = con * level
        int lvl = getBase().getExperienceLevel();
        int conMod = getAbilityModifier(ModifierSource.CON);
        String conLvlLabel = String.format("%s × %s", getString(R.string.con), getString(R.string.level));
        result.put(new Modifier(HP, lvl * conMod), conLvlLabel);

        //others
        for (AbilityModifier mod : base.getAbilityMods()) {
            int value = getAbilityModifier(mod.getAbility());
            value = mod.getMultiplier().multiply(value);
            Modifier resultingMod = new Modifier(mod.getTarget(), mod.getVariation(), value);
            String label = mod.getAbility().getLabel(context);
            result.put(resultingMod, label);
        }

        return result;
    }

    public List<Modifier> getSizeModifiers() {
        List<Modifier> modifiers = new ArrayList<>();
        modifiers.add(new Modifier(HIT, getSize().getCombatModifier()));
        modifiers.add(new Modifier(AC, getSize().getCombatModifier()));
        return modifiers;
    }

    private void applyEffects(Collection<? extends EffectSource> effectSources) {
        if (effectSources != null) {
            for (EffectSource source : effectSources) {
                apply(source.getEffect());
            }
        }
    }

    private void apply(EffectSource effectSource) {
        apply(effectSource.getEffect());
    }

    private void apply(Effect effect) {
        if (effect != null) {
            for (Modifier modifier : effect.getModifiers()) {
                apply(modifier);
            }
        }
    }

    private void applyModifiers(Collection<Modifier> modifiers) {
        if (modifiers != null) {
            for (Modifier modifier : modifiers) {
                apply(modifier);
            }
        }
    }

    private void apply(Modifier modifier) {
        ModifierTarget target = modifier.getTarget();
        DiceRoll amount = modifier.getAmount();
        String variation = modifier.getVariation();
        Condition condition = modifier.getCondition();

        if (!amount.isFixed()) {
            Log.w(getClass().getSimpleName(), "Could not apply modifier. Amount isn't fixed. " + modifier.getTarget());
            return;
        }
        int fixedAmount = amount.toInt();

        if (condition != null) {
            getReferencedConditions().add(condition);
            if (!getBase().getActiveConditions().contains(condition)) {
                return;
            }
        }

        switch (target) {
            case HP:
                setHitPoints(getHitPoints() + fixedAmount);
                break;
            case STR:
                setStrength(getStrength() + fixedAmount);
                break;
            case DEX:
                setDexterity(getDexterity() + fixedAmount);
                break;
            case CON:
                setConstitution(getConstitution() + fixedAmount);
                break;
            case INT:
                setIntelligence(getIntelligence() + fixedAmount);
                break;
            case WIS:
                setWisdom(getWisdom() + fixedAmount);
                break;
            case CHA:
                setCharisma(getCharisma() + fixedAmount);
                break;
            case AC:
                setArmorClass(getArmorClass() + fixedAmount);
                break;
            case FORT:
                setFortitude(getFortitude() + fixedAmount);
                break;
            case REFL:
                setReflex(getReflex() + fixedAmount);
                break;
            case WILL:
                setWill(getWill() + fixedAmount);
                break;
            case SPEED:
                setSpeed(getSpeed() + fixedAmount);
                break;
            case INIT:
                setInitiative(getInitiative() + fixedAmount);
                break;
            case HIT:
            case DAMAGE:
            case CRIT_RANGE:
            case CRIT_MULT:
                for (AttackRound atk : getAttacks()) {
                    atk.applyModifier(modifier);
                }
                break;
            case SIZE:
                setSize(Size.fromIndex(getSize().getIndex() + fixedAmount));
                break;
            case MAX_DEX:
                setMaxDexMod(Math.min(getMaxDexMod(), fixedAmount));
                break;
            case MR:
                setMagicResistance(getMagicResistance() + fixedAmount);
                break;
            case DR:
                setDamageReduction(getDamageReduction() + fixedAmount);
                break;
            case CONCEAL:
                setConcealment(Math.max(getConcealment(), fixedAmount));
                break;
            case SAVES:
                setFortitude(getFortitude() + fixedAmount);
                setReflex(getReflex() + fixedAmount);
                setWill(getWill() + fixedAmount);
                break;
            case SKILL:
                CharSkill skillBonus = getSkills().get(variation);
                if (skillBonus == null) {
                    //find skill in db
                    skillBonus = addSkill(variation);
                }
                if (skillBonus != null) { // can be null if skill is not in DB
                    skillBonus.addScore(fixedAmount);
                }
                break;
            default:
                Log.w(getClass().getSimpleName(), "Couldn't set target for effect. Target: " + target);
        }

    }

    private CharSkill addSkill(String skillName) {
        SkillDao skillDao = new SkillDao(context);
        Skill skill = skillDao.findByName(skillName);
        skillDao.close();

        if (skill == null) {
            return null;
        } else {
            CharSkill skillBonus = new CharSkill(skill);
            getSkills().put(skillName, skillBonus);
            return skillBonus;
        }
    }

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

    private String getString(int res) {
        return context.getResources().getString(res);
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

    public void setReferencedConditions(Set<Condition> referencedConditions) {
        this.referencedConditions = referencedConditions;
    }

    @Override
    public String toString() {
        return getBase().getName();
    }
}