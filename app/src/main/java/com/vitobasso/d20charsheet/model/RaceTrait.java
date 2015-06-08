package com.vitobasso.d20charsheet.model;

import com.vitobasso.d20charsheet.model.effect.Effect;
import com.vitobasso.d20charsheet.model.effect.EffectSource;
import com.vitobasso.d20charsheet.model.rulebook.Rule;

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
