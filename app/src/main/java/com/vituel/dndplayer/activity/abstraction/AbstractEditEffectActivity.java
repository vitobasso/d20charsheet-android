package com.vituel.dndplayer.activity.abstraction;

import android.view.ViewGroup;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.model.AbstractEntity;
import com.vituel.dndplayer.model.DiceRoll;
import com.vituel.dndplayer.model.effect.Effect;
import com.vituel.dndplayer.model.effect.EffectSource;
import com.vituel.dndplayer.model.effect.Modifier;
import com.vituel.dndplayer.model.effect.ModifierTarget;
import com.vituel.dndplayer.model.effect.ModifierType;

import java.util.List;

import static com.vituel.dndplayer.util.ActivityUtil.findView;
import static com.vituel.dndplayer.util.ActivityUtil.inflate;
import static com.vituel.dndplayer.util.ActivityUtil.populateSpinnerWithEnum;
import static com.vituel.dndplayer.util.ActivityUtil.populateTextView;
import static com.vituel.dndplayer.util.ActivityUtil.readDice;
import static com.vituel.dndplayer.util.ActivityUtil.readSpinner;
import static com.vituel.dndplayer.util.ActivityUtil.readString;
import static com.vituel.dndplayer.util.ActivityUtil.validateText;

/**
 * Created by Victor on 09/04/2015.
 */
public abstract class AbstractEditEffectActivity<T extends AbstractEntity & EffectSource> extends AbstractEditActivity<T> {

    private static final int NUM_EFFECTS = 5;

    @Override
    protected void populate() {

        //name
        populateTextView(this, R.id.name, entity.getName());

        //modifiers
        ViewGroup effectsRoot = findView(this, R.id.effectsList);
        Effect effect = entity.getEffect();
        if(effect != null) {
            for (Modifier modifier : effect.getModifiers()) {
                ViewGroup group = inflate(this, effectsRoot, R.layout.edit_modifier);

                populateSpinnerWithEnum(this, group, R.id.target, ModifierTarget.values(), modifier.getTarget(), null);
                populateTextView(group, R.id.amount, modifier.getAmount());
                populateSpinnerWithEnum(this, group, R.id.type, ModifierType.values(), modifier.getType(), null);
            }
        }

        //remaining empty modifiers
        int numModifiers = effect != null ? effect.getModifiers().size() : 0;
        for (int i = numModifiers; i < NUM_EFFECTS; i++) {
            ViewGroup group = inflate(this, effectsRoot, R.layout.edit_modifier);
            populateSpinnerWithEnum(this, group, R.id.target, ModifierTarget.values(), null, null);
            populateSpinnerWithEnum(this, group, R.id.type, ModifierType.values(), null, null);
        }

    }

    @Override
    protected boolean validate() {
        return validateText(this, R.id.name);
    }

    @Override
    protected T save() {

        //name
        entity.setName(readString(this, R.id.name));

        //modifiers
        Effect effect = new Effect();
        List<Modifier> modifiers = effect.getModifiers();
        ViewGroup effectsRoot = findView(this, R.id.effectsList);
        for (int i = 0; i < effectsRoot.getChildCount(); i++) {
            ViewGroup group = (ViewGroup) effectsRoot.getChildAt(i);
            ModifierTarget target = readSpinner(group, R.id.target);
            DiceRoll amount = readDice(group, R.id.amount);
            ModifierType type = readSpinner(group, R.id.type);

            if (target != null && amount != null) {
                Modifier modifier = new Modifier(target, amount, type);
                modifiers.add(modifier);
            }
        }
        entity.setEffect(effect);

        return entity;
    }
}
