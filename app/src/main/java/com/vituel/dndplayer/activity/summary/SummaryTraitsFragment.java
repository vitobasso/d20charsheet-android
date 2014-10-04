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
import com.vituel.dndplayer.model.Character;
import com.vituel.dndplayer.model.ClassLevel;
import com.vituel.dndplayer.model.Race;
import com.vituel.dndplayer.model.Trait;
import com.vituel.dndplayer.util.gui.SingleColExpListAdapter;

import java.util.List;
import java.util.TreeMap;

import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_EDITED;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_PAGE;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_EDIT;
import static com.vituel.dndplayer.util.ActivityUtil.populateTextView;

/**
 * Created by Victor on 21/03/14.
 */
public class SummaryTraitsFragment extends PagerFragment<Character, SummaryActivity> {

    TreeMap<String, List<Trait>> traitGroups;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.expandable_list;
    }

    @Override
    protected void onPopulate() {
        onUpdate();
        refreshUI();
    }

    @Override
    public void onUpdate() {
        traitGroups = organizeTraits(data);
        refreshUI();
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

    private void refreshUI() {
        ((ExpandableListView) root).setAdapter(new Adapter(traitGroups));
    }

    private TreeMap<String, List<Trait>> organizeTraits(Character character) {
        TreeMap<String, List<Trait>> traitsMap = new TreeMap<>();

        List<Trait> feats = character.getBase().getFeats();
        traitsMap.put(activity.getResources().getString(R.string.feats), feats);

        Race race = character.getBase().getRace();
        List<Trait> raceTraits = race.getTraits();
        traitsMap.put(race.getName(), raceTraits);

        for (ClassLevel classLevel : character.getBase().getClassLevels()) {
            List<Trait> classTraits = classLevel.getTraits();
            traitsMap.put(classLevel.getName(), classTraits);
        }

        return traitsMap;
    }

    private class Adapter extends SingleColExpListAdapter<String, Trait> {

        public Adapter(TreeMap<String, List<Trait>> data) {
            super(SummaryTraitsFragment.this.activity, data, R.layout.expandable_list_group, R.layout.effect_row);
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(groupLayout, null);
            }

            populateTextView(convertView, R.id.text, groups.get(groupPosition));

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(childLayout, null);
            }

            String group = groups.get(groupPosition);
            Trait effect = data.get(group).get(childPosition);

            EffectPopulator populator = new EffectPopulator(activity);
            populator.populate(effect, (ViewGroup)convertView);

            return convertView;
        }
    }

}
