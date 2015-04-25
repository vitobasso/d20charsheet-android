package com.vituel.dndplayer.activity.summary;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.abstraction.AbstractListFragment;
import com.vituel.dndplayer.activity.select.SelectTempEffectActivity;
import com.vituel.dndplayer.dao.CharTempEffectDao;
import com.vituel.dndplayer.model.TempEffect;
import com.vituel.dndplayer.model.character.CharSummary;
import com.vituel.dndplayer.model.character.CharTempEffect;
import com.vituel.dndplayer.util.gui.EffectArrayAdapter;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_SELECTED;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_SELECT;

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
        inflater.inflate(R.menu.search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
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
