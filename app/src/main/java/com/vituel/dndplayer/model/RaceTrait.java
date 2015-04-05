package com.vituel.dndplayer.model;

/**
 * Created by Victor on 04/04/2015.
 */
public class RaceTrait extends AbstractEntity implements EffectSource {

    private Effect effect;

    @Override
    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

}
