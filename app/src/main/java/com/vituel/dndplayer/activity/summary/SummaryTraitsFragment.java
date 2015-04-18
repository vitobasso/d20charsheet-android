package com.vituel.dndplayer.activity.summary;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.abstraction.PagerFragment;
import com.vituel.dndplayer.activity.edit_char.EditCharActivity;
import com.vituel.dndplayer.activity.edit_char.EditCharPagerAdapter;
import com.vituel.dndplayer.model.CharSummary;
import com.vituel.dndplayer.model.ClassLevel;
import com.vituel.dndplayer.model.ClassTrait;
import com.vituel.dndplayer.model.EffectSource;
import com.vituel.dndplayer.model.Feat;
import com.vituel.dndplayer.model.Race;
import com.vituel.dndplayer.model.RaceTrait;
import com.vituel.dndplayer.util.gui.EffectPopulator;
import com.vituel.dndplayer.util.gui.SimpleExpListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_EDITED;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_PAGE;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_EDIT;
import static com.vituel.dndplayer.util.ActivityUtil.populateTextView;

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
                intent.putExtra(EXTRA_PAGE, EditCharPagerAdapter.PAGE_FEATS);
                activity.startActivityForResult(intent, REQUEST_EDIT);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
