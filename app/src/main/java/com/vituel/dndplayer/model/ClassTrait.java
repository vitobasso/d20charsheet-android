package com.vituel.dndplayer.model;

import com.vituel.dndplayer.model.effect.Effect;
import com.vituel.dndplayer.model.effect.EffectSource;

/**
 * Created by Victor on 05/04/14.
 */
public class ClassTrait extends AbstractEntity implements EffectSource {

    private Clazz clazz;
    private Effect effect;
    private int level;
    private String overridenTraitName;

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

    public String getOverridenTraitName() {
        return overridenTraitName;
    }

    public void setOverridenTraitName(String overridenTraitName) {
        this.overridenTraitName = overridenTraitName;
    }
}
