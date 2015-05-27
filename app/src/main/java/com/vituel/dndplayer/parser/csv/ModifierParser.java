package com.vituel.dndplayer.parser.csv;

import com.vituel.dndplayer.model.DiceRoll;
import com.vituel.dndplayer.model.effect.Condition;
import com.vituel.dndplayer.model.effect.Modifier;
import com.vituel.dndplayer.model.effect.ModifierTarget;
import com.vituel.dndplayer.model.effect.ModifierType;
import com.vituel.dndplayer.parser.exception.ParseModifierException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.vituel.dndplayer.model.effect.Condition.Predicate;

/**
 * Created by Victor on 31/03/14.
 */
public class ModifierParser {

    public Modifier parse(String str) throws ParseModifierException {
        // TARGET(variation) amount type [condition]
        //(( 2  )((  4   ))) ( 5 ) ( 6 ) ((   8   ))
        Pattern p = Pattern.compile("^((\\w+)(\\((.*)\\))?)( [\\d|\\.|d|\\-|\\+]*)?( \\w+)?( \\[(.+)\\])?");
        Matcher m = p.matcher(str);
        if (m.find()) {

            //find parts
            String targetStr = m.group(2);
            if (targetStr != null) {
                targetStr = targetStr.toUpperCase();
            }
            String variationStr = m.group(4);
            String amountStr = m.group(5);
            if (amountStr != null) {
                amountStr = amountStr.trim();
            }
            String typeStr = m.group(6);
            if (typeStr != null) {
                typeStr = typeStr.trim().toUpperCase();
            }
            String conditionStr = m.group(8);

            try {

                //build modifier
                ModifierTarget target = ModifierTarget.valueOf(targetStr);
                ModifierType type = typeStr != null ? ModifierType.valueOf(typeStr) : null;
                DiceRoll amount = new DiceRoll(amountStr);
                Condition cond = parseCondition(conditionStr, target);

                return new Modifier(target, variationStr, amount, type, cond);

            } catch (IllegalArgumentException e) {
                throw new ParseModifierException("Unknown modifier format: " + str);
            }

        } else {
            throw new ParseModifierException("Unknown modifier format: " + str);
        }
    }

    private Condition parseCondition(String str, ModifierTarget target) {
        if(str == null){
            return null;
        }

        Pattern p = Pattern.compile("((when|using|against|related to) )?(.+)");
        Matcher m = p.matcher(str);
        if (m.find()) {
            String predicateStr = m.group(2);
            String name = m.group(3);

            Condition cond = new Condition();
            cond.setName(name);

            Predicate predicate = parsePredicate(target, predicateStr);
            cond.setPredicate(predicate);

            return cond;
        } else {
            return null;
        }
    }

    private Predicate parsePredicate(ModifierTarget target, String predicateStr) {
        if (predicateStr == null) {
            return target.getDefaultConditionPredicate();
        } else {
            return Predicate.fromString(predicateStr);
        }
    }

}
