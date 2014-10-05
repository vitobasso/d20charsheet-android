package com.vituel.dndplayer.activity;

import android.widget.EditText;
import android.widget.Spinner;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.abstraction.AbstractEditActivity;
import com.vituel.dndplayer.dao.ClassDao;
import com.vituel.dndplayer.model.Clazz;

import static com.vituel.dndplayer.model.Clazz.AttackProgression;
import static com.vituel.dndplayer.model.Clazz.ResistProgression;

public class EditClassActivity extends AbstractEditActivity<Clazz> {

    @Override
    protected int getLayout() {
        return R.layout.edit_class;
    }

    @Override
    protected void populate() {

        EditText className = (EditText) findViewById(R.id.name);
        className.setText(entity.getName());

        Spinner atkSpinner = (Spinner) findViewById(R.id.atkValue);
        if (entity.getAttackProg() == AttackProgression.GOOD) {
            atkSpinner.setSelection(0);
        } else if (entity.getAttackProg() == AttackProgression.AVERAGE) {
            atkSpinner.setSelection(1);
        } else {
            atkSpinner.setSelection(2);
        }

        Spinner reflexSpinner = (Spinner) findViewById(R.id.reflexValue);
        if (entity.getReflexProg() == ResistProgression.GOOD) {
            reflexSpinner.setSelection(0);
        } else {
            reflexSpinner.setSelection(1);
        }

        Spinner fortitudeSpinner = (Spinner) findViewById(R.id.fortitudeValue);
        if (entity.getFortitudeProg() == ResistProgression.GOOD) {
            fortitudeSpinner.setSelection(0);
        } else {
            fortitudeSpinner.setSelection(1);
        }

        Spinner willSpinner = (Spinner) findViewById(R.id.willValue);
        if (entity.getWillProg() == ResistProgression.GOOD) {
            willSpinner.setSelection(0);
        } else {
            willSpinner.setSelection(1);
        }
    }

    @Override
    protected Clazz save() {

        EditText className = (EditText) findViewById(R.id.name);
        entity.setName(className.getText().toString().trim());

        Spinner atkSpinner = (Spinner) findViewById(R.id.atkValue);
        entity.setAttackProg(AttackProgression.valueOf(atkSpinner.getSelectedItem().toString()));

        Spinner reflexSpinner = (Spinner) findViewById(R.id.reflexValue);
        entity.setReflexProg(ResistProgression.valueOf(reflexSpinner.getSelectedItem().toString()));

        Spinner fortitudeSpinner = (Spinner) findViewById(R.id.fortitudeValue);
        entity.setFortitudeProg(ResistProgression.valueOf(fortitudeSpinner.getSelectedItem().toString()));

        Spinner willSpinner = (Spinner) findViewById(R.id.willValue);
        entity.setWillProg(ResistProgression.valueOf(willSpinner.getSelectedItem().toString()));

        ClassDao dataSource = new ClassDao(this);
        dataSource.save(entity);
        dataSource.close();

        return entity;
    }


}
