package com.vituel.dndplayer.activity.edit_char;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.vituel.dnd_character_sheet.R;
import com.vituel.dndplayer.activity.PagerFragment;
import com.vituel.dndplayer.util.ActivityUtil;
import com.vituel.dndplayer.model.CharSkill;
import com.vituel.dndplayer.activity.SelectSkillActivity;
import com.vituel.dndplayer.model.Skill;
import com.vituel.dndplayer.model.CharBase;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.vituel.dndplayer.util.ActivityUtil.*;
import static com.vituel.dndplayer.util.font.FontUtil.MAIN_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.setFontRecursively;

/**
 * Created by Victor on 21/03/14.
 */
public class EditCharSkillsFragment extends PagerFragment<CharBase, EditCharActivity> {

    List<CharSkill> skills;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity.updateFragment(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.list;
    }

    @Override
    protected void onPopulate() {
        setHasOptionsMenu(true);
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

    @Override
    public void update(CharBase base) {
        this.skills = base.getSkills();
        refreshUI();
    }

    private void refreshUI() {
        List<CharSkill> list = new ArrayList<>(skills);
        ((ListView) root).setAdapter(new Adapter(list));
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

        activity.base.setSkills(skills);
    }
}
