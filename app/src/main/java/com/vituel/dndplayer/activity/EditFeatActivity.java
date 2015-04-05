package com.vituel.dndplayer.activity;

import android.view.ViewGroup;
import android.widget.EditText;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.abstraction.AbstractEditActivity;
import com.vituel.dndplayer.model.DiceRoll;
import com.vituel.dndplayer.model.Feat;
import com.vituel.dndplayer.model.Modifier;
import com.vituel.dndplayer.model.ModifierTarget;
import com.vituel.dndplayer.model.ModifierType;

import java.util.List;

import static com.vituel.dndplayer.util.ActivityUtil.findView;
import static com.vituel.dndplayer.util.ActivityUtil.inflate;
import static com.vituel.dndplayer.util.ActivityUtil.populateSpinnerWithEnum;
import static com.vituel.dndplayer.util.ActivityUtil.populateTextView;
import static com.vituel.dndplayer.util.ActivityUtil.readDice;
import static com.vituel.dndplayer.util.ActivityUtil.readSpinner;
import static com.vituel.dndplayer.util.ActivityUtil.readString;

/**
 * Created by Victor on 30/03/14.
 */
public class EditFeatActivity extends AbstractEditActivity<Feat> {

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
        ViewGroup effectsRoot = findView(this, R.id.effectsList);
        List<Modifier> modifiers = entity.getEffect().getModifiers();
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
    protected Feat save() {

        //basic fields
        entity.setName(readString(this, R.id.name));

        //modifiers
        List<Modifier> modifiers = entity.getEffect().getModifiers();
        modifiers.clear();
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

        return entity;
    }

}
