package com.vitobasso.d20charsheet.activity.edit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.activity.abstraction.AbstractEditActivity;
import com.vitobasso.d20charsheet.model.effect.AbilityModifier;
import com.vitobasso.d20charsheet.model.effect.ModifierSource;
import com.vitobasso.d20charsheet.model.effect.ModifierTarget;
import com.vitobasso.d20charsheet.model.effect.Multiplier;

import static com.vitobasso.d20charsheet.util.app.ActivityUtil.findView;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.populateSpinnerWithEnum;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.populateTextView;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.readSpinner;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.readString;

public class EditAbilityModActivity extends AbstractEditActivity<AbilityModifier> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Spinner targetSpinner = findView(this, R.id.target);
        targetSpinner.setOnItemSelectedListener(new SelectTargetListener());
    }

    @Override
    protected int getLayout() {
        return R.layout.edit_ability_mod;
    }

    @Override
    protected void populate() {

        if (entity.getTarget() != null) {
            populateSpinnerWithEnum(this, R.id.target, ModifierTarget.values(), entity.getTarget());
        }

        if (entity.getMultiplier() != null) {
            populateSpinnerWithEnum(this, R.id.multiplier, Multiplier.values(), entity.getMultiplier());
        }

        if (entity.getAbility() != null) {
            populateSpinnerWithEnum(this, R.id.source, ModifierSource.values(), entity.getAbility());
        }

        if (entity.getVariation() != null) {
            populateTextView(this, R.id.target_variation, entity.getVariation());
        }

    }

    @Override
    protected AbilityModifier save() {

        ModifierTarget target = readSpinner(this, R.id.target);
        entity.setTarget(target);

        Multiplier multiplier = readSpinner(this, R.id.multiplier);
        entity.setMultiplier(multiplier);

        ModifierSource source = readSpinner(this, R.id.source);
        entity.setAbility(source);

        String variation = readString(this, R.id.target_variation);
        if (variation != null && !variation.isEmpty()) {
            entity.setVariation(variation);
        }

        return entity;
    }

    private class SelectTargetListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Activity activity = EditAbilityModActivity.this;
            ModifierTarget target = readSpinner(activity, R.id.target);
            int visibility = target == ModifierTarget.SKILL ? View.VISIBLE : View.GONE;
            findView(activity, R.id.target_variation).setVisibility(visibility);
            findView(activity, R.id.nameLabel).setVisibility(visibility);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    }

}
