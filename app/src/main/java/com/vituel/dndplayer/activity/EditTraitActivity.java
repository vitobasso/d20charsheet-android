package com.vituel.dndplayer.activity;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.abstraction.AbstractEditActivity;
import com.vituel.dndplayer.model.DiceRoll;
import com.vituel.dndplayer.model.Modifier;
import com.vituel.dndplayer.model.ModifierTarget;
import com.vituel.dndplayer.model.ModifierType;
import com.vituel.dndplayer.model.Trait;
import com.vituel.dndplayer.util.gui.NoSelSpinnerAdapter;

import java.util.Arrays;
import java.util.List;

import static com.vituel.dndplayer.util.ActivityUtil.findView;
import static com.vituel.dndplayer.util.ActivityUtil.inflate;

/**
 * Created by Victor on 30/03/14.
 */
public class EditTraitActivity extends AbstractEditActivity<Trait> {

    private static final int NUM_EFFECTS = 5;

    @Override
    protected int getLayout() {
        return R.layout.edit_trait;
    }

    @Override
    protected void populate() {

        EditText name = (EditText) findViewById(R.id.name);
        name.setText(entity.getName());

        //modifiers
        SpinnerAdapter targetAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, Arrays.asList(ModifierTarget.values()));
        targetAdapter = new NoSelSpinnerAdapter(this, targetAdapter);
        SpinnerAdapter typeAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, Arrays.asList(ModifierType.values()));
        typeAdapter = new NoSelSpinnerAdapter(this, typeAdapter);

        ViewGroup effectsRoot = findView(this, R.id.effectsList);
        List<Modifier> modifiers = entity.getModifiers();
        for (Modifier modifier : modifiers) {
            ViewGroup group = inflate(this, effectsRoot, R.layout.edit_modifier);

            Spinner targetSpinner = findView(group, R.id.target);
            targetSpinner.setAdapter(targetAdapter);
            targetSpinner.setSelection(modifier.getTarget().ordinal() + 1); // +1 to compensate for the "no selection" position

            EditText amtField = findView(group, R.id.amount);
            amtField.setText("" + modifier.getAmount());

            Spinner typeSpinner = findView(group, R.id.type);
            typeSpinner.setAdapter(typeAdapter);
            typeSpinner.setSelection(modifier.getType().ordinal() + 1); // +1 to compensate for the "no selection" position
        }

        //remaining empty modifiers
        for (int i = modifiers.size(); i < NUM_EFFECTS; i++) {
            ViewGroup group = inflate(this, effectsRoot, R.layout.edit_modifier);

            Spinner targetSpinner = findView(group, R.id.target);
            targetSpinner.setAdapter(targetAdapter);

            Spinner typeSpinner = findView(group, R.id.type);
            typeSpinner.setAdapter(typeAdapter);
        }

    }

    @Override
    protected Trait save() {

        //basic fields
        EditText nameView = (EditText) findViewById(R.id.name);
        entity.setName(nameView.getText().toString().trim());

        //modifiers
        entity.getModifiers().clear();
        ViewGroup effectsRoot = findView(this, R.id.effectsList);
        for (int i = 0; i < effectsRoot.getChildCount(); i++) {
            ViewGroup group = (ViewGroup) effectsRoot.getChildAt(i);
            Spinner targetSpinner = findView(group, R.id.target);
            TextView amountValue = findView(group, R.id.amount);
            Spinner typeSpinner = findView(group, R.id.type);

            ModifierTarget target = (ModifierTarget) targetSpinner.getSelectedItem();
            DiceRoll amount = amountValue.getText().length() > 0 ? new DiceRoll(amountValue.getText().toString()) : null;
            ModifierType type = (ModifierType) typeSpinner.getSelectedItem();

            if (target != null && amount != null) {
                Modifier modifier = new Modifier(target, amount, type, entity);
                entity.getModifiers().add(modifier);
            }
        }

        return entity;
    }

}
