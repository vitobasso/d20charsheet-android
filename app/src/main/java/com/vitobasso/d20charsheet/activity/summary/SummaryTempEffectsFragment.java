package com.vitobasso.d20charsheet.activity.summary;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.activity.abstraction.AbstractListFragment;
import com.vitobasso.d20charsheet.activity.select.SelectTempEffectActivity;
import com.vitobasso.d20charsheet.dao.dependant.CharTempEffectDao;
import com.vitobasso.d20charsheet.model.TempEffect;
import com.vitobasso.d20charsheet.model.character.CharSummary;
import com.vitobasso.d20charsheet.model.character.CharTempEffect;
import com.vitobasso.d20charsheet.util.gui.EffectArrayAdapter;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.EXTRA_SELECTED;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.REQUEST_SELECT;

/**
 * Created by Victor on 21/03/14.
 */
public class SummaryTempEffectsFragment extends AbstractListFragment<CharSummary, SummaryActivity, CharTempEffect> {


    @Override
    protected List<CharTempEffect> getListData() {
        return data.getBase().getTempEffects();
    }

    @Override
    protected ListAdapter createAdapter() {
        return new Adapter(getListData());
    }

    public void save() {
        //update db
        CharTempEffectDao dataSource = new CharTempEffectDao(activity);
        dataSource.saveOverwrite(data.getBase().getId(), listData);
        dataSource.close();

        //update activity
        activity.refreshUI();
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
                Intent intent = new Intent(activity, SelectTempEffectActivity.class);
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

                        //update list
                        TempEffect selected = (TempEffect) data.getSerializableExtra(EXTRA_SELECTED);
                        CharTempEffect charTempEffect = new CharTempEffect();
                        charTempEffect.setTempEffect(selected);
                        charTempEffect.setActive(true);
                        listData.add(charTempEffect);

                        save();
                }
        }
    }

    private class Adapter extends EffectArrayAdapter<CharTempEffect> {

        public Adapter(List<CharTempEffect> objects) {
            super(activity, objects);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewGroup group = (ViewGroup) super.getView(position, convertView, parent);

            CharTempEffect effect = listData.get(position);
            View effectView = group.findViewById(R.id.effect);
            effectView.setAlpha(effect.isActive() ? 1 : .3f);

            return group;
        }
    }

    @Override
    protected void onClickRow(CharTempEffect element) {
        element.toggleActive();
        save();
    }
}
