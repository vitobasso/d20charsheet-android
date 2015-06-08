package com.vitobasso.d20charsheet.activity.edit_char;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.activity.abstraction.AbstractSimpleListFragment;
import com.vitobasso.d20charsheet.activity.edit.EditAttackRoundActivity;
import com.vitobasso.d20charsheet.dao.AttackRoundDao;
import com.vitobasso.d20charsheet.model.character.Attack;
import com.vitobasso.d20charsheet.model.character.AttackRound;
import com.vitobasso.d20charsheet.model.character.CharBase;
import com.vitobasso.d20charsheet.util.app.ActivityUtil;
import com.vitobasso.d20charsheet.util.business.AttackUtil;

import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.EXTRA_EDITED;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.EXTRA_SELECTED;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.REQUEST_CREATE;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.REQUEST_EDIT;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.findView;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.inflate;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.populateTextView;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.populateTextViewOrHide;

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
        populateTextViewOrHide(view, R.id.name, attackRound.getName());

        ViewGroup listRoot = findView(view, R.id.list);
        listRoot.removeAllViews();
        Map<Attack, String> attackGroups = AttackUtil.groupBonusByWeapon(attackRound);
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
                        listData.set(clickedIndex, edited);
                        break;
                }

                //save
                AttackRoundDao dao = new AttackRoundDao(activity);
                dao.save(edited, data.getId());
                dao.close();

                refresh();
        }
    }

}
