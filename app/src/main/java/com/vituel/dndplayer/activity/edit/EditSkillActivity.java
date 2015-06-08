package com.vituel.dndplayer.activity.edit;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.abstraction.AbstractEditActivity;
import com.vituel.dndplayer.model.Skill;
import com.vituel.dndplayer.model.effect.ModifierSource;

import static com.vituel.dndplayer.util.app.ActivityUtil.setSpinnerSelection;

/**
 * Created by Victor on 30/03/14.
 */
public class EditSkillActivity extends AbstractEditActivity<Skill> {

    @Override
    protected int getLayout() {
        return R.layout.edit_skill;
    }

    @Override
    protected void populate() {

        EditText name = (EditText) findViewById(R.id.name);
        name.setText(entity.getName());

        if (entity.getKeyAbility() != null) {
            Spinner keyAbility = (Spinner) findViewById(R.id.keyAbility);
            setSpinnerSelection(keyAbility, entity.getKeyAbility().toString());
        }

        CheckBox armorPenalty = (CheckBox) findViewById(R.id.armorPenalty);
        armorPenalty.setChecked(entity.isArmorPenaltyApplies());

    }

    @Override
    protected Skill save() {

        //basic fields
        EditText nameView = (EditText) findViewById(R.id.name);
        entity.setName(nameView.getText().toString().trim());

        Spinner keyAbilityView = (Spinner) findViewById(R.id.keyAbility);
        String abilityStr = (String) keyAbilityView.getSelectedItem();
        entity.setKeyAbility(ModifierSource.valueOf(abilityStr.toUpperCase()));

        CheckBox armorPenalty = (CheckBox) findViewById(R.id.armorPenalty);
        entity.setArmorPenaltyApplies(armorPenalty.isChecked());

        return entity;
    }

}
