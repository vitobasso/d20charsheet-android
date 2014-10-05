package com.vituel.dndplayer.activity.summary;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.abstraction.PagerFragment;
import com.vituel.dndplayer.activity.edit_char.EditCharActivity;
import com.vituel.dndplayer.activity.edit_char.EditCharPagerAdapter;
import com.vituel.dndplayer.model.CharSkill;
import com.vituel.dndplayer.model.CharSummary;
import com.vituel.dndplayer.util.AppCommons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.vituel.dndplayer.model.ModifierTarget.SKILL;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_EDITED;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_PAGE;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_EDIT;
import static com.vituel.dndplayer.util.ActivityUtil.populateTextView;
import static com.vituel.dndplayer.util.font.FontUtil.MAIN_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.setFontRecursively;

/**
 * Created by Victor on 21/03/14.
 */
public class SummarySkillsFragment extends PagerFragment<CharSummary, SummaryActivity> {

    private List<CharSkill> skills;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.list;
    }

    @Override
    protected void onPopulate() {
        this.skills = new ArrayList<>(data.getSkills().values());
        Collections.sort(skills);
        ((ListView) root).setAdapter(new Adapter(skills));
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
                intent.putExtra(EXTRA_EDITED, data.getBase());
                intent.putExtra(EXTRA_PAGE, EditCharPagerAdapter.PAGE_SKILLS);
                activity.startActivityForResult(intent, REQUEST_EDIT);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            populateTextView(group, R.id.skill, skillName);

            TextView bonusView = populateTextView(group, R.id.bonus, skill.getScore());
            int color = new AppCommons(activity).getValueColor(data.getBase(), SKILL, skillName);
            bonusView.setTextColor(color);

            group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new BreakdownDialog(activity, data).buildDialog(SKILL, skillName).show();
                }
            });

            setFontRecursively(activity, group, MAIN_FONT); //TODO bring to supperclass or to setText
            return group;
        }
    }

}
