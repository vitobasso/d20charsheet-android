package com.vituel.dndplayer.activity.edit_char;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.vituel.dnd_character_sheet.R;
import com.vituel.dndplayer.activity.PagerFragment;
import com.vituel.dndplayer.util.ActivityUtil;
import com.vituel.dndplayer.activity.EditAttackRoundActivity;
import com.vituel.dndplayer.dao.AttackRoundDao;
import com.vituel.dndplayer.model.Attack;
import com.vituel.dndplayer.model.AttackRound;
import com.vituel.dndplayer.model.CharBase;
import com.vituel.dndplayer.util.AttackUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.vituel.dndplayer.util.ActivityUtil.*;
import static com.vituel.dndplayer.util.font.FontUtil.MAIN_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.setFontRecursively;

/**
 * Created by Victor on 21/03/14.
 */
public class EditCharAttacksFragment extends PagerFragment<CharBase, EditCharActivity> {

    private static final String KEY_SELECTED_INDEX = "INDEX";

    private List<AttackRound> attackRounds;
    private int selectedIndex = -1;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            selectedIndex = savedInstanceState.getInt(KEY_SELECTED_INDEX, -1);
        }

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
        inflater.inflate(R.menu.add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                onSaveToModel();
                Intent intent = new Intent(activity, EditAttackRoundActivity.class);
                startActivityForResult(intent, REQUEST_CREATE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:

                AttackRound edited = (AttackRound) data.getSerializableExtra(EXTRA_EDITED);
                switch (requestCode) {
                    case REQUEST_CREATE:
                        attackRounds.add(edited);
                        break;
                    case REQUEST_EDIT:
                        assert selectedIndex >= 0;
                        attackRounds.set(selectedIndex, edited);
                        break;
                }

                //save
                AttackRoundDao dao = new AttackRoundDao(activity);
                dao.save(edited, activity.base.getId());
                dao.close();

                //update activity and ui
                onSaveToModel();
                refreshUI();
        }
    }

    @Override
    public void update(CharBase base) {
        this.attackRounds = base.getAttacks();
        refreshUI();
    }

    private void refreshUI() {
        List<AttackRound> list = new ArrayList<>(attackRounds);
        ListView listView = (ListView) root;
        listView.setAdapter(new Adapter(list));
        listView.setOnItemClickListener(new ClickListener());
    }

    private class Adapter extends ArrayAdapter<AttackRound> {

        public Adapter(List<AttackRound> objects) {
            super(activity, R.layout.edit_attack_list_round_row, R.id.name, objects);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            AttackRound attackRound = attackRounds.get(position);
            populateTextView(view, R.id.name, attackRound.getName());

            ViewGroup listRoot = findView(view, R.id.list);
            listRoot.removeAllViews();
            Map<Attack, String> attackGroups = AttackUtil.groupBonusByWeapon(attackRound.getAttacks());
            for (Attack attack : attackGroups.keySet()) {
                ViewGroup group = inflate(activity, listRoot, R.layout.edit_attack_list_group_row);
                populateTextView(group, R.id.penalties, attackGroups.get(attack));
                populateTextView(group, R.id.weapon, attack.getReferenceType().toString()); //TODO i18n
            }

            //remove button
            View removeView = view.findViewById(R.id.remove);
            removeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //update list
                    AttackRound cond = attackRounds.get(position);
                    attackRounds.remove(cond);

                    refreshUI();
                }
            });

            setFontRecursively(activity, view, MAIN_FONT);
            return view;
        }
    }

    @Override
    public void onSaveToModel() {
        activity.base.setAttacks(attackRounds);
    }


    private class ClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            onSaveToModel();
            selectedIndex = position;

            Intent intent = new Intent(activity, EditAttackRoundActivity.class);
            intent.putExtra(EXTRA_SELECTED, attackRounds.get(position));
            startActivityForResult(intent, ActivityUtil.REQUEST_EDIT);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SELECTED_INDEX, selectedIndex);
    }
}
