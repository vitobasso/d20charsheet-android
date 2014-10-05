package com.vituel.dndplayer.model;

import static com.vituel.dndplayer.model.AbstractEffect.Type.TEMPORARY;

/**
 * Effects to be toggled on/off as opposed to fixed effects (from equip, class, race, feats).
 * E.g.: Spells, item activation effects and combat modifiers.
 * <p/>
 * Created by Victor on 21/03/14.
 */
public class TempEffect extends AbstractEffect {

    public TempEffect() {
        super(TEMPORARY);
    }

}
