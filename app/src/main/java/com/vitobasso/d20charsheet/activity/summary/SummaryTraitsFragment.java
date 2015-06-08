package com.vitobasso.d20charsheet.activity.summary;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.activity.abstraction.PagerFragment;
import com.vitobasso.d20charsheet.model.ClassLevel;
import com.vitobasso.d20charsheet.model.ClassTrait;
import com.vitobasso.d20charsheet.model.Feat;
import com.vitobasso.d20charsheet.model.Race;
import com.vitobasso.d20charsheet.model.RaceTrait;
import com.vitobasso.d20charsheet.model.character.CharSummary;
import com.vitobasso.d20charsheet.model.effect.EffectSource;
import com.vitobasso.d20charsheet.util.gui.EffectPopulator;
import com.vitobasso.d20charsheet.util.gui.SimpleExpListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static com.vitobasso.d20charsheet.util.app.ActivityUtil.populateTextView;

/**
 * Created by Victor on 21/03/14.
 */
public class SummaryTraitsFragment extends PagerFragment<CharSummary, SummaryActivity> {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.explist;
    }

    @Override
    protected void onPopulate() {
        Adapter adapter = new Adapter(organizeTraits(data));
        ((ExpandableListView) root).setAdapter(adapter);
    }

    private TreeMap<String, List<EffectSource>> organizeTraits(CharSummary charSummary) {
        TreeMap<String, List<EffectSource>> traitsMap = new TreeMap<>();

        List<Feat> feats = charSummary.getBase().getFeats();
        traitsMap.put(activity.getResources().getString(R.string.feats), new ArrayList<EffectSource>(feats));

        Race race = charSummary.getBase().getRace();
        List<RaceTrait> raceTraits = race.getTraits();
        traitsMap.put(race.getName(), new ArrayList<EffectSource>(raceTraits));

        for (ClassLevel classLevel : charSummary.getBase().getClassLevels()) {
            List<ClassTrait> classTraits = classLevel.getTraits();
            traitsMap.put(classLevel.getName(), new ArrayList<EffectSource>(classTraits));
        }

        return traitsMap;
    }

    private class Adapter extends SimpleExpListAdapter<String, EffectSource> {

        public Adapter(TreeMap<String, List<EffectSource>> data) {
            super(SummaryTraitsFragment.this.activity, data, R.layout.explist_group, R.layout.effect_row);
        }

        @Override
        protected void populateGroup(String group, boolean isExpanded, View convertView) {
            populateTextView(convertView, R.id.text, group);
        }

        @Override
        protected void populateChild(EffectSource effectSource, View convertView) {
            EffectPopulator populator = new EffectPopulator(activity);
            populator.populate(effectSource, (ViewGroup)convertView);
        }
    }

}
