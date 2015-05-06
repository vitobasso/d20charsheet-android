package com.vituel.dndplayer.model;

import com.vituel.dndplayer.model.effect.Effect;
import com.vituel.dndplayer.model.effect.EffectSource;
import com.vituel.dndplayer.model.rulebook.Rule;

import java.util.List;

/**
 * Created by Victor on 30/03/14.
 */
public class Race extends Rule implements EffectSource {

    private Effect effect;

    private List<RaceTrait> traits;

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public List<RaceTrait> getTraits() {
        return traits;
    }

    public void setTraits(List<RaceTrait> traits) {
        this.traits = traits;
    }
}
