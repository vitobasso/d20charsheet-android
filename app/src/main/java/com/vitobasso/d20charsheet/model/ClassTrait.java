package com.vitobasso.d20charsheet.model;

import com.vitobasso.d20charsheet.model.effect.Effect;
import com.vitobasso.d20charsheet.model.effect.EffectSource;
import com.vitobasso.d20charsheet.model.rulebook.Rule;

/**
 * Created by Victor on 05/04/14.
 */
public class ClassTrait extends Rule implements EffectSource {

    private Clazz clazz;
    private Effect effect = new Effect();
    private int level;

    public Clazz getClazz() {
        return clazz;
    }

    public void setClazz(Clazz clazz) {
        this.clazz = clazz;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
