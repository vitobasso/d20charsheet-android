package com.vituel.dndplayer.activity.edit_char;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.EditAttackRoundActivity;
import com.vituel.dndplayer.activity.abstraction.AbstractSimpleListFragment;
import com.vituel.dndplayer.dao.AttackRoundDao;
import com.vituel.dndplayer.model.Attack;
import com.vituel.dndplayer.model.AttackRound;
import com.vituel.dndplayer.model.CharBase;
import com.vituel.dndplayer.util.ActivityUtil;
import com.vituel.dndplayer.util.AttackUtil;

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

/**
 * Created by Victor on 21/03/14.
 */
public class EditCharAttacksFragment extends AbstractSimpleListFragment<CharBase, EditCharActivity, AttackRound> {

    @Override
    protected int getRowLayoutResourceId() {
        return R.layout.edit_attack_list_round_row;
    }

    @Override
    protected List<AttackRound> getListData() {
        return data.getAttacks();
    }

    @Override
    protected void onPopulateRow(View view, AttackRound attackRound) {
        populateTextView(view, R.id.name, attackRound.getName());

        ViewGroup listRoot = findView(view, R.id.list);
        listRoot.removeAllViews();
        Map<Attack, String> attackGroups = AttackUtil.groupBonusByWeapon(attackRound.getAttacks());
        for (Attack attack : attackGroups.keySet()) {
            ViewGroup group = inflate(activity, listRoot, R.layout.edit_attack_list_group_row);
            populateTextView(group, R.id.penalties, attackGroups.get(attack));
            String weaponType = i18n.get(attack.getWeaponReference()).toString();
            populateTextView(group, R.id.weapon, weaponType);
        }
    }

    @Override
    protected void onClickRow(AttackRound element) {
        Intent intent = new Intent(activity, EditAttackRoundActivity.class);
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
                        listData.add(edited);
                        break;
                    case REQUEST_EDIT:
                        assert clickedIndex >= 0;
                        listData.set(clickedIndex, edited);
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

}
