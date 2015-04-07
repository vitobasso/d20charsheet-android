package com.vituel.dndplayer.activity.edit_char;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.EditAbilityModActivity;
import com.vituel.dndplayer.activity.abstraction.PagerFragment;
import com.vituel.dndplayer.dao.AbilityModifierDao;
import com.vituel.dndplayer.model.AbilityModifier;
import com.vituel.dndplayer.model.CharBase;
import com.vituel.dndplayer.util.ActivityUtil;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_EDITED;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_SELECTED;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_CREATE;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_EDIT;
import static com.vituel.dndplayer.util.ActivityUtil.populateTextView;
import static com.vituel.dndplayer.util.font.FontUtil.MAIN_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.setFontRecursively;

/**
 * Created by Victor on 21/03/14.
 */
public class EditCharAbilityModsFragment extends PagerFragment<CharBase, EditCharActivity> {

    private static final String KEY_SELECTED_INDEX = "INDEX";

    private List<AbilityModifier> modifiers;
    private int selectedIndex = -1;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.list;
    }

    @Override
    protected void onPopulate() {
        this.modifiers = data.getAbilityMods();
        ListView listView = (ListView) root;
        listView.setAdapter(new Adapter(modifiers));
        listView.setOnItemClickListener(new ClickListener());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            selectedIndex = savedInstanceState.getInt(KEY_SELECTED_INDEX, -1);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(activity, EditAbilityModActivity.class);
                startActivityForResult(intent, REQUEST_CREATE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (resultCode) {
            case RESULT_OK:

                AbilityModifier edited = (AbilityModifier) intent.getSerializableExtra(EXTRA_EDITED);
                switch (requestCode) {
                    case REQUEST_CREATE:
                        modifiers.add(edited);
                        break;
                    case REQUEST_EDIT:
                        assert selectedIndex >= 0;
                        modifiers.set(selectedIndex, edited);
                        break;
                }

                //save
                AbilityModifierDao dao = new AbilityModifierDao(activity);
                dao.save(edited, data.getId());
                dao.close();

                //update activity and ui
                update();
        }
    }

    private class Adapter extends ArrayAdapter<AbilityModifier> {

        public Adapter(List<AbilityModifier> objects) {
            super(activity, R.layout.edit_ability_mod_list_row, R.id.source, objects);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            AbilityModifier abilityMod = modifiers.get(position);
            populateTextView(view, R.id.target, abilityMod.getTargetLabel(activity));
            populateTextView(view, R.id.source, abilityMod.getSourceLabel(activity));

            setFontRecursively(activity, view, MAIN_FONT);
            return view;
        }
    }

    private class ClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectedIndex = position;

            Intent intent = new Intent(activity, EditAbilityModActivity.class);
            intent.putExtra(EXTRA_SELECTED, modifiers.get(position));
            startActivityForResult(intent, ActivityUtil.REQUEST_EDIT);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SELECTED_INDEX, selectedIndex);
    }
}
