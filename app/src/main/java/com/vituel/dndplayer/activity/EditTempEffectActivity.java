package com.vituel.dndplayer.activity;

import android.view.ViewGroup;
import android.widget.*;
import com.vituel.dnd_character_sheet.R;
import com.vituel.dndplayer.model.Modifier;
import com.vituel.dndplayer.model.ModifierTarget;
import com.vituel.dndplayer.model.ModifierType;
import com.vituel.dndplayer.model.DiceRoll;
import com.vituel.dndplayer.model.TempEffect;
import com.vituel.dndplayer.util.gui.NoSelSpinnerAdapter;

import java.util.Arrays;
import java.util.List;

import static com.vituel.dndplayer.util.ActivityUtil.findView;
import static com.vituel.dndplayer.util.ActivityUtil.inflate;

public class EditTempEffectActivity extends AbstractEditActivity<TempEffect> {

    private static final int NUM_EFFECTS = 5;

    @Override
    protected int getLayout() {
        return R.layout.edit_temp_effect;
    }

    @Override
    protected void populate() {

        //basic fields
        EditText name = (EditText) findViewById(R.id.name);
        name.setText(entity.getName());

        SpinnerAdapter condTypeAdapter = new ArrayAdapter<TempEffect.Type>(
                this, android.R.layout.simple_spinner_item, Arrays.asList(TempEffect.Type.values()));
        condTypeAdapter = new NoSelSpinnerAdapter(this, condTypeAdapter);
        Spinner condTypeSpinner = (Spinner) findViewById(R.id.type);
        condTypeSpinner.setAdapter(condTypeAdapter);
        if(entity.getTempEffectType() != null) {
            condTypeSpinner.setSelection(entity.getTempEffectType().ordinal() + 1); // +1 to compensate for the "no selection" position
        }

        //modifiers
        SpinnerAdapter targetAdapter = new ArrayAdapter<ModifierTarget>(
                this, android.R.layout.simple_spinner_item, Arrays.asList(ModifierTarget.values()));
        targetAdapter = new NoSelSpinnerAdapter(this, targetAdapter);
        SpinnerAdapter modTypeAdapter = new ArrayAdapter<ModifierType>(
                this, android.R.layout.simple_spinner_item, Arrays.asList(ModifierType.values()));
        modTypeAdapter = new NoSelSpinnerAdapter(this, modTypeAdapter);
        ViewGroup effectsRoot = findView(this, R.id.effectsList);
        List<Modifier> modifiers = entity.getModifiers();
        for (Modifier modifier : modifiers) {
            ViewGroup group = inflate(this, effectsRoot, R.layout.edit_modifier);

            Spinner targetSpinner = findView(group, R.id.target);
            targetSpinner.setAdapter(targetAdapter);
            targetSpinner.setSelection(modifier.getTarget().ordinal() + 1); // +1 to compensate for the "no selection" position

            EditText amtField = findView(group, R.id.amount);
            amtField.setText("" + modifier.getAmount());

            Spinner modTypeSpinner = findView(group, R.id.type);
            modTypeSpinner.setAdapter(modTypeAdapter);
            modTypeSpinner.setSelection(modifier.getType().ordinal() + 1); // +1 to compensate for the "no selection" position
        }

        //remaining empty modifiers
        for (int i = modifiers.size(); i < NUM_EFFECTS; i++) {
            ViewGroup group = inflate(this, effectsRoot, R.layout.edit_modifier);

            Spinner targetSpinner = findView(group, R.id.target);
            targetSpinner.setAdapter(targetAdapter);

            Spinner typeSpinner = findView(group, R.id.type);
            typeSpinner.setAdapter(modTypeAdapter);
        }
    }

    @Override
    protected TempEffect save() {

        //basic fields
        EditText nameView = (EditText) findViewById(R.id.name);
        entity.setName(nameView.getText().toString().trim());

        Spinner condTypeSpinner = (Spinner) findViewById(R.id.type);
        entity.setTempEffectType((TempEffect.Type) condTypeSpinner.getSelectedItem());

        //modifiers
        entity.getModifiers().clear();
        ViewGroup effectsRoot = findView(this, R.id.effectsList);
        for (int i = 0; i < effectsRoot.getChildCount(); i++) {
            ViewGroup group = (ViewGroup) effectsRoot.getChildAt(i);
            Spinner targetSpinner = findView(group, R.id.target);
            TextView amountValue = findView(group, R.id.amount);
            Spinner modTypeSpinner = findView(group, R.id.type);

            ModifierTarget target = (ModifierTarget) targetSpinner.getSelectedItem();
            DiceRoll amount = amountValue.getText().length() > 0 ? new DiceRoll(amountValue.getText().toString()) : null;
            ModifierType modType = (ModifierType) modTypeSpinner.getSelectedItem();

            if (target != null && amount != null) {
                Modifier modifier = new Modifier(target, amount, modType, entity);
                entity.getModifiers().add(modifier);
            }
        }

        return entity;
    }

}
