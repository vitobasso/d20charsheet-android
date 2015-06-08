package com.vitobasso.d20charsheet.model.character;

import com.vitobasso.d20charsheet.model.TempEffect;
import com.vitobasso.d20charsheet.model.effect.Effect;
import com.vitobasso.d20charsheet.model.effect.EffectSource;

import java.io.Serializable;

/**
 * Represents active or frequently used Effect to be accessed quickly in the UI.
 * <p/>
 * Created by Victor on 21/03/14.
 */
public class CharTempEffect implements Serializable, Comparable<CharTempEffect>, EffectSource {

    private TempEffect tempEffect;

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

    public TempEffect getTempEffect() {
        return tempEffect;
    }

    public void setTempEffect(TempEffect tempEffect) {
        this.tempEffect = tempEffect;
    }

    @Override
    public String toString() {
        return tempEffect.toString() + " " + active;
    }

    @Override
    public int compareTo(CharTempEffect another) {
        return tempEffect.getName().compareTo(another.tempEffect.getName());
    }

    @Override
    public Effect getEffect() {
        return tempEffect.getEffect();
    }

    @Override
    public void setEffect(Effect effect) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        return tempEffect.getName();
    }

    @Override
    public void setName(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getId() {
        return tempEffect.getId();
    }

    @Override
    public void setId(long id) {
        throw new UnsupportedOperationException();
    }
}
