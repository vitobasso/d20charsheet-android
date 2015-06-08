package com.vituel.dndplayer.model.effect;

import com.vituel.dndplayer.model.AbstractEntity;
import com.vituel.dndplayer.model.DiceRoll;
import com.vituel.dndplayer.util.LangUtil;

/**
 * Created by Victor on 25/02/14.
 */
public class Modifier extends AbstractEntity {

    protected ModifierTarget target;
    protected String variation; //specific skills are treated as variations of the target SKILL (because skills are dynamic)
    protected DiceRoll amount;
    protected ModifierType type;
    protected Condition condition;

    public Modifier() {}

    public Modifier(ModifierTarget target, String variation, DiceRoll amount, ModifierType type, Condition condition) {
        this.setTarget(target);
        this.setVariation(variation);
        this.setAmount(amount);
        this.setType(type);
        this.setCondition(condition);
    }

    public Modifier(ModifierTarget target, String variation, int amount, ModifierType type, Condition condition) {
        this(target, variation, new DiceRoll(amount), type, condition);
    }

    public Modifier(ModifierTarget target, String variation, int amount) {
        this(target, variation, new DiceRoll(amount), null, null);
    }

    public Modifier(ModifierTarget target, int amount) {
        this(target, null, new DiceRoll(amount), null, null);
    }

    public Modifier(ModifierTarget target, DiceRoll amount, ModifierType type) {
        this(target, null, amount, type, null);
    }

    public Modifier(ModifierTarget target, int amount, ModifierType type) {
        this(target, null, amount, type, null);
    }

    public Modifier(ModifierTarget target, DiceRoll amount) {
        this(target, null, amount, null, null);
    }

    public boolean isBonus() {
        switch (getTarget()) {
            case MAX_DEX:
                return false;
            default:
                return getAmount().isPositive();
        }
    }

    @Override
    public long getId() {
        return id;
    }

    public ModifierTarget getTarget() {
        return target;
    }

    public DiceRoll getAmount() {
        return amount;
    }

    public ModifierType getType() {
        return type;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setTarget(ModifierTarget target) {
        this.target = target;
    }

    public void setAmount(DiceRoll amount) {
        this.amount = amount;
    }

    public void setType(ModifierType type) {
        this.type = type;
    }

    public String getVariation() {
        return variation;
    }

    public void setVariation(String variation) {
        this.variation = variation;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public int hashCode() {
        return LangUtil.hash(target, variation, amount);
    }

    @Override
    public boolean equals(Object another) {
        return this == another;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(getVariation() == null ? getTarget().toString() : getVariation());
        str.append(" ");
        str.append(getAmount());

        if (getType() != null) {
            str.append(" ");
            str.append(getType());
        }
        if (getCondition() != null) {
            str.append(" ");
            str.append("[");
            str.append(getCondition());
            str.append("]");
        }

        return str.toString();
    }
}
