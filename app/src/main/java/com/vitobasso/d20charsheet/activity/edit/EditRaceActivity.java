package com.vitobasso.d20charsheet.activity.edit;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.activity.abstraction.AbstractEditActivity;
import com.vitobasso.d20charsheet.model.DiceRoll;
import com.vitobasso.d20charsheet.model.Race;
import com.vitobasso.d20charsheet.model.Size;
import com.vitobasso.d20charsheet.model.effect.Modifier;
import com.vitobasso.d20charsheet.model.effect.ModifierTarget;
import com.vitobasso.d20charsheet.util.app.ActivityUtil;

import java.util.List;

import static com.vitobasso.d20charsheet.model.effect.ModifierType.RACIAL;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.populateSpinnerWithEnum;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.populateTextView;

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

        populateTextView(this, R.id.name, entity.getName());
        populateSpinnerWithEnum(this, R.id.size, Size.values(), Size.fromIndex(0));

        //modifiers
        if (entity.getEffect() != null) {
            List<Modifier> modifiers = entity.getEffect().getModifiers();
            for (Modifier modifier : modifiers) {
                switch (modifier.getTarget()) {
                    case STR:
                        populateTextView(this, R.id.str, modifier);
                        break;
                    case DEX:
                        populateTextView(this, R.id.dex, modifier);
                        break;
                    case CON:
                        populateTextView(this, R.id.con, modifier);
                        break;
                    case INT:
                        populateTextView(this, R.id.attr_int, modifier);
                        break;
                    case WIS:
                        populateTextView(this, R.id.wis, modifier);
                        break;
                    case CHA:
                        populateTextView(this, R.id.cha, modifier);
                        break;
                    case SPEED:
                        populateTextView(this, R.id.speed, modifier);
                        break;
                    case SIZE:
//                        Size value = Size.fromIndex(modifier.getAmount().toInt());
//                        sizeSpinner.setSelection(value.ordinal()); //TODO
                        break;
                }
            }
        }

    }

    @Override
    protected Race save() {

        //basic fields
        EditText nameView = (EditText) findViewById(R.id.name);
        entity.setName(nameView.getText().toString().trim());

        //modifiers
        List<Modifier> modifiers = entity.getEffect().getModifiers();
        modifiers.clear();
        readFromEditText(R.id.str, ModifierTarget.STR);
        readFromEditText(R.id.dex, ModifierTarget.DEX);
        readFromEditText(R.id.con, ModifierTarget.CON);
        readFromEditText(R.id.attr_int, ModifierTarget.INT);
        readFromEditText(R.id.wis, ModifierTarget.WIS);
        readFromEditText(R.id.cha, ModifierTarget.CHA);
        readFromEditText(R.id.speed, ModifierTarget.SPEED);

        Spinner sizeSpinner = findView(R.id.size);
        Size size = (Size) sizeSpinner.getSelectedItem();
        Modifier modifier = new Modifier(ModifierTarget.SIZE, size.getIndex(), RACIAL);
        modifiers.add(modifier);

        return entity;
    }

    private void readFromEditText(int res, ModifierTarget target) {
        EditText view = findView(res);
        DiceRoll amount = view.getText().length() > 0 ? new DiceRoll(view.getText().toString().trim()) : null;

        List<Modifier> modifiers = entity.getEffect().getModifiers();
        if (target != null && amount != null) {
            Modifier modifier = new Modifier(target, amount, RACIAL);
            modifiers.add(modifier);
        }
    }

    private <T extends View> T findView(int... ids) {
        return ActivityUtil.findView(this, ids);
    }
}
