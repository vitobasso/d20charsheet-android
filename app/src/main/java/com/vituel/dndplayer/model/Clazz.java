package com.vituel.dndplayer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor on 28/02/14.
 */
public class Clazz extends AbstractEntity {

    private AttackProgression attackProg;
    private ResistProgression reflexProg;
    private ResistProgression fortitudeProg;
    private ResistProgression willProg;
    private List<List<Trait>> traits;

    public AttackProgression getAttackProg() {
        return attackProg;
    }

    public ResistProgression getReflexProg() {
        return reflexProg;
    }

    public ResistProgression getFortitudeProg() {
        return fortitudeProg;
    }

    public ResistProgression getWillProg() {
        return willProg;
    }

    public List<List<Trait>> getTraits() {
        return traits;
    }

    public static enum AttackProgression {
        POOR, AVERAGE, GOOD
    }

    public static enum ResistProgression {
        POOR, GOOD
    }

    public int getBaseAttack(int level) {
        switch (getAttackProg()) {
            case GOOD:
                return level;
            case AVERAGE:
                return level * 3 / 4;
            case POOR:
                return level / 2;
        }
        throw new AssertionError();
    }

    public int getBaseReflex(int level) {
        return getBaseResist(level, getReflexProg());
    }

    public int getBaseFortitude(int level) {
        return getBaseResist(level, getFortitudeProg());
    }

    public int getBaseWill(int level) {
        return getBaseResist(level, getWillProg());
    }

    private int getBaseResist(int level, ResistProgression prog) {
        if (level > 0) {
            switch (prog) {
                case GOOD:
                    return level / 2 + 2;
                case POOR:
                    return (level - 1) / 2;
            }
        } else {
            return 0;
        }
        throw new AssertionError();
    }

    public List<Trait> getTraits(int level) {
        List<Trait> result = new ArrayList<>();
        List<String> overridenList = new ArrayList<>();
        for (int i = level-1; i >= 0; i--) {
            if(getTraits().size() > i) {
                for (Trait trait : getTraits().get(i)) {
                    if (!overridenList.contains(trait.getName())) {
                        result.add(trait);
                    } else {
                        overridenList.remove(trait.getName());
                    }
                    String overriden = ((ClassTrait) trait).getOverridenTraitName();
                    if (overriden != null) {
                        overridenList.add(overriden);
                    }
                }
            }
        }
        return result;
    }


    @Override
    public long getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Clazz) {
            return name.equals(((Clazz) o).name);
        } else {
            return false;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAttackProg(AttackProgression attackProg) {
        this.attackProg = attackProg;
    }

    public void setReflexProg(ResistProgression reflexProg) {
        this.reflexProg = reflexProg;
    }

    public void setFortitudeProg(ResistProgression fortitudeProg) {
        this.fortitudeProg = fortitudeProg;
    }

    public void setWillProg(ResistProgression willProg) {
        this.willProg = willProg;
    }

    public void setTraits(List<List<Trait>> traits) {
        this.traits = traits;
    }
}
