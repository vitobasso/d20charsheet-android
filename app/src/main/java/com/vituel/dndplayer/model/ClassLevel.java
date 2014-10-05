package com.vituel.dndplayer.model;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor on 31/03/14.
 */
public class ClassLevel extends AbstractEffect {

    private Clazz clazz;
    private int level;

    public ClassLevel() {
        super(Type.CLASS);
        level = 1;
    }

    public int getBaseAttack() {
        return getClazz().getBaseAttack(getLevel());
    }

    @Override
    public List<Modifier> getModifiers() {
        Modifier fort = new Modifier(ModifierTarget.FORT, getClazz().getBaseFortitude(getLevel()), null, this);
        Modifier refl = new Modifier(ModifierTarget.REFL, getClazz().getBaseReflex(getLevel()), null, this);
        Modifier will = new Modifier(ModifierTarget.WILL, getClazz().getBaseWill(getLevel()), null, this);
        Modifier attack = new Modifier(ModifierTarget.HIT, getClazz().getBaseAttack(getLevel()), null, this);

        List<Modifier> result = new ArrayList<>();
        result.add(fort);
        result.add(refl);
        result.add(will);
        result.add(attack);
        return result;
    }

    public List<Trait> getTraits() {
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
    public String getName() {
        if (getClazz() != null) {
            return MessageFormat.format("{0} $lvl {1}", getClazz().getName(), getLevel());
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return clazz.getName() + " " + level;
    }
}
