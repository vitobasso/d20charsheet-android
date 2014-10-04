package com.vituel.dndplayer.activity.edit_char;

import android.content.Intent;
import android.os.Bundle;
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
import com.vituel.dndplayer.util.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_SELECTED;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_SELECT;
import static com.vituel.dndplayer.util.ActivityUtil.findView;
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
        setHasOptionsMenu(true);
    }

    @Override
    public void onUpdate() {
        this.skills = data.getSkills();
        refreshUI();
    }

    private void refreshUI() {
        List<CharSkill> list = new ArrayList<>(skills);
        ((ListView) root).setAdapter(new Adapter(list));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity.updateFragment(this); //TODO remove?
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
                onSaveToModel();
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

                        refreshUI();
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

            TextView nameView = findView(v, R.id.name);
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

                    refreshUI();
                }
            });

            setFontRecursively(activity, v, MAIN_FONT);
            return v;
        }
    }

    @Override
    public void onSaveToModel() {

        for (int i = 0; i < root.getChildCount(); i++) {
            ViewGroup group = (ViewGroup) root.getChildAt(i);
            EditText gradView = ActivityUtil.findView(group, R.id.value);
            String gradStr = gradView.getText().toString().trim();
            if(!gradStr.isEmpty()) {
                int grad = Integer.valueOf(gradStr);
                skills.get(i).setScore(grad);
            }
        }

        data.setSkills(skills);
    }
}
