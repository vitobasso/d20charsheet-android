package com.vituel.dndplayer.activity.edit_char;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.abstraction.PagerFragment;
import com.vituel.dndplayer.activity.select.SelectClassActivity;
import com.vituel.dndplayer.activity.select.SelectRaceActivity;
import com.vituel.dndplayer.model.ClassLevel;
import com.vituel.dndplayer.model.Clazz;
import com.vituel.dndplayer.model.Race;
import com.vituel.dndplayer.model.character.CharBase;
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
import static com.vituel.dndplayer.util.ActivityUtil.validateTextInt;

/**
 * Created by Victor on 28/02/14.
 */
public class EditCharBasicFragment extends PagerFragment<CharBase, EditCharActivity> {

    private Race race;
    private List<ClassLevel> classes;
    private int lastClassClicked;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.edit_char_basic;
    }

    @Override
    protected void onPopulate() {
        populateTextView(root, R.id.name, data.getName());

        race = data.getRace();
        populateRace(race);

        classes = new ArrayList<>(data.getClassLevels());
        if(classes.isEmpty()){
            classes.add(new ClassLevel());
        }
        populateClasses(classes);

        populateTextView(root, R.id.hp, data.getHitPoints());
        populateTextView(root, R.id.str, data.getStrength());
        populateTextView(root, R.id.dex, data.getDexterity());
        populateTextView(root, R.id.con, data.getConstitution());
        populateTextView(root, R.id.attr_int, data.getIntelligence());
        populateTextView(root, R.id.wis, data.getWisdom());
        populateTextView(root, R.id.cha, data.getCharisma());
    }

    @Override
    public boolean onValidate(){
        boolean allValid = validateText(root, R.id.name);
        allValid &= validateText(root, R.id.race);

        ViewGroup classesRoot = findView(R.id.classList);
        for (int i = 0; i < classes.size(); i++) {
            ViewGroup group = (ViewGroup) classesRoot.getChildAt(i);
            allValid &= validateText(group, R.id.classField);
            allValid &= validateTextInt(group, R.id.level, 1, 50);
        }

        allValid &= validateTextInt(root, R.id.hp, 1, 50);
        allValid &= validateTextInt(root, R.id.str, 1, 50);
        allValid &= validateTextInt(root, R.id.dex, 1, 50);
        allValid &= validateTextInt(root, R.id.con, 1, 50);
        allValid &= validateTextInt(root, R.id.attr_int, 1, 50);
        allValid &= validateTextInt(root, R.id.wis, 1, 50);
        allValid &= validateTextInt(root, R.id.cha, 1, 50);
        return allValid;
    }

    @Override
    public void onSave() {
        data.setName(readString(root, R.id.name));

        data.setRace(race);

        //update levels (classes are updated at the time of user action)
        ViewGroup classesRoot = findView(R.id.classList);
        for (int i = 0; i < classes.size(); i++) {
            ViewGroup group = (ViewGroup) classesRoot.getChildAt(i);
            int lvl = readInt(group, R.id.level);
            classes.get(i).setLevel(lvl);
        }
        data.setClassLevels(classes);

        data.setHitPoints(readInt(root, R.id.hp));
        data.setStrength(readInt(root, R.id.str));
        data.setDexterity(readInt(root, R.id.dex));
        data.setConstitution(readInt(root, R.id.con));
        data.setIntelligence(readInt(root, R.id.attr_int));
        data.setWisdom(readInt(root, R.id.wis));
        data.setCharisma(readInt(root, R.id.cha));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.edit_char_basic, menu);

        ViewGroup classesRoot = findView(R.id.classList);
        int classRows = classesRoot.getChildCount();
        if (classRows < 2) {
            MenuItem remove = menu.findItem(R.id.action_remove);
            remove.setEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ViewGroup classesRoot = findView(R.id.classList);
        int classRows = classesRoot.getChildCount();
        switch (item.getItemId()) {
            case R.id.action_add:
                inflateClassRow(classesRoot, classRows);
                classes.add(new ClassLevel());
                activity.invalidateOptionsMenu();
                return true;
            case R.id.action_remove:
                int lastRow = classRows - 1;
                classesRoot.removeViewAt(lastRow);
                classes.remove(lastRow);
                activity.invalidateOptionsMenu();
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
        classesRoot.removeAllViews();
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

    //TODO move to PageFragment
    private <T extends View> T findView(int... ids) {
        return ActivityUtil.findView(root, ids);
    }

}
