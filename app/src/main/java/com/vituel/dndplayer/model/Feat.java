package com.vituel.dndplayer.model;

/**
 * Created by Victor on 25/02/14.
 */
public class Feat extends AbstractEntity {

    private Effect effect;

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }
}
