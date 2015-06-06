package com.vituel.dndplayer.model;

import com.vituel.dndplayer.model.effect.Effect;
import com.vituel.dndplayer.model.effect.EffectSource;
import com.vituel.dndplayer.model.rulebook.Rule;

/**
 * Created by Victor on 04/04/2015.
 */
public class RaceTrait extends Rule implements EffectSource {

    private Race race;
    private Effect effect;

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    @Override
    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

}
