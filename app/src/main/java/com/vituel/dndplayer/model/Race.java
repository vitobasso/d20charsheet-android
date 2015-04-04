package com.vituel.dndplayer.model;

import java.util.List;

/**
 * Created by Victor on 30/03/14.
 */
public class Race extends AbstractEntity {

    private Effect effect;

    private List<Effect> traits;

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public List<Effect> getTraits() {
        return traits;
    }

    public void setTraits(List<Effect> traits) {
        this.traits = traits;
    }
}
