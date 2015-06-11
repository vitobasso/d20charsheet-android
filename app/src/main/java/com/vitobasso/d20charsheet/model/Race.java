package com.vitobasso.d20charsheet.model;

import com.vitobasso.d20charsheet.model.effect.Effect;
import com.vitobasso.d20charsheet.model.effect.EffectSource;
import com.vitobasso.d20charsheet.model.rulebook.Rule;

import java.util.List;

/**
 * Created by Victor on 30/03/14.
 */
public class Race extends Rule implements EffectSource {

    private Effect effect;

    private List<RaceTrait> traits;

    public Race() {
    }

    //used by jackson
    public Race(long id) {
        super(id);
    }

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
