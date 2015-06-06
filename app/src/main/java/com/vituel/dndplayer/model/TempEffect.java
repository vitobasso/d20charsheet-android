package com.vituel.dndplayer.model;

import com.vituel.dndplayer.model.effect.Effect;
import com.vituel.dndplayer.model.effect.EffectSource;
import com.vituel.dndplayer.model.rulebook.Rule;

/**
 * Effects to be toggled on/off as opposed to fixed effects (from equip, class, race, feats).
 * E.g.: Spells, item activation effects and combat modifiers.
 * <p/>
 * Created by Victor on 21/03/14.
 */
public class TempEffect extends Rule implements EffectSource {

    private Effect effect;

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }
}
