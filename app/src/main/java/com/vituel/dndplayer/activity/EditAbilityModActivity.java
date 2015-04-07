package com.vituel.dndplayer.activity;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.abstraction.AbstractEditActivity;
import com.vituel.dndplayer.model.AbilityModifier;
import com.vituel.dndplayer.model.ModifierSource;
import com.vituel.dndplayer.model.ModifierTarget;
import com.vituel.dndplayer.model.Multiplier;

import static com.vituel.dndplayer.util.ActivityUtil.populateSpinnerWithEnum;
import static com.vituel.dndplayer.util.ActivityUtil.populateTextView;
import static com.vituel.dndplayer.util.ActivityUtil.readSpinner;
import static com.vituel.dndplayer.util.ActivityUtil.readString;

public class EditAbilityModActivity extends AbstractEditActivity<AbilityModifier> {

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

}
