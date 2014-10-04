package com.vituel.dndplayer.activity.edit_char;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.SelectClassActivity;
import com.vituel.dndplayer.activity.SelectRaceActivity;
import com.vituel.dndplayer.activity.abstraction.PagerFragment;
import com.vituel.dndplayer.model.CharBase;
import com.vituel.dndplayer.model.ClassLevel;
import com.vituel.dndplayer.model.Clazz;
import com.vituel.dndplayer.model.Race;
import com.vituel.dndplayer.util.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_SELECTED;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_SELECT_CLASS;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_SELECT_RACE;
import static com.vituel.dndplayer.util.ActivityUtil.inflate;
import static com.vituel.dndplayer.util.ActivityUtil.populateTextView;
import static com.vituel.dndplayer.util.ActivityUtil.readInt;
import static com.vituel.dndplayer.util.ActivityUtil.readString;
import static com.vituel.dndplayer.util.ActivityUtil.validateText;

/**
 * Created by Victor on 28/02/14.
 */
public class EditCharBasicFragment extends PagerFragment<CharBase, EditCharActivity> {

    Race race;
    List<ClassLevel> classes;

    int lastClassClicked;

    @Override
    protected int getLayout() {
        return R.layout.edit_char_basic;
    }

    @Override
    protected void onPopulate() {
        CharBase base = activity.base;

        populateTextView(root, R.id.name, base.getName());

        race = base.getRace();
        populateRace(race);

        classes = new ArrayList<>(base.getClassLevels());
        if(classes.isEmpty()){
            ClassLevel classLevel = new ClassLevel();
            classLevel.setLevel(1);
            classes.add(classLevel);
        }
        populateClasses(classes);

        populateTextView(root, R.id.hp, base.getHitPoints());
        populateTextView(root, R.id.str, base.getStrength());
        populateTextView(root, R.id.dex, base.getDexterity());
        populateTextView(root, R.id.con, base.getConstitution());
        populateTextView(root, R.id.attr_int, base.getIntelligence());
        populateTextView(root, R.id.wis, base.getWisdom());
        populateTextView(root, R.id.cha, base.getCharisma());

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                ViewGroup classesRoot = findView(R.id.classList);
                inflateClassRow(classesRoot, classesRoot.getChildCount());
                classes.add(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void populateRace(Race race) {
        EditText raceView = findView(R.id.race);
        if (race != null) {
            raceView.setText(race.getName());
        }

        raceView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    selectRace();
                }
            }
        });
        raceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectRace();
            }
        });
    }

    private void selectRace() {
        Intent intent = new Intent(activity, SelectRaceActivity.class);
        startActivityForResult(intent, REQUEST_SELECT_RACE);
    }

    private void populateClasses(List<ClassLevel> classes) {
        ViewGroup classesRoot = findView(R.id.classList);
        for (int i = 0; i == 0 || i < classes.size(); i++) {
            ViewGroup group = inflateClassRow(classesRoot, i);

            if (!classes.isEmpty() && classes.get(i) != null) {
                //populate values
                ClassLevel classLevel = classes.get(i);
                Clazz clazz = classLevel.getClazz();
                int level = classLevel.getLevel();

                populateTextView(group, R.id.classField, clazz);
                populateTextView(group, R.id.level, level);
            }
        }
    }

    private ViewGroup inflateClassRow(ViewGroup parentView, final int position) {
        ViewGroup group = inflate(activity, parentView, R.layout.edit_char_basic_class);

        //setup class field listeners
        EditText classView = ActivityUtil.findView(group, R.id.classField);
        classView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    selectClass(position);
                }
            }
        });
        classView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectClass(position);
            }
        });

        //setup remove button
        View removeBtn = ActivityUtil.findView(group, R.id.remove);
        if (position == 0) {
            removeBtn.setVisibility(View.GONE);
        } else {
            removeBtn.setOnClickListener(new RemoveClickListener(position));
        }

        return group;
    }

    private void selectClass(int position) {
        lastClassClicked = position;

        //call select activity
        Intent intent = new Intent(activity, SelectClassActivity.class);
        startActivityForResult(intent, REQUEST_SELECT_CLASS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SELECT_RACE:
                switch (resultCode) {
                    case RESULT_OK:

                        //get selected race
                        Race selected = (Race) data.getSerializableExtra(EXTRA_SELECTED);

                        //update UI
                        if (selected != null) {
                            populateTextView(root, R.id.race, selected.getName());
                        }

                        //update memory
                        race = selected;

                        break;
                }
                break;

            case REQUEST_SELECT_CLASS:
                switch (resultCode) {
                    case RESULT_OK:

                        //get selected class
                        Clazz selected = (Clazz) data.getSerializableExtra(EXTRA_SELECTED);

                        //update UI
                        if (selected != null) {
                            ViewGroup classesRoot = findView(R.id.classList);
                            ViewGroup group = (ViewGroup) classesRoot.getChildAt(lastClassClicked);
                            populateTextView(group, R.id.classField, selected.getName());
                        }

                        //update memory
                        ClassLevel newClassLvl = new ClassLevel();
                        newClassLvl.setClazz(selected);
                        classes.set(lastClassClicked, newClassLvl);

                        break;
                }
                break;
        }
    }

    @Override
    public void onSaveToModel() {
        CharBase base = activity.base;

        base.setName(readString(root, R.id.name));

        base.setRace(race);

        //update levels (classes are updated at the time of user action)
        ViewGroup classesRoot = findView(R.id.classList);
        for (int i = 0; i < classes.size(); i++) {
            ViewGroup group = (ViewGroup) classesRoot.getChildAt(i);
            int lvl = readInt(group, R.id.level);
            classes.get(i).setLevel(lvl);
        }
        base.setClassLevels(classes);

        base.setHitPoints(readInt(root, R.id.hp));
        base.setStrength(readInt(root, R.id.str));
        base.setDexterity(readInt(root, R.id.dex));
        base.setConstitution(readInt(root, R.id.con));
        base.setIntelligence(readInt(root, R.id.attr_int));
        base.setWisdom(readInt(root, R.id.wis));
        base.setCharisma(readInt(root, R.id.cha));
    }

    @Override
    public boolean onValidate(){
        boolean allValid = validateText(root, R.id.name);
        allValid &= validateText(root, R.id.race);
        allValid &= validateText(root, R.id.classField);
        allValid &= validateText(root, R.id.level);
        allValid &= validateText(root, R.id.hp);
        allValid &= validateText(root, R.id.str);
        allValid &= validateText(root, R.id.dex);
        allValid &= validateText(root, R.id.con);
        allValid &= validateText(root, R.id.attr_int);
        allValid &= validateText(root, R.id.wis);
        allValid &= validateText(root, R.id.cha);
        return allValid;
    }

    private <T extends View> T findView(int... ids) {
        return ActivityUtil.findView(root, ids);
    }


    private class RemoveClickListener implements View.OnClickListener {

        private int position;

        public RemoveClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            ViewGroup classesRoot = findView(R.id.classList);
            classesRoot.removeViewAt(position);
            classes.remove(position);
        }
    }
}
