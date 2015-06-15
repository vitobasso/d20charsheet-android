package com.vitobasso.d20charsheet.model;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.vitobasso.d20charsheet.model.rulebook.Rule;
import com.vitobasso.d20charsheet.util.business.OverridingTraitMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor on 28/02/14.
 */
public class Clazz extends Rule {

    private AttackProgression attackProg;
    private ResistProgression reflexProg;
    private ResistProgression fortitudeProg;
    private ResistProgression willProg;
    private List<List<ClassTrait>> traits;

    public Clazz() {
    }

    //used by jackson
    public Clazz(long id) {
        super(id);
    }

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

    public List<List<ClassTrait>> getTraits() {
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

    public List<ClassTrait> getTraits(int topLevel) {
        Iterable<ClassTrait> allTraits = Iterables.concat(getTraits());
        OverridingTraitMatcher matcher = new OverridingTraitMatcher(Lists.newArrayList(allTraits));
        return getNonOverridenTraits(topLevel, matcher);
    }

    private List<ClassTrait> getNonOverridenTraits(int topLevel, OverridingTraitMatcher matcher) {
        List<ClassTrait> result = new ArrayList<>();
        for (int level = topLevel; level > 0; level--) {
            int index = level - 1;
            if (getTraits().size() > index) {
                List<ClassTrait> traitsInLevel = getTraits().get(index);
                addTraitIfNotOverriden(traitsInLevel, result, matcher);
            }
        }
        return result;
    }

    private void addTraitIfNotOverriden(List<ClassTrait> currentLevelTraits, List<ClassTrait> higherLevelTraits, OverridingTraitMatcher matcher) {
        for (ClassTrait trait : currentLevelTraits) {
            if (!matcher.matchesAny(trait, higherLevelTraits)) {
                higherLevelTraits.add(trait);
            }
        }
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

    public void setTraits(List<List<ClassTrait>> traits) {
        this.traits = traits;
    }

}
