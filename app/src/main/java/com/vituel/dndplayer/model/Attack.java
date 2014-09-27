package com.vituel.dndplayer.model;

/**
 * Created by Victor on 01/05/14.
 */
public class Attack extends AbstractEntity {

    public void setAttackBonus(int attackBonus) {
        this.attackBonus = attackBonus;
    }

    public void setReferenceType(WeaponReferenceType referenceType) {
        this.referenceType = referenceType;
    }

    public enum WeaponReferenceType {
        MAIN_HAND, OFFHAND
    }

    private int attackBonus;
    private WeaponProperties weapon;
    private WeaponReferenceType referenceType;

    public Attack() {
    }

    public Attack(int attackBonus, WeaponReferenceType referenceType) {
        this.setAttackBonus(attackBonus);
        this.setReferenceType(referenceType);
    }

    public Attack(String name, int attackBonus, WeaponReferenceType referenceType) {
        this.name = name;
        this.setAttackBonus(attackBonus);
        this.setReferenceType(referenceType);
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

    public WeaponReferenceType getReferenceType() {
        return referenceType;
    }

    public void setWeapon(WeaponProperties weapon) {
        this.weapon = weapon;
    }

}
