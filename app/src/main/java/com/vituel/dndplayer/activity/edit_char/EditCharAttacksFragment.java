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
import com.vituel.dndplayer.activity.EditAttackRoundActivity;
import com.vituel.dndplayer.activity.abstraction.PagerFragment;
import com.vituel.dndplayer.dao.AttackRoundDao;
import com.vituel.dndplayer.model.Attack;
import com.vituel.dndplayer.model.AttackRound;
import com.vituel.dndplayer.model.CharBase;
import com.vituel.dndplayer.util.ActivityUtil;
import com.vituel.dndplayer.util.AttackUtil;
import com.vituel.dndplayer.util.i18n.EnumI18n;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_EDITED;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_SELECTED;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_CREATE;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_EDIT;
import static com.vituel.dndplayer.util.ActivityUtil.findView;
import static com.vituel.dndplayer.util.ActivityUtil.inflate;
import static com.vituel.dndplayer.util.ActivityUtil.populateTextView;
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
    protected int getLayoutResourceId() {
        return R.layout.list;
    }

    @Override
    protected void onPopulate() {
        this.attackRounds = data.getAttacks();
        List<AttackRound> list = new ArrayList<>(attackRounds); //TODO use list directly in adapter?
        ListView listView = (ListView) root;
        listView.setAdapter(new Adapter(list));
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
                Intent intent = new Intent(activity, EditAttackRoundActivity.class);
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

                AttackRound edited = (AttackRound) intent.getSerializableExtra(EXTRA_EDITED);
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
                dao.save(edited, data.getId());
                dao.close();

                //update activity and ui
                update();
        }
    }

    private class Adapter extends ArrayAdapter<AttackRound> {

        private final EnumI18n i18n;

        public Adapter(List<AttackRound> objects) {
            super(activity, R.layout.edit_attack_list_round_row, R.id.name, objects);
            i18n = new EnumI18n(activity);
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
                String weaponType = i18n.get(attack.getReferenceType()).toString();
                populateTextView(group, R.id.weapon, weaponType);
            }

            //remove button
            View removeView = view.findViewById(R.id.remove);
            removeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //update list
                    AttackRound cond = attackRounds.get(position);
                    attackRounds.remove(cond);

                    update();
                }
            });

            setFontRecursively(activity, view, MAIN_FONT);
            return view;
        }
    }

    private class ClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
