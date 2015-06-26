package com.vitobasso.d20charsheet.activity.summary;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.activity.abstraction.PagerFragment;
import com.vitobasso.d20charsheet.model.character.CharSkill;
import com.vitobasso.d20charsheet.model.character.CharSummary;
import com.vitobasso.d20charsheet.util.app.AppCommons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.SKILL;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.populateTextView;

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

            fontUtil.setFontRecursively(group); //TODO bring to supperclass or to setText
            return group;
        }
    }

}
