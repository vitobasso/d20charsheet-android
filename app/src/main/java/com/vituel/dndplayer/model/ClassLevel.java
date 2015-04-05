package com.vituel.dndplayer.model;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor on 31/03/14.
 */
public class ClassLevel extends AbstractEntity implements EffectSource {

    private Clazz clazz;
    private int level;

    private Effect effect;

    public ClassLevel() {
        level = 1;
        effect = buildEffect();
    }

    public int getBaseAttack() {
        return getClazz().getBaseAttack(getLevel());
    }

    @Override
    public String getName() {
        if (getClazz() != null) {
            return MessageFormat.format("{0} $lvl {1}", getClazz().getName(), getLevel());
        } else {
            return null;
        }
    }

    private Effect buildEffect() {
        Modifier fort = new Modifier(ModifierTarget.FORT, getClazz().getBaseFortitude(getLevel()));
        Modifier refl = new Modifier(ModifierTarget.REFL, getClazz().getBaseReflex(getLevel()));
        Modifier will = new Modifier(ModifierTarget.WILL, getClazz().getBaseWill(getLevel()));
        Modifier attack = new Modifier(ModifierTarget.HIT, getClazz().getBaseAttack(getLevel()));

        List<Modifier> modifiers = new ArrayList<>();
        modifiers.add(fort);
        modifiers.add(refl);
        modifiers.add(will);
        modifiers.add(attack);

        Effect effect = new Effect();
        effect.setModifiers(modifiers);
        return effect;
    }

    @Override
    public Effect getEffect() {
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
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return clazz.getName() + " " + level;
    }
}
