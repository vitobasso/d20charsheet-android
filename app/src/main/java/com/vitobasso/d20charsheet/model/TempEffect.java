package com.vitobasso.d20charsheet.model;

import com.vitobasso.d20charsheet.model.effect.Effect;
import com.vitobasso.d20charsheet.model.effect.EffectSource;
import com.vitobasso.d20charsheet.model.rulebook.Rule;

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
