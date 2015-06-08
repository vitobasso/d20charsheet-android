package com.vitobasso.d20charsheet.activity.edit_char;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListAdapter;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.activity.abstraction.AbstractListFragment;
import com.vitobasso.d20charsheet.activity.select.SelectFeatActivity;
import com.vitobasso.d20charsheet.model.Feat;
import com.vitobasso.d20charsheet.model.character.CharBase;
import com.vitobasso.d20charsheet.util.gui.EffectArrayAdapter;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.EXTRA_SELECTED;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.REQUEST_SELECT;

/**
 * Created by Victor on 21/03/14.
 */
public class EditCharFeatsFragment extends AbstractListFragment<CharBase, EditCharActivity, Feat> {

    @Override
    protected List<Feat> getListData() {
        return data.getFeats();
    }

    @Override
    protected ListAdapter createAdapter() {
        return new EffectArrayAdapter<>(activity, getListData());
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
                onSave();
                Intent intent = new Intent(activity, SelectFeatActivity.class);
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
                        Feat selected = (Feat) data.getSerializableExtra(EXTRA_SELECTED);

                        //update list
                        listData.add(selected);

                        refresh();
                }
        }
    }
}
