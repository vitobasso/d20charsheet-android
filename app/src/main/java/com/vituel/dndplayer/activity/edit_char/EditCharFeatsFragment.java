package com.vituel.dndplayer.activity.edit_char;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.EffectArrayAdapter;
import com.vituel.dndplayer.activity.SelectTraitActivity;
import com.vituel.dndplayer.activity.abstraction.PagerFragment;
import com.vituel.dndplayer.model.CharBase;
import com.vituel.dndplayer.model.Trait;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_SELECTED;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_TYPE;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_SELECT;
import static com.vituel.dndplayer.util.font.FontUtil.MAIN_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.setFontRecursively;

/**
 * Created by Victor on 21/03/14.
 */
public class EditCharFeatsFragment extends PagerFragment<CharBase, EditCharActivity> {

    List<Trait> feats;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity.updateFragment(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.list;
    }

    @Override
    protected void onPopulate() {
        setHasOptionsMenu(true);
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
                onSaveToModel();
                Intent intent = new Intent(activity, SelectTraitActivity.class);
                intent.putExtra(EXTRA_TYPE, Trait.Type.FEAT);
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
                        Trait selected = (Trait) data.getSerializableExtra(EXTRA_SELECTED);

                        //update list
                        feats.add(selected);

                        refreshUI();
                }
        }
    }

    @Override
    public void update(CharBase base) {
        this.feats = base.getFeats();
        refreshUI();
    }

    private void refreshUI() {
        List<Trait> list = new ArrayList<>(feats);
        ((ListView) root).setAdapter(new Adapter(list));
    }

    private class Adapter extends EffectArrayAdapter<Trait> {

        public Adapter(List<Trait> objects) {
            super(activity, R.layout.effect_row_removable, objects);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            assert v != null;
 
            View removeView = v.findViewById(R.id.remove);
            removeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //update list
                    Trait cond = feats.get(position);
                    feats.remove(cond);

                    refreshUI();
                }
            });

            setFontRecursively(activity, v, MAIN_FONT);
            return v;
        }
    }

    @Override
    public void onSaveToModel() {
        activity.base.setFeats(feats);
    }
}
