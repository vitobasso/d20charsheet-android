package com.vituel.dndplayer.activity.summary;

import android.content.Intent;
import android.view.*;
import android.widget.ListView;
import android.widget.TextView;
import com.vituel.dnd_character_sheet.R;
import com.vituel.dndplayer.activity.EffectArrayAdapter;
import com.vituel.dndplayer.activity.PagerFragment;
import com.vituel.dndplayer.util.JavaUtil;
import com.vituel.dndplayer.activity.SelectTempEffectActivity;
import com.vituel.dndplayer.model.TempEffect;
import com.vituel.dndplayer.dao.TempEffectActivityDao;
import com.vituel.dndplayer.model.Character;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_SELECTED;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_SELECT;
import static com.vituel.dndplayer.util.font.FontUtil.MAIN_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.setFontRecursively;

/**
 * Created by Victor on 21/03/14.
 */
public class SummaryTempEffectsFragment extends PagerFragment<com.vituel.dndplayer.model.Character, SummaryActivity> {

    Map<TempEffect, Boolean> tempEffects;

    @Override
    protected int getLayout() {
        return R.layout.list;
    }

    @Override
    protected void onPopulate() {
        setHasOptionsMenu(true);

        if (tempEffects != null) {
            refreshUI();
        }
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
                        TempEffect selected = (TempEffect) data.getSerializableExtra(EXTRA_SELECTED);

                        //update list
                        tempEffects.put(selected, true);

                        refresh();
                }
        }
    }

    @Override
    public void update(Character character) {
        this.tempEffects = character.getBase().getTempEffects();
        refreshUI();
    }

    private void refreshUI() {
        List<TempEffect> list = new ArrayList<>(tempEffects.keySet());
        ((ListView) root).setAdapter(new Adapter(list));
    }

    private void refresh() {
        //update ui
        refreshUI();

        //update db
        TempEffectActivityDao dataSource = new TempEffectActivityDao(activity);
        dataSource.updateForChar(tempEffects, activity.character.getBase().getId());
        dataSource.close();

        //update activity
        activity.refreshUI();
    }

    private class Adapter extends EffectArrayAdapter<TempEffect> {

        public Adapter(List<TempEffect> objects) {
            super(activity, R.layout.effect_row_removable, objects);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewGroup group = (ViewGroup) super.getView(position, convertView, parent);
            TempEffect effect = JavaUtil.findByIndex(tempEffects.keySet(), position);

            TextView textView = (TextView) group.findViewById(R.id.name);
            textView.setText(effect.getName());

            assert group != null;
            group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //toggle activated
                    TempEffect cond = JavaUtil.findByIndex(tempEffects.keySet(), position);
                    boolean active = !tempEffects.get(cond);
                    tempEffects.put(cond, active);

                    refresh();
                }
            });

            View removeView = group.findViewById(R.id.remove);
            removeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //update list
                    TempEffect cond = JavaUtil.findByIndex(tempEffects.keySet(), position);
                    tempEffects.remove(cond);

                    refresh();
                }
            });

            //set transparency
            boolean active = tempEffects.get(effect);
            View effectView = group.findViewById(R.id.effect);
            effectView.setAlpha(active ? 1 : .3f);

            setFontRecursively(activity, group, MAIN_FONT);

            return group;
        }
    }

}
