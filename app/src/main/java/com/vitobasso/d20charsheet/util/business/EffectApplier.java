package com.vitobasso.d20charsheet.util.business;

import android.content.Context;
import android.util.Log;

import com.vitobasso.d20charsheet.dao.entity.SkillDao;
import com.vitobasso.d20charsheet.model.DiceRoll;
import com.vitobasso.d20charsheet.model.Size;
import com.vitobasso.d20charsheet.model.Skill;
import com.vitobasso.d20charsheet.model.character.AttackRound;
import com.vitobasso.d20charsheet.model.character.CharBase;
import com.vitobasso.d20charsheet.model.character.CharSkill;
import com.vitobasso.d20charsheet.model.character.CharSummary;
import com.vitobasso.d20charsheet.model.effect.Condition;
import com.vitobasso.d20charsheet.model.effect.Effect;
import com.vitobasso.d20charsheet.model.effect.EffectSource;
import com.vitobasso.d20charsheet.model.effect.Modifier;
import com.vitobasso.d20charsheet.model.effect.ModifierTarget;
import com.vitobasso.d20charsheet.util.business.exception.EffectApplicationException;

import java.util.Collection;
import java.util.Set;

/**
 * Created by Victor on 27/06/2015.
 */
public class EffectApplier {

    private Context context;
    private CharSummary summary;
    private CharBase base;

    public EffectApplier(Context context, CharSummary summary) {
        this.context = context;
        this.summary = summary;
        this.base = summary.getBase();
    }

    public void apply(Collection<? extends EffectSource> effectSources) {
        if (effectSources != null) {
            for (EffectSource source : effectSources) {
                apply(source.getEffect());
            }
        }
    }

    public void apply(EffectSource effectSource) {
        apply(effectSource.getEffect());
    }

    public void applyModifiers(Collection<Modifier> modifiers) {
        if (modifiers != null) {
            for (Modifier modifier : modifiers) {
                tryApply(modifier);
            }
        }
    }

    private void apply(Effect effect) {
        if (effect != null) {
            for (Modifier modifier : effect.getModifiers()) {
                tryApply(modifier);
            }
        }
    }

    private void tryApply(Modifier modifier) {
        try {
            if (checkCondition(modifier)) {
                apply(modifier);
            }
        } catch (EffectApplicationException e) {
            Log.w(getClass().getSimpleName(), "Could not apply modifier. Amount isn't fixed. " + modifier.getTarget());
        }
    }

    private void apply(Modifier modifier) {
        ModifierTarget target = modifier.getTarget();
        int fixedAmount = getFixedAmount(modifier);
        //TODO split: skill, attack, others
        switch (target) {
            case HP:
                summary.setHitPoints(summary.getHitPoints() + fixedAmount);
                break;
            case STR:
                summary.setStrength(summary.getStrength() + fixedAmount);
                break;
            case DEX:
                summary.setDexterity(summary.getDexterity() + fixedAmount);
                break;
            case CON:
                summary.setConstitution(summary.getConstitution() + fixedAmount);
                break;
            case INT:
                summary.setIntelligence(summary.getIntelligence() + fixedAmount);
                break;
            case WIS:
                summary.setWisdom(summary.getWisdom() + fixedAmount);
                break;
            case CHA:
                summary.setCharisma(summary.getCharisma() + fixedAmount);
                break;
            case AC:
                summary.setArmorClass(summary.getArmorClass() + fixedAmount);
                break;
            case FORT:
                summary.setFortitude(summary.getFortitude() + fixedAmount);
                break;
            case REFL:
                summary.setReflex(summary.getReflex() + fixedAmount);
                break;
            case WILL:
                summary.setWill(summary.getWill() + fixedAmount);
                break;
            case SPEED:
                summary.setSpeed(summary.getSpeed() + fixedAmount);
                break;
            case INIT:
                summary.setInitiative(summary.getInitiative() + fixedAmount);
                break;
            case SIZE:
                summary.setSize(Size.fromIndex(summary.getSize().getIndex() + fixedAmount));
                break;
            case MAX_DEX:
                summary.setMaxDexMod(Math.min(summary.getMaxDexMod(), fixedAmount));
                break;
            case MR:
                summary.setMagicResistance(summary.getMagicResistance() + fixedAmount);
                break;
            case DR:
                summary.setDamageReduction(summary.getDamageReduction() + fixedAmount);
                break;
            case CONCEAL:
                summary.setConcealment(Math.max(summary.getConcealment(), fixedAmount));
                break;
            case SAVES:
                applySaves(fixedAmount);
                break;
            case SKILL:
                String variation = modifier.getVariation();
                applySkill(variation, fixedAmount);
                break;
            case HIT:
            case DAMAGE:
            case CRIT_RANGE:
            case CRIT_MULT:
                for (AttackRound atk : summary.getAttacks()) {
                    atk.applyModifier(modifier);
                }
                break;
            default:
                Log.w(getClass().getSimpleName(), "Couldn't set target for effect. Target: " + target);
        }

    }

    private int getFixedAmount(Modifier modifier) {
        DiceRoll amount = modifier.getAmount();
        if (!amount.isFixed()) {
            throw new EffectApplicationException("Can't apply modifier with dice");
        }
        return amount.toInt();
    }

    private void applySaves(int fixedAmount) {
        summary.setFortitude(summary.getFortitude() + fixedAmount);
        summary.setReflex(summary.getReflex() + fixedAmount);
        summary.setWill(summary.getWill() + fixedAmount);
    }

    private void applySkill(String skillName, int fixedAmount) {
        CharSkill skillBonus = summary.getSkills().get(skillName);
        if (skillBonus == null) {
            //find skill in db
            skillBonus = addSkill(skillName);
        }
        if (skillBonus != null) { // can be null if skill is not in DB
            skillBonus.addScore(fixedAmount);
        }
    }

    private CharSkill addSkill(String skillName) {
        Skill skill = findSkill(skillName);
        if (skill == null) {
            String msg = String.format("Couldn't find skill by name: '%s'", skillName);
            Log.w(getClass().getSimpleName(), msg);
            return null;
        } else {
            CharSkill skillBonus = new CharSkill(skill);
            summary.getSkills().put(skillName, skillBonus);
            return skillBonus;
        }
    }

    private Skill findSkill(String skillName) {
        SkillDao skillDao = new SkillDao(context);
        try {
            return skillDao.findByName(skillName);
        } finally {
            skillDao.close();
        }
    }

    private boolean checkCondition(Modifier modifier) {
        Condition condition = modifier.getCondition();
        if (condition != null) {
            summary.addReferencedCondition(condition);
            if (!isConditionEnabled(condition)) {
                return false;
            }
        }
        return true;
    }

    private boolean isConditionEnabled(Condition condition) {
        Set<Condition> activeConditions = base.getActiveConditions();
        for (Condition activeCondition : activeConditions) {
            if (isConditionInHierarchy(condition, activeCondition)) {
                return true;
            }
        }
        return false;
    }

    private boolean isConditionInHierarchy(Condition conditionBeingChecked, Condition activeCondition) {
        while (activeCondition != null) {
            if (activeCondition.equals(conditionBeingChecked)) {
                return true;
            }
            activeCondition = activeCondition.getParent();
        }
        return false;
    }

}
