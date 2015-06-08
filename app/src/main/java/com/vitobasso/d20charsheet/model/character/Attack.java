package com.vitobasso.d20charsheet.model.character;

import com.vitobasso.d20charsheet.model.AbstractEntity;
import com.vitobasso.d20charsheet.model.DiceRoll;
import com.vitobasso.d20charsheet.model.effect.Modifier;
import com.vitobasso.d20charsheet.model.effect.ModifierTarget;
import com.vitobasso.d20charsheet.model.item.WeaponProperties;

/**
 * Created by Victor on 01/05/14.
 */
public class Attack extends AbstractEntity {

    public void setAttackBonus(int attackBonus) {
        this.attackBonus = attackBonus;
    }

    public void setWeaponReference(WeaponReference weaponReference) {
        this.weaponReference = weaponReference;
    }

    public enum WeaponReference {
        MAIN_HAND, OFFHAND
    }

    private int attackBonus;
    private WeaponProperties weapon;
    private WeaponReference weaponReference;

    public Attack() {
    }

    public Attack(int attackBonus, WeaponReference weaponReference) {
        this.setAttackBonus(attackBonus);
        this.setWeaponReference(weaponReference);
    }

    public Attack(String name, int attackBonus, WeaponReference weaponReference) {
        this.name = name;
        this.setAttackBonus(attackBonus);
        this.setWeaponReference(weaponReference);
    }

    void applyModifier(Modifier modifier) {
        applyModifier(modifier.getTarget(), modifier.getAmount());
    }

    public void applyModifier(ModifierTarget target, DiceRoll amount) {
        int fixedAmount = amount.roll();
        switch (target) {
            case HIT:
                setAttackBonus(getAttackBonus() + fixedAmount);
                break;
            case DAMAGE:
                getWeapon().getDamage().add(amount);
                break;
            case CRIT_RANGE:
                getWeapon().getCritical().addRange(fixedAmount);
                break;
            case CRIT_MULT:
                getWeapon().getCritical().addMultiplier(fixedAmount);
                break;
            default:
                throw new AssertionError("Couldn't set target for effect. Target: " + target);
        }
    }

    public int getAttackBonus() {
        return attackBonus;
    }

    public WeaponProperties getWeapon() {
        return weapon;
    }

    public WeaponReference getWeaponReference() {
        return weaponReference;
    }

    public void setWeapon(WeaponProperties weapon) {
        this.weapon = weapon;
    }

}
