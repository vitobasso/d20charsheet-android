package com.vituel.dndplayer.model;

/**
 * Represents active or frequently used Effect to be accessed quickly in the UI.
 * ActiveConditinals are tied to a particular Character (as opposed to Conditionals)
 * <p/>
 * Created by Victor on 21/03/14.
 */
public class CharTempEffect extends TempEffect {

    private boolean active;

    public void toggleActive() {
        setActive(!isActive());
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
