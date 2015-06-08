package com.vitobasso.d20charsheet.activity.edit_char;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.activity.abstraction.AbstractSimpleListFragment;
import com.vitobasso.d20charsheet.activity.edit.EditAbilityModActivity;
import com.vitobasso.d20charsheet.dao.abstraction.AbilityModifierDao;
import com.vitobasso.d20charsheet.model.character.CharBase;
import com.vitobasso.d20charsheet.model.effect.AbilityModifier;
import com.vitobasso.d20charsheet.util.app.ActivityUtil;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.EXTRA_EDITED;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.EXTRA_SELECTED;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.REQUEST_CREATE;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.REQUEST_EDIT;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.populateTextView;

/**
 * Created by Victor on 21/03/14.
 */
public class EditCharAbilityModsFragment extends AbstractSimpleListFragment<CharBase, EditCharActivity, AbilityModifier> {

    @Override
    protected int getRowLayoutResourceId() {
        return R.layout.edit_ability_mod_list_row;
    }

    @Override
    protected int getRowTextViewResourceId() {
        return R.id.source;
    }

    @Override
    protected List<AbilityModifier> getListData() {
        return data.getAbilityMods();
    }

    @Override
    protected void onPopulateRow(View view, AbilityModifier abilityMod) {
        populateTextView(view, R.id.target, abilityMod.getTargetLabel(activity));
        populateTextView(view, R.id.source, abilityMod.getSourceLabel(activity));
    }

    @Override
    protected void onClickRow(AbilityModifier element) {
        Intent intent = new Intent(activity, EditAbilityModActivity.class);
        intent.putExtra(EXTRA_SELECTED, element);
        startActivityForResult(intent, ActivityUtil.REQUEST_EDIT);
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
                        listData.add(edited);
                        break;
                    case REQUEST_EDIT:
                        assert clickedIndex >= 0;
                        listData.set(clickedIndex, edited);
                        break;
                }

                //save
                AbilityModifierDao dao = new AbilityModifierDao(activity);
                dao.save(data.getId(), edited);
                dao.close();

                refresh();
        }
    }

}
