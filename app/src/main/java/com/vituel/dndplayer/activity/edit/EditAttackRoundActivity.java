package com.vituel.dndplayer.activity.edit;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.abstraction.AbstractEditActivity;
import com.vituel.dndplayer.model.character.Attack;
import com.vituel.dndplayer.model.character.AttackRound;

import static com.vituel.dndplayer.model.character.Attack.WeaponReference.MAIN_HAND;
import static com.vituel.dndplayer.util.ActivityUtil.findView;
import static com.vituel.dndplayer.util.ActivityUtil.inflate;
import static com.vituel.dndplayer.util.ActivityUtil.populateSpinnerWithEnum;
import static com.vituel.dndplayer.util.ActivityUtil.populateTextView;
import static com.vituel.dndplayer.util.ActivityUtil.readInt;
import static com.vituel.dndplayer.util.ActivityUtil.readSpinner;
import static com.vituel.dndplayer.util.ActivityUtil.readString;

public class EditAttackRoundActivity extends AbstractEditActivity<AttackRound> {

    @Override
    protected int getLayout() {
        return R.layout.edit_attack_round;
    }

    @Override
    protected void populate() {

        if (entity.getName() != null) {
            populateTextView(this, R.id.name, entity.getName());
        }

        if (entity.getAttacks().isEmpty()) {
            entity.addAttack(new Attack("", 0, MAIN_HAND));
        }

        ViewGroup listRoot = findView(this, R.id.list);
        for (Attack attack : entity.getAttacks()) {
            populateAttack(listRoot, attack);
        }

    }

    private void populateAttack(ViewGroup listRoot, Attack attack) {
        ViewGroup group = inflate(this, listRoot, R.layout.edit_attack_round_row);
        populateTextView(group, R.id.penalty, attack.getAttackBonus());
        populateSpinnerWithEnum(this, group, R.id.weapon, Attack.WeaponReference.values(), attack.getWeaponReference(), null);

        View removeBtn = findView(group, R.id.remove);
        removeBtn.setOnClickListener(new ClickRemove(group));
    }

    @Override
    protected AttackRound save() {

        String name = readString(this, R.id.name);
        entity.setName(name);

        entity.getAttacks().clear();
        ViewGroup effectsRoot = findView(this, R.id.list);
        for (int i = 0; i < effectsRoot.getChildCount(); i++) {
            Attack attack = readAttackRow(effectsRoot, i, name);
            entity.addAttack(attack);
        }

        return entity;
    }

    private Attack readAttackRow(ViewGroup effectsRoot, int i, String name) {
        ViewGroup group = (ViewGroup) effectsRoot.getChildAt(i);
        Attack.WeaponReference weaponRef = readSpinner(group, R.id.weapon);
        int penalty = readInt(group, R.id.penalty);
        return new Attack(name, penalty, weaponRef);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:

                ViewGroup listRoot = findView(this, R.id.list);
                populateAttack(listRoot, new Attack("", 0, MAIN_HAND));

                return true;
            default:
                return super.onMenuItemSelected(featureId, item);
        }
    }


    private class ClickRemove implements View.OnClickListener {

        ViewGroup group;

        private ClickRemove(ViewGroup group) {
            this.group = group;
        }

        @Override
        public void onClick(View v) {
            ViewGroup listRoot = findView(EditAttackRoundActivity.this, R.id.list);
            listRoot.removeView(group);
        }
    }
}
