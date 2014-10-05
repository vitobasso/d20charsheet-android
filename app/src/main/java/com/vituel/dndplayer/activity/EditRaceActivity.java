package com.vituel.dndplayer.activity;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.abstraction.AbstractEditActivity;
import com.vituel.dndplayer.model.DiceRoll;
import com.vituel.dndplayer.model.Modifier;
import com.vituel.dndplayer.model.ModifierTarget;
import com.vituel.dndplayer.model.Race;
import com.vituel.dndplayer.model.Size;
import com.vituel.dndplayer.util.ActivityUtil;

import java.util.Arrays;
import java.util.List;

import static com.vituel.dndplayer.model.ModifierType.RACIAL;

/**
 * Created by Victor on 30/03/14.
 */
public class EditRaceActivity extends AbstractEditActivity<Race> {

    @Override
    protected int getLayout() {
        return R.layout.edit_race;
    }

    @Override
    protected void populate() {

        EditText name = (EditText) findViewById(R.id.name);
        name.setText(entity.getName());

        SpinnerAdapter sizeAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, Arrays.asList(Size.values()));
        Spinner sizeSpinner = findView(R.id.size);
        sizeSpinner.setAdapter(sizeAdapter);
        Size initValue = Size.fromIndex(0);
        sizeSpinner.setSelection(initValue.ordinal());

        //modifiers
        List<Modifier> modifiers = entity.getModifiers();
        for (Modifier modifier : modifiers) {
            switch (modifier.getTarget()) {
                case STR:
                    populateEditText(R.id.str, modifier);
                    break;
                case DEX:
                    populateEditText(R.id.dex, modifier);
                    break;
                case CON:
                    populateEditText(R.id.con, modifier);
                    break;
                case INT:
                    populateEditText(R.id.attr_int, modifier);
                    break;
                case WIS:
                    populateEditText(R.id.wis, modifier);
                    break;
                case CHA:
                    populateEditText(R.id.cha, modifier);
                    break;
                case SPEED:
                    populateEditText(R.id.speed, modifier);
                    break;
                case SIZE:
                    Size value = Size.fromIndex(modifier.getAmount().toInt());
                    sizeSpinner.setSelection(value.ordinal());
                    break;
            }
        }

    }

    private void populateEditText(int res, Modifier modifier) {
        EditText view = findView(res);
        view.setText(modifier.getAmount().toString());
    }

    @Override
    protected Race save() {

        //basic fields
        EditText nameView = (EditText) findViewById(R.id.name);
        entity.setName(nameView.getText().toString().trim());

        //modifiers
        entity.getModifiers().clear();
        readFromEditText(R.id.str, ModifierTarget.STR);
        readFromEditText(R.id.dex, ModifierTarget.DEX);
        readFromEditText(R.id.con, ModifierTarget.CON);
        readFromEditText(R.id.attr_int, ModifierTarget.INT);
        readFromEditText(R.id.wis, ModifierTarget.WIS);
        readFromEditText(R.id.cha, ModifierTarget.CHA);
        readFromEditText(R.id.speed, ModifierTarget.SPEED);

        Spinner sizeSpinner = findView(R.id.size);
        Size size = (Size) sizeSpinner.getSelectedItem();
        Modifier modifier = new Modifier(ModifierTarget.SIZE, size.getIndex(), RACIAL, entity);
        entity.getModifiers().add(modifier);

        return entity;
    }

    private void readFromEditText(int res, ModifierTarget target) {
        EditText view = findView(res);
        DiceRoll amount = view.getText().length() > 0 ? new DiceRoll(view.getText().toString().trim()) : null;

        if (target != null && amount != null) {
            Modifier modifier = new Modifier(target, amount, RACIAL, entity);
            entity.getModifiers().add(modifier);
        }
    }

    private <T extends View> T findView(int... ids) {
        return ActivityUtil.findView(this, ids);
    }
}
