package com.vitobasso.d20charsheet.model;

import com.vitobasso.d20charsheet.model.effect.Effect;
import com.vitobasso.d20charsheet.model.effect.EffectSource;
import com.vitobasso.d20charsheet.model.rulebook.Rule;

/**
 * Created by Victor on 25/02/14.
 */
public class Feat extends Rule implements EffectSource {

    private Effect effect;

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }
}
