package com.vitobasso.d20charsheet.util.business;

import android.content.Context;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.model.Critical;
import com.vitobasso.d20charsheet.model.DiceRoll;
import com.vitobasso.d20charsheet.model.character.Attack;
import com.vitobasso.d20charsheet.model.character.AttackRound;
import com.vitobasso.d20charsheet.model.character.CharBase;
import com.vitobasso.d20charsheet.model.item.WeaponProperties;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static com.vitobasso.d20charsheet.model.character.Attack.WeaponReference.MAIN_HAND;
import static com.vitobasso.d20charsheet.model.character.Attack.WeaponReference.OFFHAND;

/**
 * Created by Victor on 13/09/14.
 */
public class AttackHelper {

    private Context context;
    private CharBase base;

    public AttackHelper(Context context, CharBase base) {
        this.context = context;
        this.base = base;
    }

    public static int[] fullAttackPenalties(int bab) {
        int[] values = new int[1 + (bab - 1) / 5];
        for (int i = 0; i < values.length; i++) {
            values[i] = -i * 5;
        }
        return values;
    }

    public static WeaponProperties unnarmedStrike(){
        //TODO monk dmg
        WeaponProperties weapon = new WeaponProperties();
        weapon.setName("$unnarmed_strike");
        weapon.setDamage(new DiceRoll("1d6"));
        weapon.setCritical(new Critical(1, 2));
        return weapon;
    }

    /**
     * Groups Attacks by weapon and builds the bonus sequence string for each weapon.
     *
     * @return Map with representative Attacks as key and the bonuses string as values
     *
     * TODO decide if it's necessary to have independent damage or critical values
     * on different attacks of same weapon in the same attack round.
     * If so, group them based on an "equals" test.
     *
     */
    public static Map<Attack, String> groupBonusByWeapon(AttackRound attackRound) {
        List<Attack> allAttacks = attackRound.getAttacks();
        Map<Attack, String> grouping = new TreeMap<>(sortByWeapon());
        for (Attack attack : allAttacks) {
            addAllBonusesWithSameWeapon(attack, allAttacks, grouping);
        }
        return grouping;
    }

    private static Comparator<Attack> sortByWeapon() {
        //order according to the enum definition
        return new Comparator<Attack>() {
            @Override
            public int compare(Attack lhs, Attack rhs) {
                return lhs.getWeaponReference().compareTo(rhs.getWeaponReference());
            }
        };
    }

    private static void addAllBonusesWithSameWeapon(Attack attack, List<Attack> allAttacks, Map<Attack, String> grouping) {
        if (!isWeaponInMap(attack, grouping)) {
            List<Integer> bonuses = getAllBonusesOnSameWeapon(allAttacks, attack);
            String bonusesString = buildAttackBonusString(bonuses);
            grouping.put(attack, bonusesString);
        }
    }

    private static boolean isWeaponInMap(Attack attack, Map<Attack, String> grouping) {
        for (Attack groupedAttack : grouping.keySet()) {
            if (isSameWeapon(attack, groupedAttack)) {
                return true;
            }
        }
        return false;
    }

    private static List<Integer> getAllBonusesOnSameWeapon(List<Attack> list, Attack example) {
        List<Integer> bonuses = new ArrayList<>();
        for (Attack attack : list) {
            if (isSameWeapon(attack, example)) {
                bonuses.add(attack.getAttackBonus());
            }
        }
        return bonuses;
    }

    private static String buildAttackBonusString(List<Integer> values) {
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

    private static boolean isSameWeapon(Attack left, Attack right){
        return left.getWeaponReference() == right.getWeaponReference();
    }

    public static WeaponProperties getRepresentativeWeapon(AttackRound round, Attack.WeaponReference refType){
        for (Attack attack : round.getAttacks()) {
            if(attack.getWeaponReference() == refType){
                return attack.getWeapon();
            }
        }
        return null;
    }

    public String createDefaultAttackRoundName(AttackRound attackRound) {
        Set<Attack.WeaponReference> weaponsUsed = new HashSet<>();
        for (Attack attack : attackRound.getAttacks()) {
            weaponsUsed.add(attack.getWeaponReference());
        }
        return createDefaultAttackRoundName(weaponsUsed);
    }

    private String createDefaultAttackRoundName(Set<Attack.WeaponReference> weaponsUsed) {
        String mainWeaponName = base.getWeapon(MAIN_HAND).getName();
        String offhandWeaponName = base.getWeapon(OFFHAND).getName();
        if (weaponsUsed.size() == 1) {
            if (weaponsUsed.contains(MAIN_HAND)) {
                return mainWeaponName;
            } else {
                return offhandWeaponName;
            }
        } else if (weaponsUsed.size() == 2) {
            return mainWeaponName + "/" + offhandWeaponName;
        } else {
            return context.getString(R.string.attacks);
        }
    }

}
