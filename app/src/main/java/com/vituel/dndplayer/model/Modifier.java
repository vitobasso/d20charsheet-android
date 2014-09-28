package com.vituel.dndplayer.model;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Created by Victor on 25/02/14.
 */
public class Modifier extends AbstractEntity {

    private ModifierTarget target;
    private String variation; //specific skills are treated as variations of the target SKILL (because skills are dynamic)
    private DiceRoll amount;
    private ModifierType type;
    private Condition condition;
    @JsonIgnore
    private AbstractEffect source;

    public Modifier() {}

    public Modifier(ModifierTarget target, String variation, DiceRoll amount, ModifierType type, Condition condition, AbstractEffect source) {
        this.setTarget(target);
        this.setVariation(variation);
        this.setAmount(amount);
        this.setType(type);
        this.setSource(source);
        this.setCondition(condition);
    }

    public Modifier(ModifierTarget target, String variation, int amount, ModifierType type, Condition condition, AbstractEffect source) {
        this(target, variation, new DiceRoll(amount), type, condition, source);
    }

    public Modifier(ModifierTarget target, String variation, int amount) {
        this(target, variation, new DiceRoll(amount), null, null, null);
    }

    public Modifier(ModifierTarget target, int amount) {
        this(target, null, new DiceRoll(amount), null, null, null);
    }

    public Modifier(ModifierTarget target, DiceRoll amount, ModifierType type, AbstractEffect source) {
        this(target, null, amount, type, null, source);
    }

    public Modifier(ModifierTarget target, int amount, ModifierType type, AbstractEffect source) {
        this(target, null, amount, type, null, source);
    }

    public Modifier(ModifierTarget target, DiceRoll amount, AbstractEffect source) {
        this(target, null, amount, null, null, source);
    }

    public Modifier(ModifierTarget target, int amount, AbstractEffect source) {
        this(target, null, amount, null, null, source);
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

    public void setVariation(String variation) {
        this.variation = variation;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public AbstractEffect getSource() {
        return source;
    }

    public void setSource(AbstractEffect source) {
        this.source = source;
    }
}
