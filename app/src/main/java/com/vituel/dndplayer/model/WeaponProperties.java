package com.vituel.dndplayer.model;

import java.util.ArrayList;
import java.util.List;

import static com.vituel.dndplayer.model.ModifierTarget.*;

/**
 * Created by Victor on 01/05/14.
 */
public class WeaponProperties extends AbstractEffect {

    public enum RangeType {
        RANGED, THROWN, MELEE
        //ranged gets DEX on atk and dmg
        //thrown gets DEX on atk and STR on dmg
        //melee gets STR on atk and dmg, dmg is 1.5x STR if dual wielding (and not light) and 0.5x STR if offhand
    }

    public enum WeightType {
        NORMAL, LIGHT
        //normal gets 1.5x STR when dual wielding
    }

    private DiceRoll damage;
    private Critical critical;
    private int range;
    private String statusEffect;
    private RangeType rangeType;
    private WeightType weightType;

    public WeaponProperties() {
        super(Type.EQUIP_MAGIC);
        this.setDamage(new DiceRoll());
        this.setCritical(new Critical(1, 2));
        this.setRange(1);
        this.setRangeType(RangeType.MELEE);
        this.setWeightType(WeightType.NORMAL);
        this.getModifiers().addAll(getAsModifiers());
    }

    public WeaponProperties(String name) {
        this();
        this.name = name;
    }

    public WeaponProperties(String name, DiceRoll damage, Critical critical, int range) {
        this();
        this.setDamage(damage);
        this.setCritical(critical);
        this.setRange(range);
        this.getModifiers().addAll(getAsModifiers());
        this.name = name;
    }

    public WeaponProperties(String name, DiceRoll damage, Critical critical, int range, String statusEffect,
                            RangeType rangeType, WeightType weightType) {
        this(name);
        this.setDamage(damage);
        this.setCritical(critical);
        this.setRange(range);
        this.setStatusEffect(statusEffect);
        this.setRangeType(rangeType);
        this.setWeightType(weightType);
    }

    public DiceRoll getDamage() {
        return damage;
    }

    public Critical getCritical() {
        return critical;
    }

    public int getRange() {
        return range;
    }

    public String getStatusEffect() {
        return statusEffect;
    }

    public RangeType getRangeType() {
        return rangeType;
    }

    public WeightType getWeightType() {
        return weightType;
    }

    public void setDamage(DiceRoll damage) {
        this.damage = damage;
    }

    public void setCritical(Critical critical) {
        this.critical = critical;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void setStatusEffect(String statusEffect) {
        this.statusEffect = statusEffect;
    }

    public void setRangeType(RangeType rangeType) {
        this.rangeType = rangeType;
    }

    public void setWeightType(WeightType weightType) {
        this.weightType = weightType;
    }

    public List<Modifier> getAsModifiers() {
        Modifier dmg = new Modifier(DAMAGE, getDamage(), this);
        Modifier critMult = new Modifier(CRIT_MULT, getCritical().getMultiplier(), this);
        Modifier critRange = new Modifier(CRIT_RANGE, getCritical().getRange(), this);

        List<Modifier> modifiers = new ArrayList<>();
        modifiers.add(dmg);
        modifiers.add(critRange);
        modifiers.add(critMult);
        return modifiers;
    }

}
