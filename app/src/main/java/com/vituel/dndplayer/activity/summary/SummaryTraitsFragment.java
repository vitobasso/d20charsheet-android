package com.vituel.dndplayer.activity.summary;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.EffectPopulator;
import com.vituel.dndplayer.activity.abstraction.PagerFragment;
import com.vituel.dndplayer.activity.edit_char.EditCharActivity;
import com.vituel.dndplayer.activity.edit_char.EditCharPagerAdapter;
import com.vituel.dndplayer.model.CharSummary;
import com.vituel.dndplayer.model.ClassLevel;
import com.vituel.dndplayer.model.Feat;
import com.vituel.dndplayer.model.Race;
import com.vituel.dndplayer.util.gui.SingleColExpListAdapter;

import java.util.List;
import java.util.TreeMap;

import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_EDITED;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_PAGE;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_EDIT;
import static com.vituel.dndplayer.util.ActivityUtil.populateTextView;
import static com.vituel.dndplayer.util.font.FontUtil.MAIN_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.setFontRecursively;

/**
 * Created by Victor on 21/03/14.
 */
public class SummaryTraitsFragment extends PagerFragment<CharSummary, SummaryActivity> {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.expandable_list;
    }

    @Override
    protected void onPopulate() {
        Adapter adapter = new Adapter(organizeTraits(data));
        ((ExpandableListView) root).setAdapter(adapter);
    }

    private TreeMap<String, List<Feat>> organizeTraits(CharSummary charSummary) {
        TreeMap<String, List<Feat>> traitsMap = new TreeMap<>();

        List<Feat> feats = charSummary.getBase().getFeats();
        traitsMap.put(activity.getResources().getString(R.string.feats), feats);

        Race race = charSummary.getBase().getRace();
        List<Feat> raceTraits = race.getTraits();
        traitsMap.put(race.getName(), raceTraits);

        for (ClassLevel classLevel : charSummary.getBase().getClassLevels()) {
            List<Feat> classTraits = classLevel.getTraits();
            traitsMap.put(classLevel.getName(), classTraits);
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

    private class Adapter extends SingleColExpListAdapter<String, Feat> {

        public Adapter(TreeMap<String, List<Feat>> data) {
            super(SummaryTraitsFragment.this.activity, data, R.layout.expandable_list_group, R.layout.effect_row);
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(groupLayout, null);
            }

            populateTextView(convertView, R.id.text, groups.get(groupPosition));

            setFontRecursively(activity, convertView, MAIN_FONT); //TODO bring to supperclass or to setText
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(childLayout, null);
            }

            String group = groups.get(groupPosition);
            Feat effect = children.get(group).get(childPosition);

            EffectPopulator populator = new EffectPopulator(activity);
            populator.populate(effect, (ViewGroup)convertView);

            setFontRecursively(activity, convertView, MAIN_FONT); //TODO bring to supperclass or to setText
            return convertView;
        }
    }

}
