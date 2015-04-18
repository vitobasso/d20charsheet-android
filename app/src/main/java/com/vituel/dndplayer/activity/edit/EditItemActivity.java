package com.vituel.dndplayer.activity.edit;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.abstraction.AbstractEditEffectActivity;
import com.vituel.dndplayer.model.DiceRoll;
import com.vituel.dndplayer.model.item.Item;
import com.vituel.dndplayer.model.item.SlotType;
import com.vituel.dndplayer.model.item.WeaponItem;
import com.vituel.dndplayer.model.item.WeaponProperties;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.vituel.dndplayer.model.item.Item.ItemType;
import static com.vituel.dndplayer.util.ActivityUtil.findView;
import static com.vituel.dndplayer.util.ActivityUtil.populateSpinnerWithEnum;
import static com.vituel.dndplayer.util.ActivityUtil.populateTextView;
import static com.vituel.dndplayer.util.ActivityUtil.readInt;
import static com.vituel.dndplayer.util.ActivityUtil.readSpinner;
import static com.vituel.dndplayer.util.ActivityUtil.readString;
import static com.vituel.dndplayer.util.ActivityUtil.validateSpinner;
import static com.vituel.dndplayer.util.ActivityUtil.validateText;

public class EditItemActivity extends AbstractEditEffectActivity<Item> {

    @Override
    protected int getLayout() {
        return R.layout.edit_item;
    }

    @Override
    protected void populate() {

        //effect fields
        super.populate();

        //basic fields
        populateSpinnerWithEnum(this, null, R.id.slot, SlotType.values(), entity.getSlotType(), new SlotSelectionListener());
        populateSpinnerWithEnum(this, null, R.id.itemType, ItemType.values(), entity.getItemType(), new ItemTypeSelectionListener());

        //weapon fields
        if (entity instanceof WeaponItem && entity.getItemType() == ItemType.WEAPON) {
            WeaponItem weapon = (WeaponItem) entity;
            WeaponProperties wp = weapon.getWeaponProperties();

            View weaponFields = findViewById(R.id.weapon_fields);
            weaponFields.setVisibility(VISIBLE);

            populateTextView(this, R.id.damage, wp.getDamage());
            populateTextView(this, R.id.crit_range, wp.getCritical().getRange());
            populateTextView(this, R.id.crit_mult, wp.getCritical().getMultiplier());
        }

    }

    @Override
    protected boolean validate() {
        boolean allValid = super.validate();
        allValid &= validateSpinner(this, R.id.slot);

        SlotType slotType = readSpinner(this, R.id.slot);
        if (slotType == SlotType.HELD) {
            allValid &= validateSpinner(this, R.id.itemType);
        }

        ItemType itemType = readSpinner(this, R.id.itemType);
        if (itemType == ItemType.WEAPON) {
            allValid &= validateText(this, R.id.damage);
            allValid &= validateText(this, R.id.crit_range);
            allValid &= validateText(this, R.id.crit_mult);
        }

        return allValid;
    }

    @Override
    protected Item save() {

        //item type
        ItemType itemType = readSpinner(this, R.id.itemType);
        if (itemType == ItemType.WEAPON) {
            long id = entity.getId();
            entity = new WeaponItem();
            entity.setId(id);
        }

        //effect fields
        super.save();

        //basic fields
        entity.setItemType(itemType);
        entity.setSlotType((SlotType) readSpinner(this, R.id.slot));

        //weapon fields
        if (entity.getItemType() == ItemType.WEAPON) {
            WeaponItem weapon = (WeaponItem) entity;
            WeaponProperties wp = weapon.getWeaponProperties();

            String dmgStr = readString(this, R.id.damage);
            wp.setDamage(new DiceRoll(dmgStr));
            wp.getCritical().setRange(readInt(this, R.id.crit_range));
            wp.getCritical().setMultiplier(readInt(this, R.id.crit_mult));
        }

        return entity;
    }

    private class SlotSelectionListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            View label = findView(EditItemActivity.this, R.id.itemTypeLabel);
            Spinner spinner = findView(EditItemActivity.this, R.id.itemType);
            if (position >= 0 && SlotType.values()[position] == SlotType.HELD) {
                label.setVisibility(VISIBLE);
                spinner.setVisibility(VISIBLE);
            } else {
                label.setVisibility(GONE);
                spinner.setVisibility(GONE);
                //TODO hide weapon fields
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

    private class ItemTypeSelectionListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            View weaponFields = findView(EditItemActivity.this, R.id.weapon_fields);
            if (position >= 0 && ItemType.values()[position] == ItemType.WEAPON) {
                weaponFields.setVisibility(VISIBLE);
            } else {
                weaponFields.setVisibility(GONE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

}
