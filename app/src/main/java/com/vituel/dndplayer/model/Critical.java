package com.vituel.dndplayer.model;

import junit.framework.Assert;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Victor on 16/08/14.
 */
public class Critical implements Serializable {

    private int range;
    private int multiplier;

    public Critical() {
    }

    public Critical(int range, int multiplier) {
        this.setRange(range);
        this.setMultiplier(multiplier);
    }

    public Critical(String expression){
        decode(expression);
    }

    public void addRange(int value) {
        this.setRange(this.getRange() + value);
    }

    public void addMultiplier(int value) {
        this.setMultiplier(this.getMultiplier() + value);
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    private void decode(String expression){
        Assert.assertNotNull(expression);
        Assert.assertFalse(expression.isEmpty());

        Pattern p = Pattern.compile("((\\d{1,2})\\-20/)?(x|×)(\\d{1,2})");
        Matcher m = p.matcher(expression);

        if(m.find()){
            String rangeStr = m.group(2);
            String multStr = m.group(4);
            int range = rangeStr == null ? 1 : 21 - Integer.valueOf(rangeStr);
            int multiplier = Integer.valueOf(multStr);
            Assert.assertTrue("Invalid critical range", range > 0 && range < 20);
            Assert.assertTrue("Invalid critical multiplier", multiplier > 0);
            this.range = range;
            this.multiplier = multiplier;
        }else {
            Assert.fail("Can't match critical expression pattern");
        }
    }

    @Override
    public String toString() {
        Assert.assertTrue("Invalid critical range", getRange() > 0 && getRange() < 20);
        if (getRange() == 1) {
            return "×" + getMultiplier();
        } else {
            return 21 - getRange() + "-20/×" + getMultiplier();
        }
    }

    public Critical copy(){
        return new Critical(getRange(), getMultiplier());
    }
}
