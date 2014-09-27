package com.vituel.dndplayer.model;

/**
 * Represents active or frequently used Effect to be accessed quickly in the UI.
 * ActiveConditinals are tied to a particular Character (as opposed to Conditionals)
 * <p/>
 * Created by Victor on 21/03/14.
 */
public class ActiveTempEffect extends AbstractEntity implements Comparable<ActiveTempEffect> {

    private TempEffect tempEffect;
    private boolean active;

    public ActiveTempEffect() {
    }

    public void toggleActive() {
        setActive(!isActive());
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public TempEffect getTempEffect() {
        return tempEffect;
    }

    @Override
    public int compareTo(ActiveTempEffect another) {
        return getTempEffect().compareTo(another.getTempEffect());
    }

    @Override
    public int hashCode() {
        return getTempEffect().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof ActiveTempEffect) {
            ActiveTempEffect another = ((ActiveTempEffect) o);
            return getTempEffect().equals(another.getTempEffect());
        }
        return false;
    }

    @Override
    public String toString() {
        return getTempEffect().toString();
    }

    public void setTempEffect(TempEffect tempEffect) {
        this.tempEffect = tempEffect;
    }
}
