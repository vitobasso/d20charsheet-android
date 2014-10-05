package com.vituel.dndplayer.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Victor on 26/02/14.
 */
public class DiceRoll implements Serializable {

    private Map<Integer, Integer> dice = new HashMap<>();
    private int fixedValue;

    public DiceRoll() {
    }

    public DiceRoll(int fixedValue) {
        this.fixedValue = fixedValue;
    }

    public DiceRoll(String expression) {
        decode(expression);
    }

    private void decode(String expression) {
        if (expression == null || expression.isEmpty()) {
            return;
        }

        Pattern p = Pattern.compile("(\\-?\\d+(d(2|3|4|6|8|10|12|20|100))?)[\\+\\-]?");
        Matcher m = p.matcher(expression);

        while (m.find()) {
            String str = m.group(1);

            String[] parts = str.split("d");
            int count = Integer.valueOf(parts[0]);
            int faces = 0;
            if (parts.length > 1) {
                faces = Integer.valueOf(parts[1]);
            }

            if (faces > 0) {
                Integer currentValue = dice.get(faces);
                if (currentValue == null) {
                    currentValue = 0;
                }
                dice.put(faces, currentValue + count);
            } else {
                fixedValue += count;
            }
        }
    }

    private String encode() {
        String str = "";

        for (int faces : dice.keySet()) {
            if (!str.isEmpty() && faces >= 0) {
                str += "+";
            }
            str += dice.get(faces) + "d" + faces;
        }

        if (!str.isEmpty() && fixedValue > 0) {
            str += "+" + fixedValue;
        } else if (str.isEmpty() || fixedValue < 0) {
            str += fixedValue;
        }

        return str;
    }

    public void add(DiceRoll other) {

        //add all keys from the other DiceRoll
        dice.keySet().addAll(other.dice.keySet());

        //sum values for each key
        for (int faceNumber : dice.keySet()) {
            int thisValue = dice.containsKey(faceNumber) ? dice.get(faceNumber) : 0;
            int otherValue = other.dice.containsKey(faceNumber) ? other.dice.get(faceNumber) : 0;
            int sum = thisValue + otherValue;
            dice.put(faceNumber, sum);
        }

        //add the fixed value too
        fixedValue += other.fixedValue;
    }

    public void add(int i) {
        add(new DiceRoll(i));
    }

    public boolean isFixed() {
        return dice.isEmpty();
    }

    public boolean isPositive(){
        return fixedValue > 0;
    }

    public int roll() {
        int result = fixedValue;

        for (int faces : dice.keySet()) {
            for (int i = 0; i < dice.get(faces); i++) {
                result += (int) (Math.random() * faces);
            }
        }

        return result;
    }

    public int toInt(){
        if(isFixed()){
            return fixedValue;
        }else{
            throw new IllegalStateException();
        }
    }

    @Override
    public String toString() {
        return encode();
    }

    public DiceRoll copy(){
        DiceRoll copy = new DiceRoll();
        copy.fixedValue = fixedValue;
        copy.dice = new HashMap<>(dice);
        return copy;
    }

}
