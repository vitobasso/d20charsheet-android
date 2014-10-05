package com.vituel.dndplayer.activity.edit_char;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.SelectSkillActivity;
import com.vituel.dndplayer.activity.abstraction.PagerFragment;
import com.vituel.dndplayer.model.CharBase;
import com.vituel.dndplayer.model.CharSkill;
import com.vituel.dndplayer.model.Skill;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_SELECTED;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_SELECT;
import static com.vituel.dndplayer.util.ActivityUtil.findView;
import static com.vituel.dndplayer.util.ActivityUtil.readInt;
import static com.vituel.dndplayer.util.ActivityUtil.validateText;
import static com.vituel.dndplayer.util.font.FontUtil.MAIN_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.setFontRecursively;

/**
 * Created by Victor on 21/03/14.
 */
public class EditCharSkillsFragment extends PagerFragment<CharBase, EditCharActivity> {

    private List<CharSkill> skills;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.list;
    }

    @Override
    protected void onPopulate() {
        this.skills = data.getSkills();
        ((ListView) root).setAdapter(new Adapter(skills));
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
            skills.get(i).setScore(grad);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                onSave();
                Intent intent = new Intent(activity, SelectSkillActivity.class);
                startActivityForResult(intent, REQUEST_SELECT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_SELECT:
                switch (resultCode) {
                    case RESULT_OK:
                        Skill selected = (Skill) data.getSerializableExtra(EXTRA_SELECTED);

                        //update list
                        CharSkill charSkill = new CharSkill(selected);
                        skills.add(charSkill);

                        update();
                }
        }
    }

    private class Adapter extends ArrayAdapter<CharSkill> {

        public Adapter(List<CharSkill> objects) {
            super(activity, R.layout.edit_skill_row, R.id.name, objects);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewGroup v = (ViewGroup) super.getView(position, convertView, parent);
            assert v != null;

            CharSkill charSkill = skills.get(position);

            TextView nameView = findView(v, R.id.name); //TODO use ActivityUtil.populateTextView
            nameView.setText(charSkill.getSkill().getName());

            EditText gradView = findView(v, R.id.value);
            gradView.setText("" + charSkill.getScore());

            View removeView = findView(v, R.id.remove);
            removeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //update list
                    CharSkill cond = skills.get(position);
                    skills.remove(cond);

                    update();
                }
            });

            setFontRecursively(activity, v, MAIN_FONT);
            return v;
        }
    }

}
