package com.vituel.dndplayer.activity;

import android.view.ViewGroup;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.model.DiceRoll;
import com.vituel.dndplayer.model.Modifier;
import com.vituel.dndplayer.model.ModifierTarget;
import com.vituel.dndplayer.model.ModifierType;
import com.vituel.dndplayer.model.TempEffect;

import java.util.List;

import static com.vituel.dndplayer.util.ActivityUtil.findView;
import static com.vituel.dndplayer.util.ActivityUtil.inflate;
import static com.vituel.dndplayer.util.ActivityUtil.populateSpinnerWithEnum;
import static com.vituel.dndplayer.util.ActivityUtil.populateTextView;
import static com.vituel.dndplayer.util.ActivityUtil.readDice;
import static com.vituel.dndplayer.util.ActivityUtil.readSpinner;
import static com.vituel.dndplayer.util.ActivityUtil.readString;
import static com.vituel.dndplayer.util.ActivityUtil.validateSpinner;
import static com.vituel.dndplayer.util.ActivityUtil.validateText;

public class EditTempEffectActivity extends AbstractEditActivity<TempEffect> {

    private static final int NUM_EFFECTS = 5;

    @Override
    protected int getLayout() {
        return R.layout.edit_temp_effect;
    }

    @Override
    protected void populate() {

        //basic fields
        populateTextView(this, R.id.name, entity.getName());
        populateSpinnerWithEnum(this, null, R.id.type, TempEffect.Type.values(), entity.getTempEffectType(), null);

        //modifiers
        ViewGroup effectsRoot = findView(this, R.id.effectsList);
        List<Modifier> modifiers = entity.getModifiers();
        for (Modifier modifier : modifiers) {
            ViewGroup group = inflate(this, effectsRoot, R.layout.edit_modifier);
            populateSpinnerWithEnum(this, group, R.id.target, ModifierTarget.values(), modifier.getTarget(), null);
            populateTextView(group, R.id.amount, modifier.getAmount());
            populateSpinnerWithEnum(this, group, R.id.type, ModifierType.values(), modifier.getType(), null);
        }

        //remaining empty modifiers
        for (int i = modifiers.size(); i < NUM_EFFECTS; i++) {
            ViewGroup group = inflate(this, effectsRoot, R.layout.edit_modifier);
            populateSpinnerWithEnum(this, group, R.id.target, ModifierTarget.values(), null, null);
            populateSpinnerWithEnum(this, group, R.id.type, ModifierType.values(), null, null);
        }
    }

    @Override
    protected boolean validate() {
        boolean allValid = validateText(this, R.id.name);
        allValid &= validateSpinner(this, R.id.type);
        return allValid;
    }

    @Override
    protected TempEffect save() {

        //basic fields
        entity.setName(readString(this, R.id.name));
        entity.setTempEffectType((TempEffect.Type) readSpinner(this, R.id.type));

        //modifiers
        entity.getModifiers().clear();
        ViewGroup effectsRoot = findView(this, R.id.effectsList);
        for (int i = 0; i < effectsRoot.getChildCount(); i++) {
            ViewGroup group = (ViewGroup) effectsRoot.getChildAt(i);
            ModifierTarget target = readSpinner(group, R.id.target);
            DiceRoll amount = readDice(group, R.id.amount);
            ModifierType modType = readSpinner(group, R.id.type);

            if (target != null && amount != null) {
                Modifier modifier = new Modifier(target, amount, modType, entity);
                entity.getModifiers().add(modifier);
            }
        }

        return entity;
    }

}
