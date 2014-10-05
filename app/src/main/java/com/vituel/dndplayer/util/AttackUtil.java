package com.vituel.dndplayer.util;

import com.vituel.dndplayer.model.Attack;
import com.vituel.dndplayer.model.AttackRound;
import com.vituel.dndplayer.model.Critical;
import com.vituel.dndplayer.model.DiceRoll;
import com.vituel.dndplayer.model.WeaponProperties;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Victor on 13/09/14.
 */
public class AttackUtil {

    public static int[] fullAttackPenalties(int bab) {
        int[] values = new int[1 + (bab - 1) / 5];
        for (int i = 0; i < values.length; i++) {
            values[i] = -i * 5;
        }
        return values;
    }

    /**
     * Groups Attacks by weapon and builds the bonus sequence string for each weapon.
     *
     * @param attacks All attacks in an attack round
     * @return Map with representative Attacks as key and the bonuses string as values
     *
     * TODO decide if it's necessary to have independent damage or critical values
     * on different attacks of same weapon in the same attack round.
     * If so, group them based on an "equals" test.
     *
     */
    public static Map<Attack, String> groupBonusByWeapon(List<Attack> attacks) {
        Map<Attack, String> grouped = new TreeMap<>(new Comparator<Attack>() {
            @Override
            public int compare(Attack lhs, Attack rhs) {
                //order according to the enum definition
                return lhs.getWeaponReference().compareTo(rhs.getWeaponReference());
            }
        });

        for (Attack attack : attacks) {

            // check if this one is part of a group already picked
            boolean contains = false;
            for (Attack groupedAttack : grouped.keySet()) {
                if (isSameWeapon(attack, groupedAttack)) {
                    contains = true;
                    break;
                }
            }

            // if it's a new group, add it to the list with the bonus of each member
            if (!contains) {
                List<Integer> bonuses = selectBonusesForAttackGroup(attacks, attack);
                String bonusesString = attackBonusesToString(bonuses);
                grouped.put(attack, bonusesString);
            }

        }
        return grouped;
    }

    private static List<Integer> selectBonusesForAttackGroup(List<Attack> list, Attack example) {
        List<Integer> bonuses = new ArrayList<>();
        for (Attack attack : list) {
            if (isSameWeapon(attack, example)) {
                bonuses.add(attack.getAttackBonus());
            }
        }
        return bonuses;
    }

    private static String attackBonusesToString(List<Integer> values) {
        StringBuilder str = new StringBuilder();
        for (int value : values) {
            if (value >= 0) {
                str.append("+");
            }
            str.append(value);
            str.append("/");
        }
        str.delete(str.length() - 1, str.length());
        return str.toString();
    }

    private static boolean isSameWeapon(Attack attack1, Attack attack2){
        return attack1.getWeaponReference() == attack2.getWeaponReference();
    }

    public static WeaponProperties unnarmedStrike(){
        return new WeaponProperties("$unnarmed_strike", new DiceRoll("1d6"), new Critical(1, 2), 1); //TODO monk dmg
    }

    public static WeaponProperties getRepresentativeWeapon(AttackRound round, Attack.WeaponReference refType){
        for (Attack attack : round.getAttacks()) {
            if(attack.getWeaponReference() == refType){
                return attack.getWeapon();
            }
        }
        return null;
    }

}
