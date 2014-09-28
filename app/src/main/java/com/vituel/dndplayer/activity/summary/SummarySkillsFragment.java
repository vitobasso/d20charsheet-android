package com.vituel.dndplayer.activity.summary;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.PagerFragment;
import com.vituel.dndplayer.activity.edit_char.EditCharActivity;
import com.vituel.dndplayer.activity.edit_char.EditCharPagerAdapter;
import com.vituel.dndplayer.model.CharSkill;
import com.vituel.dndplayer.model.Character;
import com.vituel.dndplayer.util.ActivityUtil;
import com.vituel.dndplayer.util.AppCommons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.vituel.dndplayer.model.ModifierTarget.SKILL;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_EDITED;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_PAGE;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_EDIT;
import static com.vituel.dndplayer.util.font.FontUtil.MAIN_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.setFontRecursively;

/**
 * Created by Victor on 21/03/14.
 */
public class SummarySkillsFragment extends PagerFragment<com.vituel.dndplayer.model.Character, SummaryActivity> {

    List<CharSkill> skills;

    @Override
    protected int getLayout() {
        return R.layout.list;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity.findCharacterToOpen(); //must be called in the last fragment?
    }

    @Override
    protected void onPopulate() {
        setHasOptionsMenu(true);

        if(skills != null) {
            refreshUI();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.summary_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:

                //edit opened character
                Intent intent = new Intent(activity, EditCharActivity.class);
                intent.putExtra(EXTRA_EDITED, activity.character.getBase());
                intent.putExtra(EXTRA_PAGE, EditCharPagerAdapter.PAGE_SKILLS);
                activity.startActivityForResult(intent, REQUEST_EDIT);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void update(Character character) {
        this.skills = new ArrayList<>(character.getSkills().values());
        Collections.sort(skills);
        refreshUI();
    }

    private void refreshUI() {
        List<CharSkill> list = new ArrayList<>(skills);
        ((ListView) root).setAdapter(new Adapter(list));
    }

   private class Adapter extends ArrayAdapter<CharSkill> {

        public Adapter(List<CharSkill> objects) {
            super(activity, R.layout.summary_skill_row, R.id.skill, objects);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewGroup group = (ViewGroup) super.getView(position, convertView, parent);
            assert group != null;

            CharSkill skill = skills.get(position);
            final String skillName = skill.getSkill().getName();

            setFontRecursively(activity, group, MAIN_FONT);
            group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new BreakdownDialog(activity, activity.character).buildDialog(SKILL, skillName).show();
                }
            });

            TextView skillView = ActivityUtil.findView(group, R.id.skill);
            skillView.setText(skillName);

            TextView bonusView = ActivityUtil.findView(group, R.id.bonus);
            bonusView.setText("" + skill.getScore());
            int color = new AppCommons(activity).getValueColor(activity.character.getBase(), SKILL, skillName);
            bonusView.setTextColor(color);

            return group;
        }
    }

}
