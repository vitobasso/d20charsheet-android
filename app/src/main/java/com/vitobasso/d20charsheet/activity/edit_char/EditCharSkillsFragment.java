package com.vitobasso.d20charsheet.activity.edit_char;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.activity.abstraction.AbstractSimpleListFragment;
import com.vitobasso.d20charsheet.activity.select.SelectSkillActivity;
import com.vitobasso.d20charsheet.model.Skill;
import com.vitobasso.d20charsheet.model.character.CharBase;
import com.vitobasso.d20charsheet.model.character.CharSkill;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.EXTRA_SELECTED;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.REQUEST_SELECT;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.populateTextView;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.readInt;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.validateText;

/**
 * Created by Victor on 21/03/14.
 */
public class EditCharSkillsFragment extends AbstractSimpleListFragment<CharBase, EditCharActivity, CharSkill> {


    @Override
    protected List<CharSkill> getListData() {
        return data.getSkills();
    }

    @Override
    protected int getRowLayoutResourceId() {
        return R.layout.edit_skill_row;
    }

    @Override
    protected void onPopulateRow(View view, CharSkill charSkill) {
        populateTextView(view, R.id.name, charSkill.getSkill().getName());
        populateTextView(view, R.id.value, charSkill.getScore());
    }

    @Override
    public boolean onValidate() {
        boolean allValid = true;
        for (int i = 0; i < root.getChildCount(); i++) {
            ViewGroup group = (ViewGroup) root.getChildAt(i);
            allValid &= validateText(group, R.id.value);
        }
        return allValid;
    }

    @Override
    public void onSave() {
        for (int i = 0; i < root.getChildCount(); i++) {
            ViewGroup group = (ViewGroup) root.getChildAt(i);
            int grad = readInt(group, R.id.value);
            listData.get(i).setScore(grad);
        }
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
                onSave();
                Intent intent = new Intent(activity, SelectSkillActivity.class);
                startActivityForResult(intent, REQUEST_SELECT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case REQUEST_SELECT:
                switch (resultCode) {
                    case RESULT_OK:
                        Skill selected = (Skill) intent.getSerializableExtra(EXTRA_SELECTED);

                        //update list
                        CharSkill charSkill = new CharSkill(selected);
                        listData.add(charSkill);

                        //udpate ability modifiers
                        data.createAbilityModsForNewSkills();

                        refresh();
                }
        }
    }

}
