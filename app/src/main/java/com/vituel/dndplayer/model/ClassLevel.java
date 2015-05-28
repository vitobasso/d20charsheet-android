package com.vituel.dndplayer.model;

import com.vituel.dndplayer.model.effect.Effect;
import com.vituel.dndplayer.model.effect.EffectSource;
import com.vituel.dndplayer.model.effect.Modifier;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static com.vituel.dndplayer.model.effect.ModifierTarget.FORT;
import static com.vituel.dndplayer.model.effect.ModifierTarget.HIT;
import static com.vituel.dndplayer.model.effect.ModifierTarget.REFL;
import static com.vituel.dndplayer.model.effect.ModifierTarget.WILL;

/**
 * Created by Victor on 31/03/14.
 */
public class ClassLevel extends AbstractEntity implements EffectSource {

    private Clazz clazz;
    private int level;

    private Effect effect;

    public ClassLevel() {
        level = 1;
    }

    public int getBaseAttack() {
        return getClazz().getBaseAttack(getLevel());
    }

    @Override
    public String getName() {
        if (getClazz() != null) {
            return MessageFormat.format("{0} {1}", getClazz().getName(), getLevel());
        } else {
            return null;
        }
    }

    private Effect buildEffect() {
        Effect effect = new Effect();
        if (clazz != null && level > 0) {
            effect.setModifiers(buildModifiers());
        }
        return effect;
    }

    private List<Modifier> buildModifiers() {
        List<Modifier> modifiers = new ArrayList<>();
        modifiers.add(new Modifier(FORT, clazz.getBaseFortitude(level)));
        modifiers.add(new Modifier(REFL, clazz.getBaseReflex(level)));
        modifiers.add(new Modifier(WILL, clazz.getBaseWill(level)));
        modifiers.add(new Modifier(HIT, clazz.getBaseAttack(level)));
        return modifiers;
    }

    @Override
    public Effect getEffect() {
        if (effect == null) {
            effect = buildEffect();
        }
        return effect;
    }

    @Override
    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public List<ClassTrait> getTraits() {
        return getClazz().getTraits(getLevel());
    }

    public Clazz getClazz() {
        return clazz;
    }

    public int getLevel() {
        return level;
    }

    public void setClazz(Clazz clazz) {
        this.clazz = clazz;
        this.effect = null;
    }

    public void setLevel(int level) {
        this.level = level;
        this.effect = null;
    }

    @Override
    public String toString() {
        return clazz.getName() + " " + level;
    }
}
