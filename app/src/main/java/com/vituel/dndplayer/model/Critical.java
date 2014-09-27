package com.vituel.dndplayer.model;

import java.io.Serializable;

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

    @Override
    public String toString() {
        assert getRange() > 0 && getRange() < 20;
        if (getRange() == 1) {
            return "x" + getMultiplier();
        } else {
            return 21 - getRange() + "-20/x" + getMultiplier();
        }
    }

    public Critical copy(){
        return new Critical(getRange(), getMultiplier());
    }
}
